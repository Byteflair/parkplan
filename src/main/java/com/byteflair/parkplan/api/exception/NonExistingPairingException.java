package com.byteflair.parkplan.api.exception;

public class NonExistingPairingException extends Exception {
	private static final long serialVersionUID = -5624491393391405641L;

	private String message;
	private Throwable cause;
	
	public NonExistingPairingException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public NonExistingPairingException(String message, Throwable cause) {
		super(message, cause);
		
		this.message = message;
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
}