package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.command.RegisterManualOverrideCommand;
import co.quind.peajes.tollcollection.application.dto.TollPassResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class RegisterManualOverrideUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterManualOverrideUseCase.class);

    public Mono<TollPassResult> execute(RegisterManualOverrideCommand command) {
        return Mono.defer(() -> {
            String overrideId = UUID.randomUUID().toString();
            MDC.put("overrideId", overrideId);
            MDC.put("stationId", command.stationId());
            MDC.put("laneId", command.laneId());
            try {
                log.info("Manual override registration requested reason={} operator={}",
                        command.reason(), command.operatorId());
                return Mono.error(new UnsupportedOperationException(
                        "RegisterManualOverrideUseCase pending wiring (HU-002)"));
            } finally {
                MDC.clear();
            }
        });
    }
}
