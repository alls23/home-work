package andersen.parking.service.impl;

import andersen.parking.enums.OrderStatus;
import andersen.parking.enums.TransactionStatus;
import andersen.parking.model.ParkingOrder;
import andersen.parking.model.TransactionEvent;
import andersen.parking.repository.ParkingOrderRepository;
import andersen.parking.service.ParkingService;
import andersen.parking.service.TransactionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class TransactionEventServiceImpl implements TransactionEventService {

    private final ParkingOrderRepository parkingOrderRepository;
    private final ParkingService parkingService;

    public Mono<Void> processEvent(TransactionEvent event) {
        return parkingOrderRepository.findById(event.getOrderId())
                .subscribeOn(Schedulers.boundedElastic())
                .map(order -> setStatus(event, order))
                .flatMap(order -> {
                    if (event.getStatus().equals(TransactionStatus.UNSUCCESSFUL)) {
                        order.setStatus(OrderStatus.FAILED);
                        return parkingService.releaseParking(order.getId())
                                .then(Mono.just(order));
                    }
                    order.setStatus(OrderStatus.COMPLETED);
                    return Mono.just(order);
                })
                .flatMap(parkingOrderRepository::save)
                .then();
    }

    private ParkingOrder setStatus(TransactionEvent transactionEvent, ParkingOrder order) {
        order.setStatus(TransactionStatus.SUCCESSFUL.equals(transactionEvent.getStatus())
                ? OrderStatus.COMPLETED
                : OrderStatus.FAILED);
        return order;
    }
}
