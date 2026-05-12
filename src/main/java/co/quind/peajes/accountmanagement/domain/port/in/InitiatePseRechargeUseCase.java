package co.quind.peajes.accountmanagement.domain.port.in;

import co.quind.peajes.accountmanagement.application.command.InitiatePseRechargeCommand;
import co.quind.peajes.accountmanagement.application.dto.RechargeInitiatedResponse;
import reactor.core.publisher.Mono;

public interface InitiatePseRechargeUseCase {
	Mono<RechargeInitiatedResponse> initiate(InitiatePseRechargeCommand command);
}
