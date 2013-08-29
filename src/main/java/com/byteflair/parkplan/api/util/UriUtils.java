package com.byteflair.parkplan.api.util;

import java.math.BigDecimal;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.byteflair.parkplan.api.domain.type.PositionType;
import com.byteflair.parkplan.api.domain.type.ResponseType;
import com.byteflair.parkplan.api.domain.type.ScopeType;
import com.byteflair.parkplan.api.domain.type.TrackType;

@Component
public class UriUtils {
	@Value("${client.id}")
    private String clientId;
	@Value("${client.secret}")
    private String clientSecret;
	
	@Value("${redirectUri}")
	private String redirectUri;
	
	public URI getLoginUri() {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("login.topoos.com");
		uri.path("/oauth/authtoken");
		uri.queryParam("client_id", clientId);
		uri.queryParam("redirect_uri", redirectUri);
		uri.queryParam("response_type", ResponseType.CODE.getValue());
		uri.queryParam("scope", ScopeType.EMAIL.getValue());
		
		return uri.build().toUri();
	}
	
	public URI getAccessTokenUri() {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("login.topoos.com");
		uri.path("/oauth/accesstoken");
		
		return uri.build().toUri();
	}
	
	public MultiValueMap<String, String> getAccessTokenUriParams(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		
		params.add("grant_type", "authorization_code");
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("code", code);
		params.add("redirect_uri", redirectUri);
		
		return params;
	}
	
	public URI getUserMeUri(String accessToken) {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("api.topoos.com");
		uri.path("/1/users/me/show.json");
		
		uri.queryParam("oauth_token", accessToken);
		
		return uri.build().toUri();
	}
	
	public URI getTrackAddUri(String accessToken, String trackName) {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("api.topoos.com");
		uri.path("/1/tracks/add.json");
		
		uri.queryParam("oauth_token", accessToken);
		
		if (StringUtils.isNotBlank(trackName)) {
			if (trackName.length() > 100) {
				uri.queryParam("name", trackName.substring(0, 99));
			} else {
				uri.queryParam("name", trackName);
			}
		}
		
		return uri.build().toUri();
	}
	
	public URI getRuleAddUri(String accessToken, Long trackId, BigDecimal lat, BigDecimal lng, BigDecimal radius) {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("api.topoos.com");
		uri.path("/1/rules/add.json");
		
		uri.queryParam("oauth_token", accessToken);
		uri.queryParam("track", trackId);
		uri.queryParam("type", TrackType.TRACK_OUT_OF_BOUNDS.toString());
		uri.queryParam("lat", lat);
		uri.queryParam("lng", lng);
		uri.queryParam("radius", radius);
		
		return uri.build().toUri();
	}
	
	public URI getPositionAddUri(String accessToken, Long trackId, BigDecimal lat, BigDecimal lng, PositionType positionType) {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("api.topoos.com");
		uri.path("/1/positions/add.json");
		
		uri.queryParam("oauth_token", accessToken);
		uri.queryParam("track", trackId);
		uri.queryParam("lat", lat);
		uri.queryParam("lng", lng);
		uri.queryParam("postype", positionType.toString());
		
		return uri.build().toUri();
	}
	
	public URI getTrackDetailUri(String accessToken, Long trackId) {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		
		uri.scheme("https");
		uri.host("api.topoos.com");
		uri.path("/1/tracks/get.json");
		
		uri.queryParam("oauth_token", accessToken);
		uri.queryParam("track", trackId);
		
		return uri.build().toUri();
	}
}
