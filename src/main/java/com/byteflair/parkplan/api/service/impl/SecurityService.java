package com.byteflair.parkplan.api.service.impl;

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.byteflair.parkplan.api.domain.OauthToken;
import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.persistence.ITokenDAO;
import com.byteflair.parkplan.api.persistence.IUserDAO;
import com.byteflair.parkplan.api.service.ISecurityService;
import com.byteflair.parkplan.api.util.UriUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SecurityService implements ISecurityService {
	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
	
	@Autowired
	IUserDAO userDAO;
	@Autowired
	ITokenDAO tokenDAO;
	
	@Autowired
	private UriUtils uriUtils;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	@Transactional
	public void login(String code) throws TopoosException, JsonParseException, JsonMappingException, IOException {
		// get access token
		URI uriAccessToken = uriUtils.getAccessTokenUri();
		
		ResponseEntity<String> responseAccessToken = restTemplate.postForEntity(uriAccessToken, uriUtils.getAccessTokenUriParams(code), String.class);
		
		if (responseAccessToken.getStatusCode() != HttpStatus.OK) {
			logger.error("Access token could not be retreived from TopoOS. Received status code " + responseAccessToken.getStatusCode().toString());
			logger.error(responseAccessToken.getBody());
			
			throw new TopoosException("Access token could not be retreived from TopoOS. Received status code " + responseAccessToken.getStatusCode().toString());
		}
		
		OauthToken token = objectMapper.readValue(responseAccessToken.getBody(), OauthToken.class);
		
		// request user data
		ResponseEntity<String> responseUserMe = restTemplate.getForEntity(uriUtils.getUserMeUri(token.getAccessToken()), String.class);
		
		if (responseUserMe.getStatusCode() != HttpStatus.OK) {
			logger.error("User details could not be retreived from TopoOS. Received status code " + responseUserMe.getStatusCode().toString());
			logger.error(responseUserMe.getBody());
			
			throw new TopoosException("User details could not be retreived from TopoOS. Received status code " + responseUserMe.getStatusCode().toString());
		}
		
		User user = new User();
		
		JsonNode rootNode = objectMapper.readTree(responseUserMe.getBody());
		
		user.setTopoosId(rootNode.get("id").asText());
		user.setUsername(rootNode.get("name").asText());
		user.setEmail(rootNode.get("email").asText());
				
		User existingUser = userDAO.findByTopoosId(user.getTopoosId());
		
		if (existingUser == null) {
			existingUser = userDAO.save(user);
		}
		
		if (existingUser.getToken() == null) {
			token.setUser(existingUser);
			token.setUserId(existingUser.getId());
			
			user.setToken(token);
			
			userDAO.save(existingUser);
		} else if (!existingUser.getToken().equals(token)) {
			OauthToken existingToken = existingUser.getToken();
			
			existingToken.setAccessToken(token.getAccessToken());
			existingToken.setRefreshToken(token.getRefreshToken());
			existingToken.setTokenType(token.getTokenType());
			existingToken.setExpiresIn(token.getExpiresIn());
			
			tokenDAO.save(existingToken);
		}
	}
}