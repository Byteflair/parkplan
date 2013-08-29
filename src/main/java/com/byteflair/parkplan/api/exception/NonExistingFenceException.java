package com.byteflair.parkplan.api.exception;

public class NonExistingFenceException extends Exception {
	private static final long serialVersionUID = 3695090287733758750L;

	private String message;
	private Throwable cause;
	
	public NonExistingFenceException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public NonExistingFenceException(String message, Throwable cause) {
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