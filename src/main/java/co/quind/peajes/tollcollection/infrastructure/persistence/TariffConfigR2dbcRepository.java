package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TariffConfigR2dbcRepository extends ReactiveCrudRepository<TariffConfigEntity, String> {
    Mono<TariffConfigEntity> findByStationIdAndVehicleClassAndActiveTrue(String stationId, String vehicleClass);
}
