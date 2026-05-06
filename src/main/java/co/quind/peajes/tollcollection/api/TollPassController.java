package co.quind.peajes.tollcollection.api;

import co.quind.peajes.tollcollection.api.dto.TollPassRequest;
import co.quind.peajes.tollcollection.api.dto.TollPassResponse;
import co.quind.peajes.tollcollection.application.command.ProcessTollPassCommand;
import co.quind.peajes.tollcollection.application.usecase.ProcessTollPassUseCase;
import co.quind.peajes.tollcollection.domain.model.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import reactor.core.publisher.Mono;

/**
 * Endpoint REST para registro de pasos vehiculares desde antenas RFID.
 * POST /api/v1/toll-passes — procesa un paso y responde ≤ 150ms.
 */
@RestController
@RequestMapping("/api/v1/toll-passes")
public class TollPassController {

    private static final Logger log = LoggerFactory.getLogger(TollPassController.class);

    private final ProcessTollPassUseCase processUseCase;

    public TollPassController(ProcessTollPassUseCase processUseCase) {
        this.processUseCase = processUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<TollPassResponse>> processTollPass(
            @RequestBody TollPassRequest request) {
        log.info("Paso recibido desde antena stationId={} laneId={}", request.stationId(), request.laneId());
        var command = new ProcessTollPassCommand(
                request.tagId(), request.stationId(),
                request.laneId(), request.vehicleClass(),
                request.detectedAt()
        );
        return processUseCase.process(command)
                .map(result -> {
                    var response = TollPassResponse.from(result);
                    var status = result.status() == TransactionStatus.AUTHORIZED
                            ? HttpStatus.OK : HttpStatus.UNPROCESSABLE_ENTITY;
                    return ResponseEntity.status(status).body(response);
                });
    }
}
