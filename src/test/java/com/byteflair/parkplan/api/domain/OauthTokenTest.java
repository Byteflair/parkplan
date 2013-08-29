package com.byteflair.parkplan.api.domain;

import org.junit.Test;
import org.springframework.util.Assert;

public class OauthTokenTest {
	
	@Test
	public void equalsTokens() {
		OauthToken token1 = new OauthToken();
		token1.setAccessToken("1");
		token1.setRefreshToken("1");
		token1.setTokenType("bearer");
		token1.setExpiresIn(666L);
		
		OauthToken token2 = new OauthToken();
		token2.setAccessToken("1");
		token2.setRefreshToken("1");
		token2.setTokenType("bearer");
		token2.setExpiresIn(666L);
		
		Assert.isTrue(token1.equals(token2));
	}
	
	@Test
	public void differentAccessToken() {
		OauthToken token1 = new OauthToken();
		token1.setAccessToken("1");
		token1.setRefreshToken("1");
		token1.setTokenType("bearer");
		token1.setExpiresIn(666L);
		
		OauthToken token2 = new OauthToken();
		token2.setAccessToken("2");
		token2.setRefreshToken("1");
		token2.setTokenType("bearer");
		token2.setExpiresIn(666L);
		
		Assert.isTrue(!token1.equals(token2));
	}
	
	@Test
	public void differentRefreshToken() {
		OauthToken token1 = new OauthToken();
		token1.setAccessToken("1");
		token1.setRefreshToken("1");
		token1.setTokenType("bearer");
		token1.setExpiresIn(666L);
		
		OauthToken token2 = new OauthToken();
		token2.setAccessToken("1");
		token2.setRefreshToken("2");
		token2.setTokenType("bearer");
		token2.setExpiresIn(666L);
		
		Assert.isTrue(!token1.equals(token2));
	}
	
	@Test
	public void differentTokenType() {
		OauthToken token1 = new OauthToken();
		token1.setAccessToken("1");
		token1.setRefreshToken("1");
		token1.setTokenType("bearer");
		token1.setExpiresIn(666L);
		
		OauthToken token2 = new OauthToken();
		token2.setAccessToken("1");
		token2.setRefreshToken("1");
		token2.setTokenType("foobar");
		token2.setExpiresIn(666L);
		
		Assert.isTrue(!token1.equals(token2));
	}
	
	@Test
	public void differentExpiration() {
		OauthToken token1 = new OauthToken();
		token1.setAccessToken("1");
		token1.setRefreshToken("1");
		token1.setTokenType("bearer");
		token1.setExpiresIn(666L);
		
		OauthToken token2 = new OauthToken();
		token2.setAccessToken("1");
		token2.setRefreshToken("1");
		token2.setTokenType("bearer");
		token2.setExpiresIn(999L);
		
		Assert.isTrue(!token1.equals(token2));
	}
	
	@Test
	public void allDifferent() {
		OauthToken token1 = new OauthToken();
		token1.setAccessToken("1");
		token1.setRefreshToken("1");
		token1.setTokenType("bearer");
		token1.setExpiresIn(666L);
		
		OauthToken token2 = new OauthToken();
		token2.setAccessToken("2");
		token2.setRefreshToken("2");
		token2.setTokenType("foobar");
		token2.setExpiresIn(999L);
		
		Assert.isTrue(!token1.equals(token2));
	}
}