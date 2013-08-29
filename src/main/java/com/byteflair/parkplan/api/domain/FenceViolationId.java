package com.byteflair.parkplan.api.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FenceViolationId implements Serializable {
	private static final long serialVersionUID = 3183279529889641630L;

	private Long fence;
	private Long user;
	private Date date;

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

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(fence).append(user).append(date).build();
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
		
		FenceViolationId rhs = (FenceViolationId) obj;
		
		return new EqualsBuilder().append(fence, rhs.fence).append(user, rhs.user).append(date, rhs.date).isEquals();
	}
}