package co.quind.peajes.accountmanagement.application.dto;

import java.math.BigDecimal;

public record BalanceResponse(String balance, String currency, String lastUpdatedAt) {}
