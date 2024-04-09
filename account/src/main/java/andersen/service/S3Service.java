package andersen.service;

import andersen.configuration.aws.S3ClientConfigurationProperties;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final String METADATA_FILENAME_KEY = "filename";

    private final S3AsyncClient s3client;
    private final S3Presigner s3Presigner;
    private final S3ClientConfigurationProperties s3config;


    public URI getPresignedURL(String fileKey) {
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(s3config.getLinkDuration()))
                .getObjectRequest(getObject -> getObject
                        .bucket(s3config.getBucket())
                        .key(fileKey)
                        .build())
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.httpRequest().getUri();
    }

    public Mono<String> saveFile(Flux<DataBuffer> bufferFlux, String fileS3key, String filename) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put(METADATA_FILENAME_KEY, filename);

        final UploadState uploadState = new UploadState(s3config.getBucket(), fileS3key);
        CompletableFuture<CreateMultipartUploadResponse> uploadRequest = s3client
                .createMultipartUpload(CreateMultipartUploadRequest.builder()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM.toString())
                        .key(fileS3key)
                        .metadata(metadata)
                        .bucket(s3config.getBucket())
                        .build());

        return Mono.fromFuture(uploadRequest)
                .flatMapMany(response -> {
                    checkResult(response);
                    uploadState.setUploadId(response.uploadId());
                    return bufferFlux;
                })
                .bufferUntil(buffer -> {
                    uploadState.setBuffered(uploadState.getBuffered() + buffer.readableByteCount());
                    if (uploadState.getBuffered() >= s3config.getMultipartMinPartSize()) {
                        uploadState.setBuffered(0);
                        return true;
                    } else {
                        return false;
                    }
                })
                .map(S3Service::concatBuffers)
                .flatMap(buffer -> uploadPart(uploadState, buffer))
                .reduce(uploadState, (state, completedPart) -> {
                    state.getCompletedParts().put(completedPart.partNumber(), completedPart);
                    return state;
                })
                .flatMap(this::completeUpload)
                .map(response -> {
                    checkResult(response);
                    return uploadState.getFilekey();
                });
    }

    private Mono<CompleteMultipartUploadResponse> completeUpload(UploadState state) {
        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
                .parts(state.getCompletedParts().values())
                .build();
        return Mono.fromFuture(s3client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(state.getBucket())
                .uploadId(state.getUploadId())
                .multipartUpload(multipartUpload)
                .key(state.getFilekey())
                .build()));
    }

    private static ByteBuffer concatBuffers(List<DataBuffer> buffers) {

        int partSize = 0;
        for (DataBuffer b : buffers) {
            partSize += b.readableByteCount();
        }

        ByteBuffer partData = ByteBuffer.allocate(partSize);
        buffers.forEach(buffer -> partData.put(buffer.toByteBuffer()));

        partData.rewind();
        return partData;
    }

    private Mono<CompletedPart> uploadPart(UploadState uploadState, ByteBuffer buffer) {
        final int partNumber = uploadState.getIncrementedPartCounter();

        CompletableFuture<UploadPartResponse> request = s3client.uploadPart(
                UploadPartRequest.builder()
                        .bucket(uploadState.getBucket())
                        .key(uploadState.getFilekey())
                        .partNumber(partNumber)
                        .uploadId(uploadState.getUploadId())
                        .contentLength((long) buffer.capacity())
                        .build(),
                AsyncRequestBody.fromPublisher(Mono.just(buffer))
        );

        return Mono.fromFuture(request)
                .map(uploadPartResult -> {
                    checkResult(uploadPartResult);
                    return CompletedPart.builder()
                            .eTag(uploadPartResult.eTag())
                            .partNumber(partNumber)
                            .build();
                });
    }

    private static void checkResult(SdkResponse result) {
        if (result.sdkHttpResponse() == null || !result.sdkHttpResponse().isSuccessful()) {
            throw new RuntimeException("Failed to check Oracle S3 response");
        }
    }

    @Data
    static class UploadState {

        String bucket;
        String filekey;
        String uploadId;
        int partCounter;
        Map<Integer, CompletedPart> completedParts = new HashMap<>();
        int buffered = 0;

        public UploadState(String bucket, String filekey) {
            this.bucket = bucket;
            this.filekey = filekey;
        }

        public int getIncrementedPartCounter() {
            return ++this.partCounter;
        }

    }

}
