package andersen.parking.consumer;


import andersen.parking.model.TransactionEvent;
import andersen.parking.service.TransactionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@Configuration
public class TransactionEventConsumer {

    private final TransactionEventService transactionEventService;

    @KafkaListener(topics = "${kafka.topics.transaction}", groupId = "${kafka.group-ids.parking-service}")
    public void consumeMessage(TransactionEvent event) {
        transactionEventService.processEvent(event)
                .subscribe();
    }

}
