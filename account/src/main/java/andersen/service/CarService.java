package andersen.service;

import andersen.dto.request.CarRegistrationRequest;
import andersen.dto.response.S3UploadResponse;
import andersen.model.Car;
import andersen.model.UserFile;
import andersen.repository.CarRepository;
import andersen.repository.UserFileRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final UserFileRepository userFileRepository;
    private final S3Service s3Service;

    public Flux<S3UploadResponse> savePhoto(Flux<FilePart> fluxFile, String email) {
        final String uuid = UUID.randomUUID().toString();
        return fluxFile
                .flatMap(part -> s3Service.saveFile(part.content(), uuid, part.filename()))
                .flatMap(__ -> carRepository.findByUserEmail(email))
                .flatMap(car -> userFileRepository.save(UserFile.builder()
                        .carId(car.getId())
                        .fileKey(uuid)
                        .presignedUrl(s3Service.getPresignedURL(uuid).toString())
                        .build()))
                .map(file -> S3UploadResponse.builder()
                        .fileKey(file.getFileKey())
                        .presignedUrl(file.getPresignedUrl())
                        .build());
    }

    public Mono<Integer> registerCar(CarRegistrationRequest request, String email) {
        return carRepository.save(Car.builder()
                        .brand(request.brand())
                        .userEmail(email)
                        .model(request.model())
                        .number(request.plateNumber())
                        .type(request.type())
                        .build())
                .map(Car::getId);
    }
}
