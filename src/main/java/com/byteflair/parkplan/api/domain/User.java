package com.byteflair.parkplan.api.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name="user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "topoos_id", unique = true)
	private String topoosId;
	@Column(unique = true)
	private String username;
	private String email;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private OauthToken token;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getTopoosId() {
		return topoosId;
	}

	public void setTopoosId(String topoosId) {
		this.topoosId = topoosId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public OauthToken getToken() {
		return token;
	}

	public void setToken(OauthToken token) {
		this.token = token;
	}
}