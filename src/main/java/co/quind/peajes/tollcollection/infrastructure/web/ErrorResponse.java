package co.quind.peajes.tollcollection.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
	String timestamp,
	String code,
	String message,
	String correlationId
) {

	public static ErrorResponse of(String code, String message, String correlationId) {
		return new ErrorResponse(Instant.now().toString(), code, message, correlationId);
	}

}
