package com.byteflair.parkplan.api.exception;

public class DuplicatePairingException extends Exception {
	private static final long serialVersionUID = -5624491393391405641L;

	private String message;
	private Throwable cause;
	
	public DuplicatePairingException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public DuplicatePairingException(String message, Throwable cause) {
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