package co.quind.peajes.accountmanagement.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceCacheEntry(BigDecimal balance, String currency, Instant lastUpdatedAt) {

    public static BalanceCacheEntry of(String balance, String currency, String lastUpdatedAt) {
        return new BalanceCacheEntry(
            new BigDecimal(balance),
            currency,
            Instant.parse(lastUpdatedAt)
        );
    }
}
