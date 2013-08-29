package com.byteflair.parkplan.api.exception;

public class ForbiddenFenceException extends Exception {
	private static final long serialVersionUID = 2449302138706443838L;

	private String message;
	private Throwable cause;
	
	public ForbiddenFenceException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public ForbiddenFenceException(String message, Throwable cause) {
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