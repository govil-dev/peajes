package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.PrepaidAccountSnapshot;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;
import reactor.core.publisher.Mono;

public interface AccountManagementPort {
	Mono<PrepaidAccountSnapshot> getBalance(TagId tagId);
	Mono<PrepaidAccountSnapshot> deductBalance(TagId tagId, MoneyAmount amount, PassId passId);
}
