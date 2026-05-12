package co.quind.peajes.accountmanagement.infrastructure.web;

import co.quind.peajes.accountmanagement.application.command.InitiatePseRechargeCommand;
import co.quind.peajes.accountmanagement.application.command.ProcessPseRechargeCommand;
import co.quind.peajes.accountmanagement.application.dto.PayUNotificationRequest;
import co.quind.peajes.accountmanagement.application.dto.PseRechargeRequest;
import co.quind.peajes.accountmanagement.application.dto.PseRechargeResponse;
import co.quind.peajes.accountmanagement.application.dto.RechargeInitiatedResponse;
import co.quind.peajes.accountmanagement.domain.port.in.InitiatePseRechargeUseCase;
import co.quind.peajes.accountmanagement.domain.port.in.ProcessPseRechargeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PseRechargeController {

	private final InitiatePseRechargeUseCase initiateUseCase;
	private final ProcessPseRechargeUseCase processUseCase;

	/**
	 * Inicia una recarga de saldo vía PSE para una cuenta prepago.
	 * Llama al SDK de PayU y retorna la URL de redirección para el pago.
	 */
	@PostMapping("/api/v1/accounts/{accountId}/recharge/pse")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Mono<RechargeInitiatedResponse> initiateRecharge(
			@PathVariable String accountId,
			@Valid @RequestBody PseRechargeRequest request) {
		var command = new InitiatePseRechargeCommand(
			accountId,
			request.amount().toPlainString(),
			request.currency()
		);
		return initiateUseCase.initiate(command)
			.doOnSuccess(r -> log.info("PSE recharge initiated: accountId={}, externalRef={}",
				accountId, r.externalReferenceId()))
			.doOnError(ex -> log.error("Error initiating PSE recharge: accountId={}", accountId, ex));
	}

	/**
	 * Webhook de PayU: recibe la notificación de resultado de pago PSE.
	 * Procesa la acreditación o fallo de la recarga de forma idempotente.
	 */
	@PostMapping("/api/v1/payments/pse/notifications")
	@ResponseStatus(HttpStatus.OK)
	public Mono<PseRechargeResponse> processNotification(
			@Valid @RequestBody PayUNotificationRequest request) {
		var command = new ProcessPseRechargeCommand(
			request.accountId(),
			request.externalReferenceId(),
			request.amount(),
			request.currency(),
			request.paymentStatus(),
			request.failureReason()
		);
		return processUseCase.process(command)
			.doOnSuccess(r -> log.info("PSE notification processed: externalRef={}, status={}",
				request.externalReferenceId(), r.status()))
			.doOnError(ex -> log.error("Error processing PSE notification: externalRef={}",
				request.externalReferenceId(), ex));
	}
}
