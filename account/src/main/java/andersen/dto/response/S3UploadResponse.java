package andersen.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S3UploadResponse {
    private String fileKey;
    private String presignedUrl;
}
