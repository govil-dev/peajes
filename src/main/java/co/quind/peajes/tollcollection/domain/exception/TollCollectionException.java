package co.quind.peajes.tollcollection.domain.exception;

public class TollCollectionException extends RuntimeException {

	public TollCollectionException(String message) {
		super(message);
	}

	public TollCollectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
