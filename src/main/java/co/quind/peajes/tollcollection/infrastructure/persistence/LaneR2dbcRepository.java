package co.quind.peajes.tollcollection.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LaneR2dbcRepository extends R2dbcRepository<LaneEntity, UUID> {
	Mono<LaneEntity> findById(UUID id);
}
