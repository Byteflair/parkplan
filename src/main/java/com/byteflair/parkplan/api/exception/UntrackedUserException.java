package com.byteflair.parkplan.api.exception;

public class UntrackedUserException extends Exception {
	private static final long serialVersionUID = 4275056816356813258L;
	
	private String message;
	private Throwable cause;
	
	public UntrackedUserException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public UntrackedUserException(String message, Throwable cause) {
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