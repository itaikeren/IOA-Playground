package playground.exception;

public class UserNotConfirmException extends RuntimeException {
	private static final long serialVersionUID = -402044789717888327L;

	public UserNotConfirmException() {
	}

	public UserNotConfirmException(String message) {
		super(message);
	}

	public UserNotConfirmException(Throwable cause) {
		super(cause);
	}

	public UserNotConfirmException(String message, Throwable cause) {
		super(message, cause);
	}
}
