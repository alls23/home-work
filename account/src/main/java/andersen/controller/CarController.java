package andersen.controller;

import andersen.configuration.UserPrincipal;
import andersen.dto.request.CarRegistrationRequest;
import andersen.dto.response.S3UploadResponse;
import andersen.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/login")
    public Mono<String> dummy() {
        return Mono.just("Hello, World!");
    }

    @PostMapping("/register")
    public Mono<Integer> registerCar(@ModelAttribute UserPrincipal userPrincipal, @RequestBody CarRegistrationRequest request) {
        return carService.registerCar(request, userPrincipal.getEmail());
    }

    @PostMapping("/photo")
    public Flux<S3UploadResponse> addCarPhoto(@ModelAttribute UserPrincipal user, @RequestPart("file") Flux<FilePart> fileFlux) {
        return carService.savePhoto(fileFlux, user.getEmail());
    }
}
