package com.byteflair.parkplan.api.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FencePairingId implements Serializable {
	private static final long serialVersionUID = 3183279529889641630L;

	private Long fence;
	private Long pairing;
	private Long topoosTrackId;

	public Long getFence() {
		return fence;
	}

	public void setFence(Long track) {
		this.fence = track;
	}

	public Long getPairing() {
		return pairing;
	}

	public void setPairing(Long pairing) {
		this.pairing = pairing;
	}

	public Long getTopoosTrackId() {
		return topoosTrackId;
	}

	public void setTopoosTrackId(Long topoosTrackId) {
		this.topoosTrackId = topoosTrackId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(fence).append(pairing).append(topoosTrackId).build();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
	
		if (obj == this) {
			return true;
		}
		
		if (obj.getClass() != getClass()) {
			return false;
		}
		
		FencePairingId rhs = (FencePairingId) obj;
		
		return new EqualsBuilder().append(fence, rhs.fence).append(pairing, rhs.pairing).append(topoosTrackId, rhs.topoosTrackId).isEquals();
	}
}