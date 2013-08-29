package com.byteflair.parkplan.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ping")
public class PingEndpoint {
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<String> pingBack() {
		return new ResponseEntity<String>("{\"message\":\"Running!\"}", HttpStatus.OK);
	}
}