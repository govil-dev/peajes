package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.TollPass;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import reactor.core.publisher.Mono;

/** Puerto de salida para persistencia de pasos vehiculares. */
public interface TollPassRepository {
    Mono<TollPass> findByPassId(PassId passId);
    Mono<TollPass> save(TollPass tollPass);
}
