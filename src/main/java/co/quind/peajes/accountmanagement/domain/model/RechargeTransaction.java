package co.quind.peajes.accountmanagement.domain.model;

import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId;

import java.time.Instant;
import java.util.UUID;

public final class RechargeTransaction {

	private final UUID id;
	private final AccountId accountId;
	private final ExternalReferenceId externalReferenceId;
	private final Balance amount;
	private final RechargeStatus status;
	private final String failureReason;
	private final Instant createdAt;

	private RechargeTransaction(UUID id, AccountId accountId, ExternalReferenceId externalReferenceId,
								Balance amount, RechargeStatus status, String failureReason, Instant createdAt) {
		this.id = id;
		this.accountId = accountId;
		this.externalReferenceId = externalReferenceId;
		this.amount = amount;
		this.status = status;
		this.failureReason = failureReason;
		this.createdAt = createdAt;
	}

	public static RechargeTransaction completed(AccountId accountId, ExternalReferenceId externalReferenceId,
												Balance amount) {
		return new RechargeTransaction(UUID.randomUUID(), accountId, externalReferenceId,
			amount, RechargeStatus.COMPLETED, null, Instant.now());
	}

	public static RechargeTransaction failed(AccountId accountId, ExternalReferenceId externalReferenceId,
											 Balance amount, String failureReason) {
		return new RechargeTransaction(UUID.randomUUID(), accountId, externalReferenceId,
			amount, RechargeStatus.FAILED, failureReason, Instant.now());
	}

	/** Reconstruye desde persistencia — no genera eventos. */
	public static RechargeTransaction fromPersisted(UUID id, AccountId accountId,
													ExternalReferenceId externalReferenceId,
													Balance amount, RechargeStatus status,
													String failureReason, Instant createdAt) {
		return new RechargeTransaction(id, accountId, externalReferenceId, amount, status, failureReason, createdAt);
	}

	public UUID id() { return id; }
	public AccountId accountId() { return accountId; }
	public ExternalReferenceId externalReferenceId() { return externalReferenceId; }
	public Balance amount() { return amount; }
	public RechargeStatus status() { return status; }
	public String failureReason() { return failureReason; }
	public Instant createdAt() { return createdAt; }
}
