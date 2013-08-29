package com.byteflair.parkplan.api.domain.dto;

import com.byteflair.parkplan.api.domain.Pairing;


public class PairingDTO {
	private Long id;
	private Long controller;
	private Long controlled;
	private String status;

	public PairingDTO() {
	}
	
	public PairingDTO(Long id, String status) {
		this.id = id;
		this.status = status;
	}
	
	public PairingDTO(Long controller, Long controlled) {
		this.controller = controller;
		this.controlled = controlled;
	}
	
	public PairingDTO(Long controller, Long controlled, String status) {
		this.controller = controller;
		this.controlled = controlled;
		this.status = status;
	}
	
	public PairingDTO(Pairing pairing) {
		this.id = pairing.getId();
		this.controller = pairing.getController().getId();
		this.controlled = pairing.getControlled().getId();
		this.status = pairing.getStatus().toString();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getController() {
		return controller;
	}

	public void setController(Long controller) {
		this.controller = controller;
	}

	public Long getControlled() {
		return controlled;
	}

	public void setControlled(Long controlled) {
		this.controlled = controlled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}