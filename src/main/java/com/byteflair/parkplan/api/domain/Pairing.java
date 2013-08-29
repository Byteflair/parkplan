package com.byteflair.parkplan.api.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.byteflair.parkplan.api.domain.type.PairingStatus;

@Entity(name="pairing")
public class Pairing {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne
	private User controller;
	@OneToOne
	private User controlled;
	@Enumerated(EnumType.STRING)
	private PairingStatus status;

	@OneToMany(mappedBy = "pairing")
	private Set<FencePairing> fences;
	
	public Pairing() {
	}
	
	public Pairing(User controller, User controlled, PairingStatus status) {
		this.controller = controller;
		this.controlled = controlled;
		this.status = status;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getController() {
		return controller;
	}

	public void setController(User controller) {
		this.controller = controller;
	}

	public User getControlled() {
		return controlled;
	}

	public void setControlled(User controlled) {
		this.controlled = controlled;
	}

	public PairingStatus getStatus() {
		return status;
	}

	public void setStatus(PairingStatus status) {
		this.status = status;
	}

	public Set<FencePairing> getFences() {
		if (fences == null) {
			this.fences = new HashSet<FencePairing>();
		}
		
		return fences;
	}

	public void setFences(Set<FencePairing> tracks) {
		this.fences = tracks;
	}
	
	public void addFence(Fence fence, Long topoosTrackId) {
		if (fences == null) {
			fences = new HashSet<FencePairing>();
		}
		
		FencePairing fencePairing = new FencePairing();
		fencePairing.setFence(fence);
		fencePairing.setPairing(this);
		fencePairing.setTopoosTrackId(topoosTrackId);
		
		fences.add(fencePairing);
		
		fence.getPairings().add(fencePairing);
	}
}
