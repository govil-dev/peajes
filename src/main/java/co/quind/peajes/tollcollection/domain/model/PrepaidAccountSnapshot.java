package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.AccountId;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;

public record PrepaidAccountSnapshot(AccountId accountId, MoneyAmount balance) {
}
