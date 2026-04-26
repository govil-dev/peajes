package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TollPassR2dbcRepository extends ReactiveCrudRepository<TollPassEntity, String> {
    Mono<TollPassEntity> findByPassId(String passId);
}
