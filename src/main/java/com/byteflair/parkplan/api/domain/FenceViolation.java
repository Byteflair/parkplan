package com.byteflair.parkplan.api.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fence_violation")
@IdClass(FenceViolationId.class)
public class FenceViolation {

	@Id
	@ManyToOne
	@JoinColumn(name = "fence_id")
	private Fence fence;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Id
	@Column(updatable = false)
	private Date date;

	@Column(precision = 8, scale = 6)
	private BigDecimal lat;
	@Column(precision = 8, scale = 6)
	private BigDecimal lng;
	
	@Column(name = "topoos_position_id", unique = true)
	private Long topoosPositionId;

	public Fence getFence() {
		return fence;
	}

	public void setFence(Fence fence) {
		this.fence = fence;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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

	public Long getTopoosPositionId() {
		return topoosPositionId;
	}

	public void setTopoosPositionId(Long topoosPositionId) {
		this.topoosPositionId = topoosPositionId;
	}
}