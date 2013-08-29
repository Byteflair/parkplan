package com.byteflair.parkplan.api.exception;

public class IncorrectFenceStatusException extends Exception {
	private static final long serialVersionUID = 2391590125732593547L;

	private String message;
	private Throwable cause;
	
	public IncorrectFenceStatusException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public IncorrectFenceStatusException(String message, Throwable cause) {
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