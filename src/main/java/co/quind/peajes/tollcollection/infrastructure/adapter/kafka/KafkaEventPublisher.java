package co.quind.peajes.tollcollection.infrastructure.adapter.kafka;

import co.quind.peajes.tollcollection.domain.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Publica eventos de dominio en Kafka con semántica at-least-once.
 * El producer está configurado con idempotencia habilitada.
 */
@Component
public class KafkaEventPublisher implements EventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String transactionsTopic;
    private final String accountsTopic;
    private final String incidentsTopic;

    public KafkaEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${toll.kafka.topics.transactions:toll.transactions.v1}") String transactionsTopic,
            @Value("${toll.kafka.topics.accounts:account.alerts.v1}") String accountsTopic,
            @Value("${toll.kafka.topics.incidents:incidents.events.v1}") String incidentsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionsTopic = transactionsTopic;
        this.accountsTopic = accountsTopic;
        this.incidentsTopic = incidentsTopic;
    }

    @Override
    public Mono<Void> publishToTransactions(Object event) {
        return publish(transactionsTopic, event);
    }

    @Override
    public Mono<Void> publishToAccounts(Object event) {
        return publish(accountsTopic, event);
    }

	@Override
	public Mono<Void> publishToIncidents(Object event) {
		return publish(incidentsTopic, event);
	}

    private Mono<Void> publish(String topic, Object event) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, event).toCompletableFuture())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(r -> log.info("Evento publicado en topic={} partition={} offset={}",
                        r.getRecordMetadata().topic(),
                        r.getRecordMetadata().partition(),
                        r.getRecordMetadata().offset()))
                .doOnError(e -> log.error("Error publicando evento en topic={}: {}", topic, e.getMessage()))
                .then();
    }
}
