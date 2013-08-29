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
import org.springframework.web.bind.annotation.RequestParam;

import com.byteflair.parkplan.api.domain.dto.PairingDTO;
import com.byteflair.parkplan.api.exception.DuplicatePairingException;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.service.IPairingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "pairings")
public class PairingEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(PairingEndpoint.class);
	
	@Autowired
	private IPairingService pairingService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "/{pairingId}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("pairingId") Long pairingId, @RequestBody PairingDTO pairing) throws NonExistingUserException, DuplicatePairingException, NonExistingPairingException, ForbiddenActionException, IncorrectPairingStatusException, JsonProcessingException {
		logger.info("PUT - /pairings/" + pairingId);
		
		pairing.setId(pairingId);
		
		PairingDTO updatedPairing = pairingService.updatePairing(pairing);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(updatedPairing), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{pairingId}", method = RequestMethod.GET)
	public ResponseEntity<String> get(@PathVariable("pairingId") Long pairingId, @RequestParam(value = "controller", required = false) String controller, @RequestParam(value = "controlled", required = false) String controlled, @RequestParam(value = "status", required = false) String status) throws NonExistingPairingException, JsonProcessingException {
		logger.info("GET - /pairings/" + pairingId);
		
		PairingDTO foundPairing = pairingService.getPairing(pairingId);
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(foundPairing), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> find(@RequestParam(value = "controller", required = false) Long controller, @RequestParam(value = "controlled", required = false) Long controlled, @RequestParam(value = "status", required = false) String status) throws IncorrectPairingStatusException, JsonProcessingException {
		logger.info("GET - /pairings");
		
		List<PairingDTO> foundPairings = pairingService.findPairings(new PairingDTO(controller, controlled, status));
		
		return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(foundPairings), HttpStatus.OK);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
		logger.error("exception: '" + e.getMessage() + "'", e);
		
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}