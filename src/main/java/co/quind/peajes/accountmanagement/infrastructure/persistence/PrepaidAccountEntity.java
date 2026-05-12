package co.quind.peajes.accountmanagement.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("prepaid_accounts")
public record PrepaidAccountEntity(
		@Id UUID id,
		@Column("account_id") UUID accountId,
		@Column("balance") BigDecimal balance,
		@Column("currency") String currency,
		@Column("status") String status
) {}
