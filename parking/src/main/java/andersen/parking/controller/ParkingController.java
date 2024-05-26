package andersen.parking.controller;

import andersen.parking.dto.request.BookParkingRequest;
import andersen.parking.dto.response.ParkingSpaceResponse;
import andersen.parking.model.ParkingOrderEvent;
import andersen.parking.producer.ParkingOrderProducer;
import andersen.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {
    private final ParkingService parkingService;
    private final ParkingOrderProducer parkingOrderProducer;

    @GetMapping("/login")
    public Mono<String> dummy() {
        parkingOrderProducer.sendParkingOrder(ParkingOrderEvent.builder()
                        .parkingId(1)
                .build());
        return Mono.just("Hello");
    }

    @GetMapping("{parking_id}]")
    public Flux<ParkingSpaceResponse> getParkingSpaces(int parkingId) {
        return parkingService.getParkingSpaces(parkingId);
    }

    @PostMapping("/book")
    public Mono<Void> bookParkingSpace(@RequestBody BookParkingRequest request) {
        return parkingService.bookParkingSpace(request);
    }

}
