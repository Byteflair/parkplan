package com.byteflair.parkplan.api.domain.dto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PositionDTO {
	private Long user;
	private BigDecimal lat;
	private BigDecimal lng;
	private Date date;
	private Boolean violation;

	public PositionDTO() {
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Incorrect date format (" + date + "). Expected format was '" + pattern + "'");
		}
	}

	public Boolean getViolation() {
		return violation;
	}

	public void setViolation(Boolean violation) {
		this.violation = violation;
	}
}