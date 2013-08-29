package com.byteflair.parkplan.api;

import java.io.IOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.service.ISecurityService;
import com.byteflair.parkplan.api.util.UriUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class LoginEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(LoginEndpoint.class);
	
	@Autowired
	private UriUtils uriUtils;
	
	@Autowired
	private ISecurityService securityService;
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public View login() {
		logger.info("GET - /login");
		
		URI loginUri = uriUtils.getLoginUri();
		
		logger.info("redirecting to: " + loginUri.toString());
		
		return new RedirectView(loginUri.toString());
	}
	
	@RequestMapping(value = "login", method = RequestMethod.GET, params = { "code" })
	public ResponseEntity<String> loginOk(@RequestParam("code") String code) throws JsonParseException, JsonMappingException, TopoosException, IOException {
		logger.info("GET - /login?code=" + code);
		
		securityService.login(code);
		
		return new ResponseEntity<String>("{\"message\":\"successful log in\"}", HttpStatus.OK);
	}
	
	@RequestMapping(value = "login", method = RequestMethod.GET, params = { "error" })
	public ResponseEntity<String> loginError(@RequestParam("error") String error) {
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", error), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
		logger.error("exception: '" + e.getMessage() + "'", e);
		
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
