package playground.exception;

public class ActivityNotFoundException extends Exception {
	private static final long serialVersionUID = -4653694053960623197L;

	public ActivityNotFoundException() {
		super();
	}

	public ActivityNotFoundException(String message) {
		super(message);
	}

	public ActivityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActivityNotFoundException(Throwable cause) {
		super(cause);
	}
}
