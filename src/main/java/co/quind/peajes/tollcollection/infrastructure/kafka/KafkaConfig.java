package co.quind.peajes.tollcollection.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

	@Bean
	public NewTopic tollPassesTopic() {
		return TopicBuilder.name("toll.passes.v1")
			.partitions(12)
			.replicas(3)
			.config("retention.ms", String.valueOf(7 * 24 * 60 * 60 * 1000))
			.build();
	}

	@Bean
	public NewTopic tollTransactionsTopic() {
		return TopicBuilder.name("toll.transactions.v1")
			.partitions(12)
			.replicas(3)
			.config("retention.ms", String.valueOf(30 * 24 * 60 * 60 * 1000))
			.build();
	}

	@Bean
	public NewTopic accountAlertsTopic() {
		return TopicBuilder.name("account.alerts.v1")
			.partitions(6)
			.replicas(3)
			.config("retention.ms", String.valueOf(7 * 24 * 60 * 60 * 1000))
			.build();
	}

	@Bean
	public ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate(
			KafkaProperties kafkaProperties) {
		Map<String, Object> props = new HashMap<>(
			kafkaProperties.buildProducerProperties(null));
		SenderOptions<String, Object> senderOptions = SenderOptions
			.create(props);
		return new ReactiveKafkaProducerTemplate<>(senderOptions);
	}

}
