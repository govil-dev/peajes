package co.quind.peajes.tollcollection.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tc_tariff_configs")
public record TariffConfigEntity(
	@Id UUID id,
	@Column("station_id") UUID stationId,
	@Column("vehicle_class") String vehicleClass,
	@Column("amount") BigDecimal amount,
	@Column("currency") String currency,
	@Column("valid_from") LocalDate validFrom,
	@Column("valid_until") LocalDate validUntil,
	@Column("active") boolean active
) {}
