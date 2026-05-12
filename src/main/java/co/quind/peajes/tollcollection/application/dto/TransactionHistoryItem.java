package co.quind.peajes.tollcollection.application.dto;

import java.math.BigDecimal;

public record TransactionHistoryItem(
    String transactionId,
    String stationName,
    BigDecimal amount,
    String vehicleClass,
    String authorizedAt
) {}
