package andersen.payment.consumer;

import andersen.payment.model.ParkingOrderEvent;
import andersen.payment.model.TransactionEvent;
import andersen.payment.service.PaymentEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
public class ParkingOrderConsumer {

    private final PaymentEventService transactionEventService;

    @KafkaListener(topics = "${kafka.topics.parking}", groupId = "${kafka.group-ids.payment-service}")
    public void consumeMessage(ParkingOrderEvent event) {
        transactionEventService.processPayment(event)
                .subscribe();
    }

    @KafkaListener(topics = "transaction", groupId = "${kafka.group-ids.payment-service}")
    public void consumeMessage1(TransactionEvent event) {
        System.out.println("TransactionEvent: " + event.toString());
    }

}
