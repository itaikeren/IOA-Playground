package playground.exception;

public class NotAdminException extends Exception {

	private static final long serialVersionUID = -4952011972523634721L;

	public NotAdminException() {
		super();
	}

	public NotAdminException(String message) {
		super(message);
	}

	public NotAdminException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAdminException(Throwable cause) {
		super(cause);
	}
}
