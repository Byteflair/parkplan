package com.byteflair.parkplan.api;

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

import com.byteflair.parkplan.api.domain.dto.PositionDTO;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.exception.UntrackedUserException;
import com.byteflair.parkplan.api.service.IFenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PositionEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(PositionEndpoint.class);
	
	@Autowired
	private IFenceService fenceService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "/users/{userId}/positions", method = RequestMethod.POST)
	public ResponseEntity<String> createTrack(@PathVariable("userId") Long userId, @RequestBody PositionDTO positionDto) throws IncorrectPairingStatusException, UntrackedUserException, TopoosException, JsonProcessingException {
		logger.info("POST - /positions");
		
		positionDto.setUser(userId);
		
		PositionDTO position = fenceService.createPosition(positionDto);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(position), HttpStatus.CREATED);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
		logger.error("exception: '" + e.getMessage() + "'", e);
		
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}