package co.quind.peajes.tollcollection.domain.port.out;

import reactor.core.publisher.Mono;

/** Puerto de salida para publicación de eventos en Kafka. */
public interface EventPublisherPort {
    Mono<Void> publishToTransactions(Object event);
    Mono<Void> publishToAccounts(Object event);
}
