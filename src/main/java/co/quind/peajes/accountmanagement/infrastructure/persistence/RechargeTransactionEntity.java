package co.quind.peajes.accountmanagement.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table("recharge_transactions")
public record RechargeTransactionEntity(
		@Id UUID id,
		@Column("account_id") UUID accountId,
		@Column("external_reference_id") String externalReferenceId,
		@Column("amount") BigDecimal amount,
		@Column("currency") String currency,
		@Column("status") String status,
		@Column("failure_reason") String failureReason,
		@Column("created_at") Instant createdAt
) {}
