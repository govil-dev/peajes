package co.quind.peajes.accountmanagement.domain.exception;

public class AccountFrozenException extends RuntimeException {
	public AccountFrozenException(String accountId) {
		super("Account " + accountId + " is FROZEN and cannot process recharges");
	}
}
