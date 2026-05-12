package co.quind.peajes.accountmanagement.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class PayUWebClientConfig {

	@Bean
	public WebClient payuWebClient(
			WebClient.Builder builder,
			@Value("${app.payu.base-url:http://localhost:8082}") String baseUrl) {
		return builder
			.baseUrl(baseUrl)
			.defaultHeader("Content-Type", "application/json")
			.build();
	}
}
