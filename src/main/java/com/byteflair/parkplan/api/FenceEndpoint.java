package com.byteflair.parkplan.api;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.byteflair.parkplan.api.domain.dto.FenceDTO;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.ForbiddenFenceException;
import com.byteflair.parkplan.api.exception.IncorrectFenceStatusException;
import com.byteflair.parkplan.api.exception.NonExistingFenceException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.service.IFenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class FenceEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(FenceEndpoint.class);
	
	@Autowired
	private IFenceService fenceService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "/users/{controllerId}/fences", method = RequestMethod.POST)
	public ResponseEntity<String> createFence(@PathVariable("controllerId") Long controllerId, @RequestBody FenceDTO fenceDto) throws JsonProcessingException, NonExistingPairingException, ForbiddenFenceException, TopoosException, IOException, NonExistingUserException {
		logger.info("POST - /users/" + controllerId + "/fences");
		
		fenceDto.setController(controllerId);
		
		FenceDTO createdFence = fenceService.createFence(fenceDto);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(createdFence), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/users/{userId}/fences", method = RequestMethod.GET)
	public ResponseEntity<String> findFences(@PathVariable("userId") Long userId, @RequestParam(value = "status", required = false) String status) throws IncorrectFenceStatusException, JsonProcessingException {
		logger.info("GET - /users/" + userId + "/fences");
		
		List<FenceDTO> foundFences = fenceService.findFences(userId, status);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(foundFences), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userId}/fences/{fenceId}", method = RequestMethod.GET)
	public ResponseEntity<String> getTrack(@PathVariable("userId") Long userId, @PathVariable("fenceId") Long fenceId) throws JsonProcessingException, NonExistingFenceException, NonExistingUserException, ForbiddenActionException, TopoosException, IOException {
		logger.info("GET - /users/" + userId + "/fences/" + fenceId);
		
		FenceDTO fenceDetail = fenceService.getFence(userId, fenceId);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(fenceDetail), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/{userId}/fences/{fenceId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateFence(@PathVariable("userId") Long userId, @PathVariable("fenceId") Long fenceId, @RequestBody FenceDTO fenceDto) throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException, JsonProcessingException {
		logger.info("PUT - /fences/" + fenceId);
		
		fenceDto.setId(fenceId);
		
		FenceDTO updatedFence = fenceService.updateFence(userId, fenceDto);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(updatedFence), HttpStatus.CREATED);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
		logger.error("exception: '" + e.getMessage() + "'", e);
		
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}