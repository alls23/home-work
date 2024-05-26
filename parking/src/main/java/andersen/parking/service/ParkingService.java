package andersen.parking.service;

import andersen.parking.dto.request.BookParkingRequest;
import andersen.parking.dto.response.ParkingSpaceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParkingService {
    Mono<Void> bookParkingSpace(BookParkingRequest request);
    Flux<ParkingSpaceResponse> getParkingSpaces(int parkingId);
    Mono<Void> releaseParking(Integer parkingId);
}
