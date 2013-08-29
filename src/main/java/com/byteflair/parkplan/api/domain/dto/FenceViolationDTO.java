package com.byteflair.parkplan.api.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.byteflair.parkplan.api.domain.Fence;
import com.byteflair.parkplan.api.domain.User;

public class FenceViolationDTO {
	private Long fence;
	private Long user;
	private Date date;
	private BigDecimal lat;
	private BigDecimal lng;

	public FenceViolationDTO() {
	}
	
	public FenceViolationDTO(Fence fence, User user, Date date, BigDecimal lat, BigDecimal lng) {
		this.fence = fence.getId();
		this.user = user.getId();
		this.date = date;
		this.lat = lat;
		this.lng = lng;
	}
	
	public Long getFence() {
		return fence;
	}

	public void setFence(Long fence) {
		this.fence = fence;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
}