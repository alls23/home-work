package andersen.parking.service;

import andersen.parking.dto.request.BookParkingRequest;
import andersen.parking.dto.response.ParkingSpaceResponse;
import andersen.parking.repository.ParkingRepository;
import andersen.parking.repository.ParkingSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingService {
    private final ParkingRepository parkingRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public Mono<Void> bookParkingSpace(BookParkingRequest request) {
        return parkingSpaceRepository.findById(request.spaceId())
                .flatMap(space -> {
                    space.setCarId(request.carId());
                    space.setOccupied(true);
                    space.setStartTime(request.startTime());
                    space.setEndTime(request.endTime());
                    return parkingSpaceRepository.save(space);
                })
                .then();

    }

    public Flux<ParkingSpaceResponse> getParkingSpaces(int parkingId) {
        return parkingSpaceRepository.findAllByParkingId(parkingId)
                .map(ParkingSpaceResponse::fromModel);
    }
}
