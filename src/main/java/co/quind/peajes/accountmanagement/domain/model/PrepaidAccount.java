package co.quind.peajes.accountmanagement.domain.model;

import co.quind.peajes.accountmanagement.domain.event.AccountRecharged;
import co.quind.peajes.accountmanagement.domain.exception.AccountFrozenException;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PrepaidAccount {

	private final UUID id;
	private final AccountId accountId;
	private Balance balance;
	private final AccountStatus status;
	private final List<Object> domainEvents = new ArrayList<>();

	private PrepaidAccount(UUID id, AccountId accountId, Balance balance, AccountStatus status) {
		this.id = id;
		this.accountId = accountId;
		this.balance = balance;
		this.status = status;
	}

	/** Reconstruye desde persistencia — no genera eventos de dominio. */
	public static PrepaidAccount fromPersisted(UUID id, AccountId accountId, Balance balance, AccountStatus status) {
		return new PrepaidAccount(id, accountId, balance, status);
	}

	/**
	 * Acredita el monto de una recarga PSE exitosa al saldo de la cuenta.
	 * Valida que la cuenta no esté FROZEN antes de procesar.
	 */
	public RechargeTransaction creditRecharge(ExternalReferenceId externalRef, Balance amount) {
		if (status == AccountStatus.FROZEN) {
			throw new AccountFrozenException(accountId.toString());
		}
		this.balance = this.balance.add(amount);
		RechargeTransaction transaction = RechargeTransaction.completed(accountId, externalRef, amount);
		raiseAccountRecharged(transaction);
		return transaction;
	}

	/** Registra una recarga fallida por rechazo del banco PSE. No modifica el saldo. */
	public RechargeTransaction registerFailedRecharge(ExternalReferenceId externalRef, Balance amount,
													  String failureReason) {
		return RechargeTransaction.failed(accountId, externalRef, amount, failureReason);
	}

	private void raiseAccountRecharged(RechargeTransaction transaction) {
		domainEvents.add(new AccountRecharged(
			UUID.randomUUID().toString(),
			accountId.toString(),
			transaction.id().toString(),
			transaction.amount().toPlainString(),
			transaction.amount().currency(),
			balance.toPlainString(),
			"PSE",
			transaction.externalReferenceId().value(),
			Instant.now().toString()
		));
	}

	public List<Object> pullDomainEvents() {
		List<Object> events = new ArrayList<>(domainEvents);
		domainEvents.clear();
		return events;
	}

	public UUID id() { return id; }
	public AccountId accountId() { return accountId; }
	public Balance balance() { return balance; }
	public AccountStatus status() { return status; }
}
