package co.quind.peajes.accountmanagement.infrastructure.kafka;

import co.quind.peajes.accountmanagement.domain.event.AccountRecharged;
import co.quind.peajes.accountmanagement.domain.port.out.AccountEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventKafkaPublisher implements AccountEventPublisher {

	private final ReactiveKafkaProducerTemplate<String, Object> kafkaTemplate;

	@Value("${app.account.kafka.topics.account-events:account.events.v1}")
	private String accountEventsTopic;

	@Override
	public Mono<Void> publishAccountRecharged(AccountRecharged event) {
		return kafkaTemplate.send(accountEventsTopic, event.accountId(), event)
			.doOnSuccess(result -> log.info("AccountRecharged published: topic={}, accountId={}, offset={}",
				accountEventsTopic, event.accountId(), result.recordMetadata().offset()))
			.doOnError(ex -> log.error("Failed to publish AccountRecharged: accountId={}", event.accountId(), ex))
			.then();
	}
}
