package com.byteflair.parkplan.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "oauth_token")
@JsonInclude(Include.NON_NULL)
public class OauthToken {
	@Id
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "access_token")
	private String accessToken;
	@Column(name = "refresh_token")
	private String refreshToken;
	@Column(name = "token_type")
	private String tokenType;
	@Column(name = "expires_in")
	private Long expiresIn;

	@MapsId
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	private User user;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	@JsonProperty("access_token")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	@JsonProperty("refresh_token")
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	@JsonProperty("token_type")
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	@JsonProperty("expires_in")
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("accessToken", accessToken).append("refreshToken", refreshToken).append("tokenType", tokenType).append("expiresIn", expiresIn).toString();
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
		
		OauthToken rhs = (OauthToken) obj;
		
		return new EqualsBuilder().append(accessToken, rhs.accessToken).append(refreshToken, rhs.refreshToken).append(tokenType, rhs.tokenType).append(expiresIn, rhs.expiresIn).isEquals();
	}
}