package co.quind.peajes.accountmanagement.domain.port.in;

import co.quind.peajes.accountmanagement.application.command.ProcessPseRechargeCommand;
import co.quind.peajes.accountmanagement.application.dto.PseRechargeResponse;
import reactor.core.publisher.Mono;

public interface ProcessPseRechargeUseCase {
	Mono<PseRechargeResponse> process(ProcessPseRechargeCommand command);
}
