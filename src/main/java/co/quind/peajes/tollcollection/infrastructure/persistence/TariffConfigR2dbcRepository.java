package co.quind.peajes.tollcollection.infrastructure.persistence;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TariffConfigR2dbcRepository extends R2dbcRepository<TariffConfigEntity, UUID> {
	@Query("""
		SELECT * FROM tc_tariff_configs
		WHERE station_id = :stationId
		  AND vehicle_class = :vehicleClass
		  AND active = true
		  AND valid_from <= :date
		  AND (valid_until IS NULL OR valid_until >= :date)
		ORDER BY valid_from DESC
		LIMIT 1
		""")
	Mono<TariffConfigEntity> findActiveByStationAndClass(UUID stationId, String vehicleClass, LocalDate date);
}
