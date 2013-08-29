package com.byteflair.parkplan.api.domain.type;

public enum PositionType {
	TRACK_INIT(1, false), TRACK_END(1, true), POS(3, true), GSM_OK(4, false), GSM_NO(5, false), ALARM_INIT(6, true), ALARM_END(7, true), GPS_OK(8, true), GPS_NO(9, true), TRACK_END_PASSIVE(10, false);
	
	private Integer code;
	private Boolean real;
	
	private PositionType(Integer code, Boolean real) {
		this.code = code;
		this.real = real;
	}
	
	public Integer getCode() {
		return this.code;
	}
	
	public Boolean isReal() {
		return this.real;
	}
}