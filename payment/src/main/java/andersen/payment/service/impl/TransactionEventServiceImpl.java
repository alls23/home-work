package andersen.payment.service.impl;

import andersen.payment.enums.TransactionStatus;
import andersen.payment.model.ParkingOrderEvent;
import andersen.payment.model.TransactionEvent;
import andersen.payment.producer.TransactionEventProducer;
import andersen.payment.service.PaymentEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionEventServiceImpl implements PaymentEventService {

    private final TransactionEventProducer transactionEventProducer;

    @Override
    public Mono<Void> processPayment(ParkingOrderEvent event) {
        return Mono.just(event)
                .doOnSuccess(e -> log.info("Payment for order {} was processed", e.getParkingId()))
                .map(e -> {
                    if(e.getPrice() > 70.0) {
                        return new TransactionEvent(e.getParkingId(), TransactionStatus.UNSUCCESSFUL);
                    } else {
                        return new TransactionEvent(e.getParkingId(), TransactionStatus.SUCCESSFUL);
                    }
                })
                .doOnNext(transactionEventProducer::sendTransactionEvent)
                .then();
    }
}
