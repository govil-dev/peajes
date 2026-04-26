package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.application.dto.AccountDetails;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;
import reactor.core.publisher.Mono;

/**
 * Puerto de salida hacia account-management-api.
 * Timeout: 200ms. Circuit breaker: failureRateThreshold=50%.
 */
public interface AccountManagementPort {
    Mono<AccountDetails> getAccountByTag(TagId tagId);
    Mono<MoneyAmount> deductBalance(TagId tagId, MoneyAmount amount, PassId passId);
}
