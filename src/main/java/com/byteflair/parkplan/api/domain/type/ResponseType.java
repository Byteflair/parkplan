package com.byteflair.parkplan.api.domain.type;

public enum ResponseType {
	CODE("code"), TOKEN("token");
	
	private String value;
	
	private ResponseType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}