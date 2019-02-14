package playground.exception;

public class inValidCodeException extends RuntimeException{
	private static final long serialVersionUID = 954417857288942747L;
	
	public inValidCodeException() {
	}

	public inValidCodeException(String message) {
		super(message);
	}

	public inValidCodeException(Throwable cause) {
		super(cause);
	}

	public inValidCodeException(String message, Throwable cause) {
		super(message, cause);
	}

}
