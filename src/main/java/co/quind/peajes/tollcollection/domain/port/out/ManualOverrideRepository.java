package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.ManualOverride;
import co.quind.peajes.tollcollection.domain.valueobject.OverrideId;
import reactor.core.publisher.Mono;

public interface ManualOverrideRepository {
	Mono<ManualOverride> save(ManualOverride override);
	Mono<ManualOverride> findByOverrideId(OverrideId overrideId);
}
