package com.byteflair.parkplan.api.domain.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.byteflair.parkplan.api.domain.Fence;
import com.byteflair.parkplan.api.domain.FencePairing;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FenceDTO {
	private Long id;
	private String name;
	private Date created;
	private Long controller;
	private Set<Long> controllees;
	private String status;
	private BigDecimal lat;
	private BigDecimal lng;
	private BigDecimal radius;
	private TrackDetailDTO detail;

	public FenceDTO() {
	}

	public FenceDTO(Long controller, String status) {
		this.controller = controller;
		this.status = status;
	}
	
	public FenceDTO(Fence fence) {
		this.id = fence.getId();
		this.name = fence.getName();
		this.created = fence.getCreated();
		
		this.controllees = new HashSet<Long>();
		
		for (FencePairing trackPairing : fence.getPairings()) {
			if (controller == null) {
				this.controller = trackPairing.getPairing().getController().getId();
			}
			
			this.controllees.add(trackPairing.getPairing().getControlled().getId());
		}
		
		this.status = fence.getStatus().toString();
		
		this.lat = fence.getLat();
		this.lng = fence.getLng();
		this.radius = fence.getRadius();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getController() {
		return controller;
	}

	public void setController(Long controller) {
		this.controller = controller;
	}

	public Set<Long> getControllees() {
		return controllees;
	}

	public void setControllees(Set<Long> controllees) {
		this.controllees = controllees;
	}
	
	public void addControllee(Long controlleeId) {
		if (this.controllees == null) {
			this.controllees = new HashSet<Long>();
		}
		
		this.controllees.add(controlleeId);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public BigDecimal getRadius() {
		return radius;
	}

	public void setRadius(BigDecimal radius) {
		this.radius = radius;
	}

	public TrackDetailDTO getDetail() {
		return detail;
	}

	public void setDetail(TrackDetailDTO detail) {
		this.detail = detail;
	}
}