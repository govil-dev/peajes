package co.quind.peajes.tollcollection.infrastructure.web;

import co.quind.peajes.tollcollection.domain.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InsufficientBalanceException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleInsufficientBalance(
			InsufficientBalanceException ex) {
		log.warn("Insufficient balance: {}", ex.getMessage());
		return Mono.just(ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(ErrorResponse.of("INSUFFICIENT_BALANCE", ex.getMessage(), null)));
	}

	@ExceptionHandler(TagInactiveException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleTagInactive(TagInactiveException ex) {
		log.warn("Tag inactive: {}", ex.getMessage());
		return Mono.just(ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(ErrorResponse.of("TAG_INACTIVE", ex.getMessage(), null)));
	}

	@ExceptionHandler(LaneNotOpenException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleLaneNotOpen(LaneNotOpenException ex) {
		log.warn("Lane not open: {}", ex.getMessage());
		return Mono.just(ResponseEntity
			.status(HttpStatus.CONFLICT)
			.body(ErrorResponse.of("LANE_NOT_OPEN", ex.getMessage(), null)));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleValidation(WebExchangeBindException ex) {
		String details = ex.getBindingResult().getFieldErrors().stream()
			.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
			.collect(Collectors.joining(", "));
		log.warn("Validation error: {}", details);
		return Mono.just(ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.of("VALIDATION_ERROR", details, null)));
	}

	@ExceptionHandler(Exception.class)
	public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
		log.error("Unexpected error", ex);
		return Mono.just(ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.of("INTERNAL_ERROR", ex.getMessage(), null)));
	}

}
