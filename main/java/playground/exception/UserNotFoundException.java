package playground.exception;

public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = -4402147946901277231L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

}
