package co.quind.peajes.tollcollection.infrastructure.kafka;

import co.quind.peajes.tollcollection.domain.event.*;
import co.quind.peajes.tollcollection.domain.port.out.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements DomainEventPublisher {

	private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

	@Value("${app.kafka.topics.toll-passes:toll.passes.v1}")
	private String tollPassesTopic;

	@Value("${app.kafka.topics.toll-transactions:toll.transactions.v1}")
	private String tollTransactionsTopic;

	@Value("${app.kafka.topics.account-alerts:account.alerts.v1}")
	private String accountAlertsTopic;

	@Override
	public Mono<Void> publish(Object domainEvent) {
		String topic = getTopicForEvent(domainEvent);
		String key = getKeyForEvent(domainEvent);

		return kafkaTemplate.send(topic, key, domainEvent)
			.doOnSuccess(result -> log.info("Event published: topic={}, key={}, offset={}",
				topic, key, result.recordMetadata().offset()))
			.doOnError(ex -> log.error("Failed to publish event: topic={}, key={}", topic, key, ex))
			.then();
	}

	@Override
	public Flux<Void> publishAll(List<Object> domainEvents) {
		return Flux.fromIterable(domainEvents)
			.concatMap(this::publish);
	}

	private String getTopicForEvent(Object event) {
		return switch (event) {
			case TollPassRegistered _ -> tollPassesTopic;
			case TransactionAuthorized _ -> tollTransactionsTopic;
			case TransactionDeclined _ -> tollTransactionsTopic;
			case AccountBalanceLow _ -> accountAlertsTopic;
			default -> throw new IllegalArgumentException("Unknown event type: " + event.getClass().getName());
		};
	}

	private String getKeyForEvent(Object event) {
		return switch (event) {
			case TollPassRegistered e -> e.passId();
			case TransactionAuthorized e -> e.passId();
			case TransactionDeclined e -> e.passId();
			case AccountBalanceLow e -> e.accountId() != null ? e.accountId() : "unknown";
			default -> "unknown";
		};
	}

}
