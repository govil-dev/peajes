package co.quind.peajes.tollcollection.application.dto;

import co.quind.peajes.tollcollection.domain.model.TollPass;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TollPassResponse(
	String passId,
	String status,
	String declineReason,
	String tariffAmount,
	String currency,
	String authorizedAt,
	String declinedAt
) {

	public static TollPassResponse from(TollPass tollPass) {
		Instant now = Instant.now();
		return new TollPassResponse(
			tollPass.passId().value(),
			tollPass.status().name(),
			tollPass.declineReason() != null ? tollPass.declineReason().name() : null,
			tollPass.tariff() != null ? tollPass.tariff().toJsonString() : null,
			tollPass.tariff() != null ? tollPass.tariff().currency() : "COP",
			tollPass.status().name().equals("AUTHORIZED") ? now.toString() : null,
			tollPass.status().name().equals("DECLINED") ? now.toString() : null
		);
	}

	public static TollPassResponse fromExisting(TollPass tollPass) {
		return from(tollPass);
	}

}
