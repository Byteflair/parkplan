package com.byteflair.parkplan.api.domain.type;

public enum ScopeType {
	OFFLINE_ACCESS("offline_access"), EMAIL("email"), PROFILE("profile");
	
	private String value;
	
	private ScopeType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}