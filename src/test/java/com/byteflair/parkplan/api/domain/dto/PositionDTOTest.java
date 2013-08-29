package com.byteflair.parkplan.api.domain.dto;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class PositionDTOTest {
	
	@Test
	public void positionDtoSetDate() {
		PositionDTO positionDto = new PositionDTO();
		positionDto.setDate("2013-08-06T18:06:43.913+00:00", "yyyy-MM-dd'T'HH:mm:ss.SSS");
		
		Calendar c = Calendar.getInstance();
		c.setTime(positionDto.getDate());
		
		Assert.assertTrue(c.get(Calendar.YEAR) == 2013);
		Assert.assertTrue(c.get(Calendar.MONTH) + 1 == 8);
		Assert.assertTrue(c.get(Calendar.DAY_OF_MONTH) == 6);
		Assert.assertTrue(c.get(Calendar.HOUR_OF_DAY) == 18);
		Assert.assertTrue(c.get(Calendar.MINUTE) == 6);
		Assert.assertTrue(c.get(Calendar.SECOND) == 43);
		Assert.assertTrue(c.get(Calendar.MILLISECOND) == 913);
	}
}