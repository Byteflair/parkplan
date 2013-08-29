package com.byteflair.parkplan.api.exception;

public class IncorrectNotificationTypeException extends Exception {
	private static final long serialVersionUID = -8324579477405703527L;
	
	private String message;
	private Throwable cause;
	
	public IncorrectNotificationTypeException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public IncorrectNotificationTypeException(String message, Throwable cause) {
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