package andersen.parking.producer;

import andersen.parking.model.ParkingOrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingOrderProducer {
    private static final String TOPIC = "parking";
    private final KafkaTemplate<String, ParkingOrderEvent> kafkaTemplate;

    public void sendParkingOrder(ParkingOrderEvent order) {
        log.info("Producing message: {}", order);
        Message<ParkingOrderEvent> msg = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
        kafkaTemplate.send(msg);
    }
}
