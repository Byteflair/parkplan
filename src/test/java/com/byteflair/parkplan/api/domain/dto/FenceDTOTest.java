package com.byteflair.parkplan.api.domain.dto;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FenceDTOTest {
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void trackDtoToJson() throws JsonProcessingException  {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setId(1L);
		fenceDto.setName("Controlar a A en lugar X");
		fenceDto.setController(1L);
		fenceDto.addControllee(1L);
		
		String json = objectMapper.writer().writeValueAsString(fenceDto);
		
		System.out.println(json);
		
		Assert.assertEquals(json, "{\"id\":1,\"name\":\"Controlar a A en lugar X\",\"controller\":1,\"pairings\":[1]}");
	}
	
	@Test
	public void jsonToTrackDto() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"id\":1,\"name\":\"Controlar a A en lugar X\",\"controller\":1,\"pairings\":[1]}";
		
		FenceDTO trackDto = objectMapper.readValue(json, FenceDTO.class);
		
		Assert.assertEquals(1L, trackDto.getId().longValue());
		Assert.assertEquals("Controlar a A en lugar X", trackDto.getName());
		Assert.assertEquals(new Long(1L), trackDto.getController());
		
		Assert.assertNotNull(trackDto.getControllees());
		
		Long pairingId = trackDto.getControllees().iterator().next();
		
		Assert.assertEquals(new Long(1L), pairingId);
	}
}