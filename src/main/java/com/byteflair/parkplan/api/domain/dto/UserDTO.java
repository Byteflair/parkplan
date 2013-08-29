package com.byteflair.parkplan.api.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.byteflair.parkplan.api.domain.User;

public class UserDTO {
	private Long id;
	private String username;

	public UserDTO() {
	}
	
	public UserDTO(Long id) {
		this.id = id;
	}
	
	public UserDTO(String username) {
		this.username = username;
	}
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		
		UserDTO rhs = (UserDTO) obj;
		
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}
}