package com.byteflair.parkplan.api.exception;

public class TopoosException extends Exception {
	private static final long serialVersionUID = -377927422051158503L;

	private String message;
	private Throwable cause;
	
	public TopoosException(String message) {
		super(message);
		
		this.message = message;
	}
	
	public TopoosException(String message, Throwable cause) {
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