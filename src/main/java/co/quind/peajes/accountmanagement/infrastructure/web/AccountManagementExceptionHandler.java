package co.quind.peajes.accountmanagement.infrastructure.web;

import co.quind.peajes.accountmanagement.domain.exception.AccountFrozenException;
import co.quind.peajes.accountmanagement.domain.exception.AccountNotFoundException;
import co.quind.peajes.accountmanagement.domain.exception.PaymentGatewayException;
import co.quind.peajes.tollcollection.infrastructure.web.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice(basePackages = "co.quind.peajes.accountmanagement")
public class AccountManagementExceptionHandler {

	@ExceptionHandler(AccountFrozenException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleAccountFrozen(AccountFrozenException ex) {
		log.warn("Account frozen: {}", ex.getMessage());
		return Mono.just(ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(ErrorResponse.of("ACCOUNT_FROZEN", ex.getMessage(), null)));
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleAccountNotFound(AccountNotFoundException ex) {
		log.warn("Account not found: {}", ex.getMessage());
		return Mono.just(ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.of("ACCOUNT_NOT_FOUND", ex.getMessage(), null)));
	}

	@ExceptionHandler(PaymentGatewayException.class)
	public Mono<ResponseEntity<ErrorResponse>> handlePaymentGateway(PaymentGatewayException ex) {
		log.error("Payment gateway error: {}", ex.getMessage());
		return Mono.just(ResponseEntity
			.status(HttpStatus.SERVICE_UNAVAILABLE)
			.body(ErrorResponse.of("PAYMENT_GATEWAY_UNAVAILABLE", ex.getMessage(), null)));
	}
}
