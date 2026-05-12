package co.quind.peajes.accountmanagement.domain.exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(String accountId) {
		super("Prepaid account not found: " + accountId);
	}
}
