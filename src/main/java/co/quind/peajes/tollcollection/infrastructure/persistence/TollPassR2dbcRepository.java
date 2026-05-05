package co.quind.peajes.tollcollection.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TollPassR2dbcRepository extends R2dbcRepository<TollPassEntity, UUID> {
	Mono<TollPassEntity> findByPassId(String passId);
}
