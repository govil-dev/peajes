package co.quind.peajes.tollcollection.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record TcTransactionEntry(
    String transactionId,
    String accountId,
    String stationName,
    BigDecimal amount,
    String currency,
    String vehicleClass,
    Instant authorizedAt
) {}
