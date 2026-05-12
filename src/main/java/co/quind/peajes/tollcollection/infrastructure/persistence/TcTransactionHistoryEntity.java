package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table("tc_transaction_history_rm")
public record TcTransactionHistoryEntity(
    @Id UUID id,
    @Column("transaction_id") String transactionId,
    @Column("account_id") String accountId,
    @Column("station_name") String stationName,
    @Column("amount") BigDecimal amount,
    @Column("currency") String currency,
    @Column("vehicle_class") String vehicleClass,
    @Column("authorized_at") Instant authorizedAt
) {}
