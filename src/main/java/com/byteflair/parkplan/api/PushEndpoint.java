package com.byteflair.parkplan.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.byteflair.parkplan.api.domain.PushNotification;
import com.byteflair.parkplan.api.domain.dto.FenceViolationDTO;
import com.byteflair.parkplan.api.domain.type.NotificationType;
import com.byteflair.parkplan.api.exception.IncorrectNotificationTypeException;
import com.byteflair.parkplan.api.exception.NonExistingFenceException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.service.IFenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/push")
public class PushEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(PushEndpoint.class);
	
	@Autowired
	private IFenceService fenceService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@InitBinder
    public void bind(WebDataBinder dataBinder, @RequestParam(value="notification_type") String notificationType) throws IncorrectNotificationTypeException {
		PushNotification pushNotification = (PushNotification) dataBinder.getTarget();
		
		pushNotification.setNotificationType(notificationType);
    }
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<String> push(@ModelAttribute PushNotification pushNotification) throws JsonProcessingException, NonExistingUserException, NonExistingFenceException, IOException {
		if (pushNotification.getType() == NotificationType.OUT_OF_BOUNDS) {
			FenceViolationDTO fenceViolationDto = fenceService.registerFenceViolation(pushNotification);
			
			return new ResponseEntity<String>(objectMapper.writer().writeValueAsString(fenceViolationDto), HttpStatus.CREATED);
		} else {			
			return new ResponseEntity<String>(String.format("{\"ignored\":\"Only processing %s push notifications\"}", NotificationType.OUT_OF_BOUNDS.toString()), HttpStatus.OK);
		}
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> handleUnexpectedExceptions(Exception e) {
		logger.error("/push exception: '" + e.getMessage() + "'", e);
		
		return new ResponseEntity<String>(String.format("{\"reason\":\"%s\"}", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}