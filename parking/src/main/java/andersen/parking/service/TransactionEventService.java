package andersen.parking.service;

import andersen.parking.model.TransactionEvent;
import reactor.core.publisher.Mono;

public interface TransactionEventService {
    Mono<Void> processEvent(TransactionEvent event);
}
