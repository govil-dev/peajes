package co.quind.peajes.tollcollection.infrastructure.webclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeductBalanceRequest(
	@JsonProperty("amount") String amount,
	@JsonProperty("currency") String currency,
	@JsonProperty("pass_id") String passId
) {}
