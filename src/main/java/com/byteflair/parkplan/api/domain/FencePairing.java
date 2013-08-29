package com.byteflair.parkplan.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fence_pairing")
@IdClass(FencePairingId.class)
public class FencePairing {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "fence_id")
	private Fence fence;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "pairing_id")
	private Pairing pairing;
	
	@Id
	@Column(name = "topoos_track_id", updatable = false)
	private Long topoosTrackId;

	public void setTopoosTrackId(Long topoosTrackId) {
		this.topoosTrackId = topoosTrackId;
	}

	public Fence getFence() {
		return fence;
	}

	public void setFence(Fence fence) {
		this.fence = fence;
	}

	public Pairing getPairing() {
		return pairing;
	}

	public void setPairing(Pairing pairing) {
		this.pairing = pairing;
	}

	public Long getTopoosTrackId() {
		return topoosTrackId;
	}
}