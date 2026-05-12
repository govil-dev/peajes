package co.quind.peajes.accountmanagement.domain.port.out;

import co.quind.peajes.accountmanagement.application.dto.PsePaymentSession;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import reactor.core.publisher.Mono;

public interface PaymentGatewayPort {
	/**
	 * Inicia una sesión de pago PSE con PayU.
	 * Aplica circuit breaker, timeout 3s y retry (x1, backoff 500ms).
	 */
	Mono<PsePaymentSession> initiatePayment(AccountId accountId, Balance amount);
}
