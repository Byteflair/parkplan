package com.byteflair.parkplan.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.byteflair.parkplan.api.domain.dto.PairingDTO;
import com.byteflair.parkplan.api.domain.dto.UserDTO;
import com.byteflair.parkplan.api.exception.DuplicatePairingException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.service.IPairingService;
import com.byteflair.parkplan.api.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "users")
public class UserEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(UserEndpoint.class);
	
	@Autowired
	private IPairingService pairingService;
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "/{controllerId}/controllees", method = RequestMethod.POST)
	public ResponseEntity<String> createControlled(@PathVariable("controllerId") Long controllerId, @RequestBody UserDTO controlled) throws NonExistingUserException, DuplicatePairingException, JsonProcessingException {
		logger.info("POST - /users/" + controllerId + "/controllees");
		
		UserDTO user = pairingService.createControlled(controllerId, controlled);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(user), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{controlledId}/controllers", method = RequestMethod.POST)
	public ResponseEntity<String> createController(@PathVariable("controlledId") Long controlledId, @RequestBody UserDTO controller) throws NonExistingUserException, DuplicatePairingException, JsonProcessingException {
		logger.info("POST - /users/" + controlledId + "/controllers");
		
		UserDTO user = pairingService.createController(controlledId, controller);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(user), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{controllerId}/controllees/{controlledId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteControlled(@PathVariable("controllerId") Long controllerId, @PathVariable("controlledId") Long controlledId) throws NonExistingUserException, NonExistingPairingException, JsonProcessingException {
		logger.info("DELETE - /users/" + controllerId + "/controllees/" + controlledId);
		
		UserDTO controlled = pairingService.deleteControlled(new PairingDTO(controllerId, controlledId));
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(controlled), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{controlledId}/controllres/{controllerId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteController(@PathVariable("controllerId") Long controllerId, @PathVariable("controlledId") Long controlledId) throws NonExistingUserException, NonExistingPairingException, JsonProcessingException {
		logger.info("DELETE - /users/" + controlledId + "/controllers/" + controllerId);
		
		UserDTO controller = pairingService.deleteController(new PairingDTO(controllerId, controlledId));
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(controller), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{controllerId}/controllees/{controlledId}", method = RequestMethod.GET)
	public ResponseEntity<String> getControlled(@PathVariable("controllerId") Long controllerId, @PathVariable("controlledId") Long controlledId) throws NonExistingPairingException, NonExistingUserException, JsonProcessingException {
		logger.info("GET - /users/" + controllerId + "/controllees/" + controlledId);
		
		UserDTO user = pairingService.getControlled(new PairingDTO(controllerId, controlledId));
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(user), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{controlledId}/controllers/{controllerId}", method = RequestMethod.GET)
	public ResponseEntity<String> getController(@PathVariable("controllerId") Long controllerId, @PathVariable("controlledId") Long controlledId) throws NonExistingPairingException, NonExistingUserException, JsonProcessingException {
		logger.info("GET - /users/" + controlledId + "/controllers/" + controllerId);
		
		UserDTO user = pairingService.getController(new PairingDTO(controllerId, controlledId));
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(user), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<String> getUser(@PathVariable("userId") Long userId) throws NonExistingUserException, JsonProcessingException {
		logger.info("GET - /users/" + userId);
		
		UserDTO user = userService.get(userId);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(user), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{controllerId}/controllees", method = RequestMethod.GET)
	public ResponseEntity<String> getControllees(@PathVariable("controllerId") Long controllerId) throws NonExistingPairingException, NonExistingUserException, JsonProcessingException {
		logger.info("GET - /users/" + controllerId + "/controllees");
		
		List<UserDTO> users = pairingService.getControllees(controllerId);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(users), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{controlledId}/controllers", method = RequestMethod.GET)
	public ResponseEntity<String> getControllers(@PathVariable("controlledId") Long controlledId) throws NonExistingPairingException, NonExistingUserException, JsonProcessingException {
		logger.info("GET - /users/" + controlledId + "/controllers");
		
		List<UserDTO> users = pairingService.getControllers(controlledId);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(users), HttpStatus.OK);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
		logger.error("exception: '" + e.getMessage() + "'", e);
		
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}