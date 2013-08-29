package com.byteflair.parkplan.api.service;

import java.io.IOException;

import com.byteflair.parkplan.api.exception.TopoosException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



public interface ISecurityService {
	public void login(String code) throws TopoosException, JsonParseException, JsonMappingException, IOException;
}