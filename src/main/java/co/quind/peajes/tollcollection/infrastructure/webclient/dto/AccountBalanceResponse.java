package co.quind.peajes.tollcollection.infrastructure.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record AccountBalanceResponse(
	@JsonProperty("account_id") String accountId,
	@JsonProperty("balance") BigDecimal balance
) {}
