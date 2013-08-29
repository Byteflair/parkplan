package com.byteflair.parkplan.api.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.byteflair.parkplan.api.domain.type.TrackType;

@Entity(name = "fence")
public class Fence {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private Date created;
	@Enumerated(EnumType.STRING)
	private FenceStatus status;
	
	@Column(name = "track_type")
	@Enumerated(EnumType.STRING)
	private TrackType trackType;
	@Column(precision = 8, scale = 6)
	private BigDecimal lat;
	@Column(precision = 8, scale = 6)
	private BigDecimal lng;
	@Column(precision = 5, scale = 2)
	private BigDecimal radius;

	@OneToMany(mappedBy = "fence", cascade = { CascadeType.ALL })
	private Set<FencePairing> pairings;
	
	@OneToMany(mappedBy = "fence", cascade = { CascadeType.ALL })
	private Set<FenceViolation> violations;

	public Fence() {
	}
	
	@PrePersist
	public void prePersist() {
		this.created = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<FencePairing> getPairings() {
		if (pairings == null) {
			this.pairings = new HashSet<FencePairing>();
		}
		
		return pairings;
	}

	public void setPairings(Set<FencePairing> pairings) {
		this.pairings = pairings;
	}
	
	public void addPairing(Pairing pairing, Long topoosTrackId) {
		if (this.pairings == null) {
			this.pairings = new HashSet<FencePairing>();
		}
		
		FencePairing trackPairing = new FencePairing();
		trackPairing.setFence(this);
		trackPairing.setPairing(pairing);
		trackPairing.setTopoosTrackId(topoosTrackId);
		
		pairings.add(trackPairing);
		
		pairing.getFences().add(trackPairing);
	}

	public User getController() {
		FencePairing trackPairing = pairings.iterator().next();
		
		return trackPairing.getPairing().getController();
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

	public FenceStatus getStatus() {
		return status;
	}

	public void setStatus(FenceStatus status) {
		this.status = status;
	}

	public TrackType getTrackType() {
		return trackType;
	}

	public void setTrackType(TrackType trackType) {
		this.trackType = trackType;
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

	public Set<FenceViolation> getViolations() {
		return violations;
	}

	public void setViolations(Set<FenceViolation> violations) {
		this.violations = violations;
	}
	
	public void addViolation(User user, Date date, BigDecimal lat, BigDecimal lng, Long topoosPositionId) {
		if (this.violations == null) {
			this.violations = new HashSet<FenceViolation>();
		}
		
		FenceViolation fenceViolation = new FenceViolation();
		fenceViolation.setFence(this);
		fenceViolation.setUser(user);
		fenceViolation.setDate(date);
		fenceViolation.setLat(lat);
		fenceViolation.setLng(lng);
		fenceViolation.setTopoosPositionId(topoosPositionId);
		
		violations.add(fenceViolation);
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
		
		Fence rhs = (Fence) obj;
		
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}
}