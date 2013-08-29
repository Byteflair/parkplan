package com.byteflair.parkplan.api.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.byteflair.parkplan.api.domain.dto.FenceDTO;
import com.byteflair.parkplan.api.domain.dto.PositionDTO;
import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.ForbiddenFenceException;
import com.byteflair.parkplan.api.exception.IncorrectFenceStatusException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingFenceException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.exception.UntrackedUserException;
import com.byteflair.parkplan.api.service.IFenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.springtestdbunit.DbUnitRule;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:parkplan-api.xml")
@DatabaseSetup(value = "fenceServiceTest.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "fenceServiceTest.xml", type = DatabaseOperation.DELETE)
@Transactional
public class FenceServiceTest {
	@Rule
	public DbUnitRule dbUnit = new DbUnitRule();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private IFenceService fenceService;
	
	@Test(expected = NonExistingPairingException.class)
	public void createFence_ExpectedNonExistingPairingException() throws NonExistingPairingException, ForbiddenFenceException, TopoosException, IOException, NonExistingUserException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setName("Controlar a 'inventado' en Nuevos Ministerios");
		fenceDto.addControllee(1L);
		
		fenceService.createFence(fenceDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createFence_ExpectedIllegalArgumentException() throws NonExistingPairingException, ForbiddenFenceException, TopoosException, IOException, NonExistingUserException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setName("Controlar a 'holab' y 'dcastro' en Nuevos Ministerios");
		fenceDto.addControllee(1L);
		fenceDto.addControllee(2L);
		
		fenceService.createFence(fenceDto);
	}
	
	@Test(expected = ForbiddenFenceException.class)
	public void createFence_ExpectedForbiddenFenceException() throws NonExistingPairingException, ForbiddenFenceException, TopoosException, IOException, NonExistingUserException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setName("Controlar a 'dcastro' en Nuevos Ministerios");
		fenceDto.addControllee(1L);
		
		fenceService.createFence(fenceDto);
	}
	
	@Test
	public void createFence() throws NonExistingPairingException, ForbiddenFenceException, TopoosException, IOException, NonExistingUserException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setName("Controlar a 'holab' en Nuevos Ministerios");
		fenceDto.addControllee(1L);
		fenceDto.setLat(new BigDecimal(40.252525));
		fenceDto.setLng(new BigDecimal(-2.676767));
		fenceDto.setRadius(new BigDecimal(10));
		
		FenceDTO createdFenceDto = fenceService.createFence(fenceDto);
		
		Assert.assertNotNull(createdFenceDto);
		Assert.assertFalse(createdFenceDto.getControllees().isEmpty());
		
		Long pairingId = createdFenceDto.getControllees().iterator().next();
		
		Assert.assertTrue(createdFenceDto.getController().equals("dcastro"));
		Assert.assertTrue(pairingId.equals("holab"));
		Assert.assertNotNull(createdFenceDto.getId());
		Assert.assertTrue(createdFenceDto.getStatus().equals("CREATED"));
		Assert.assertNotNull(createdFenceDto.getLat());
		Assert.assertNotNull(createdFenceDto.getLng());
		Assert.assertNotNull(createdFenceDto.getRadius());
	}
	
	@Test(expected = NonExistingFenceException.class)
	public void updateFence_ExpectedNonExistingFenceException() throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setId(999999L);
		fenceDto.setStatus(FenceStatus.CREATED.toString());
		
		fenceService.updateFence(1L, fenceDto);
	}
	
	@Test(expected = IncorrectFenceStatusException.class)
	public void updateFence_ExpectedIncorrectFenceStatusException() throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setId(999999L);
		fenceDto.setStatus("INVENTADO");
		
		fenceService.updateFence(1L, fenceDto);
	}

	@Test(expected = ForbiddenActionException.class)
	public void updateFence_ExpectedForbiddenActionException() throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setId(999999L);
		fenceDto.setStatus(FenceStatus.EXPIRED.toString());
		
		fenceService.updateFence(1L, fenceDto);
	}
	
	@Test
	public void updateFenceRunningToStopping() throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException {
		FenceDTO fenceDto = new FenceDTO();
		fenceDto.setId(999999L);
		fenceDto.setStatus(FenceStatus.STOPPING.toString());
		
		FenceDTO updatedFenceDto = fenceService.updateFence(1L, fenceDto);
		
		Assert.assertNotNull(updatedFenceDto);
		Assert.assertTrue(updatedFenceDto.getStatus().equalsIgnoreCase(FenceStatus.STOPPING.toString()));
	}
	
	@Test(expected = IncorrectPairingStatusException.class)
	public void createPosition_ExpectedIncorrectPairingStatusException() throws IncorrectPairingStatusException, UntrackedUserException, TopoosException {
		PositionDTO positionDto = new PositionDTO();
		positionDto.setUser(1L);
		positionDto.setLat(new BigDecimal(40.252525));
		positionDto.setLng(new BigDecimal(-2.656565));
		
		fenceService.createPosition(positionDto);
	}
	
	@Test(expected = UntrackedUserException.class)
	public void createPosition_ExpectedUnfenceedUserException() throws IncorrectPairingStatusException, UntrackedUserException, TopoosException {
		PositionDTO positionDto = new PositionDTO();
		positionDto.setUser(2L);
		positionDto.setLat(new BigDecimal(40.252525));
		positionDto.setLng(new BigDecimal(-2.656565));
		
		fenceService.createPosition(positionDto);
	}
	
	@Test
	public void createPosition() throws IncorrectPairingStatusException, UntrackedUserException, TopoosException, NonExistingPairingException, ForbiddenFenceException, NonExistingFenceException, IncorrectFenceStatusException, ForbiddenActionException, IOException, NonExistingUserException {
		// create fence
		FenceDTO newFenceDto = new FenceDTO();
		newFenceDto.setName("Controlar a 'holab' en Nuevos Ministerios");
		newFenceDto.addControllee(1L);
		newFenceDto.setLat(new BigDecimal(40.252525));
		newFenceDto.setLng(new BigDecimal(-2.676767));
		newFenceDto.setRadius(new BigDecimal(10));
		
		fenceService.createFence(newFenceDto);
		
		// create position
		PositionDTO positionDto = new PositionDTO();
		positionDto.setUser(2L);
		positionDto.setLat(new BigDecimal(40.252525));
		positionDto.setLng(new BigDecimal(-2.656565));
		
		fenceService.createPosition(positionDto);
	}
	
	@Test
	public void findFences() throws IncorrectFenceStatusException {
		List<FenceDTO> foundFences = fenceService.findFences(1L, null);
		
		Assert.assertFalse(foundFences.isEmpty());
		Assert.assertTrue(foundFences.size() == 1);
	}
	
	@Test
	public void findCreatedFences() throws IncorrectFenceStatusException {
		List<FenceDTO> foundFences = fenceService.findFences(1L, FenceStatus.CREATED.toString());
		
		Assert.assertNotNull(foundFences);
		Assert.assertTrue(foundFences.isEmpty());
	}
	
	@Test
	public void getFence() throws JsonProcessingException, NonExistingFenceException, NonExistingUserException, ForbiddenActionException, TopoosException, IOException {
		FenceDTO fenceDto = fenceService.getFence(17L, 1000003L);
		
		Assert.assertNotNull(fenceDto);
		Assert.assertNotNull(fenceDto.getDetail());
		Assert.assertNotNull(fenceDto.getDetail().getPositions());
		Assert.assertFalse(fenceDto.getDetail().getPositions().isEmpty());
	}
}