package co.quind.peajes.tollcollection.domain.port.out;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface DomainEventPublisher {
	Mono<Void> publish(Object domainEvent);
	Flux<Void> publishAll(List<Object> domainEvents);
}
