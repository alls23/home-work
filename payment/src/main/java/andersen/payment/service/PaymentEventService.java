package andersen.payment.service;

import andersen.payment.model.ParkingOrderEvent;
import reactor.core.publisher.Mono;

public interface PaymentEventService {
    Mono<Void> processPayment(ParkingOrderEvent event);
}
