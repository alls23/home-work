package andersen.parking.service.impl;

import andersen.parking.dto.request.BookParkingRequest;
import andersen.parking.dto.response.ParkingSpaceResponse;
import andersen.parking.model.ParkingOrder;
import andersen.parking.model.ParkingOrderEvent;
import andersen.parking.producer.ParkingOrderProducer;
import andersen.parking.repository.ParkingOrderRepository;
import andersen.parking.repository.ParkingSpaceRepository;
import andersen.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingOrderRepository parkingOrderRepository;
    private final ParkingOrderProducer parkingOrderProducer;
    private final PriceServiceImpl priceService;

    public Mono<Void> bookParkingSpace(BookParkingRequest request) {
        return parkingSpaceRepository.findById(request.spaceId())
                .flatMap(space -> {
                    space.setCarId(request.carId());
                    space.setOccupied(true);
                    space.setStartTime(request.startTime());
                    space.setEndTime(request.endTime());
                    return parkingSpaceRepository.save(space);
                })
                .flatMap(__ -> parkingOrderRepository.save(ParkingOrder.create()))
                .doOnSuccess(order -> parkingOrderProducer.sendParkingOrder(ParkingOrderEvent.builder()
                        .parkingId(order.getId())
                        .price(priceService.calculatePrice())
                        .build()))
                .then();

    }

    public Mono<Void> releaseParking(Integer parkingId) {
        return parkingSpaceRepository.findById(parkingId)
                .flatMap(space -> {
                    space.setCarId(null);
                    space.setOccupied(false);
                    space.setStartTime(null);
                    space.setEndTime(null);
                    return parkingSpaceRepository.save(space);
                })
                .then();
    }

    public Flux<ParkingSpaceResponse> getParkingSpaces(int parkingId) {
        return parkingSpaceRepository.findAllByParkingId(parkingId)
                .map(ParkingSpaceResponse::fromModel);
    }
}
