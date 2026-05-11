package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ManualOverrideR2dbcRepository extends R2dbcRepository<ManualOverrideEntity, UUID> {
	@Query("SELECT * FROM manual_overrides WHERE override_id = :overrideId")
	Mono<ManualOverrideEntity> findByOverrideId(String overrideId);
}
