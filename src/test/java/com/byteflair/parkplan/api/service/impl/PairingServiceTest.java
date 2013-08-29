package com.byteflair.parkplan.api.service.impl;

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

import com.byteflair.parkplan.api.domain.dto.PairingDTO;
import com.byteflair.parkplan.api.domain.dto.UserDTO;
import com.byteflair.parkplan.api.domain.type.PairingStatus;
import com.byteflair.parkplan.api.exception.DuplicatePairingException;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.service.IPairingService;
import com.github.springtestdbunit.DbUnitRule;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:parkplan-api.xml")
@DatabaseSetup(value = "pairingServiceTest.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "pairingServiceTest.xml", type = DatabaseOperation.DELETE)
@Transactional
public class PairingServiceTest {
	@Rule
	public DbUnitRule dbUnit = new DbUnitRule();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private IPairingService pairingService;
	
	@Test
	public void createPairing_AddControlled() throws NonExistingUserException, DuplicatePairingException, IncorrectPairingStatusException {
		UserDTO userDto = pairingService.createControlled(2L, new UserDTO("demo1"));
		
		Assert.assertNotNull(userDto);
		Assert.assertTrue(userDto.getId().equals(1L));
		
		PairingDTO criteriaController = new PairingDTO();
		criteriaController.setController(2L);
		
		List<PairingDTO> foundPairingsCriteriaController = pairingService.findPairings(criteriaController);
		
		Assert.assertFalse(foundPairingsCriteriaController.isEmpty());
		Assert.assertTrue(foundPairingsCriteriaController.get(0).getController().equals(1L));
		Assert.assertTrue(foundPairingsCriteriaController.get(0).getControlled().equals(2L));
		Assert.assertTrue(foundPairingsCriteriaController.get(0).getStatus().equals(PairingStatus.PENDING.toString()));
		
		PairingDTO criteriaControlled = new PairingDTO();
		criteriaControlled.setControlled(1L);
		
		List<PairingDTO> foundPairingsCriteriaControlled = pairingService.findPairings(criteriaControlled);
		
		Assert.assertFalse(foundPairingsCriteriaControlled.isEmpty());
		Assert.assertTrue(foundPairingsCriteriaControlled.get(0).getController().equals(1L));
		Assert.assertTrue(foundPairingsCriteriaControlled.get(0).getControlled().equals(2L));
		Assert.assertTrue(foundPairingsCriteriaControlled.get(0).getStatus().equals(PairingStatus.PENDING.toString()));
	}
	
	@Test
	public void createPairing_AddController() throws NonExistingUserException, DuplicatePairingException, IncorrectPairingStatusException {
		UserDTO userDto = pairingService.createController(2L, new UserDTO("demo1"));
		
		Assert.assertNotNull(userDto);
		Assert.assertTrue(userDto.getId().equals(2L));
		
		PairingDTO criteriaController = new PairingDTO();
		criteriaController.setController(2L);
		
		List<PairingDTO> foundPairingsCriteriaController = pairingService.findPairings(criteriaController);
		
		Assert.assertFalse(foundPairingsCriteriaController.isEmpty());
		Assert.assertTrue(foundPairingsCriteriaController.get(0).getController().equals(1L));
		Assert.assertTrue(foundPairingsCriteriaController.get(0).getControlled().equals(2L));
		Assert.assertTrue(foundPairingsCriteriaController.get(0).getStatus().equals(PairingStatus.PENDING.toString()));
		
		PairingDTO criteriaControlled = new PairingDTO();
		criteriaControlled.setControlled(1L);
		
		List<PairingDTO> foundPairingsCriteriaControlled = pairingService.findPairings(criteriaControlled);
		
		Assert.assertFalse(foundPairingsCriteriaControlled.isEmpty());
		Assert.assertTrue(foundPairingsCriteriaControlled.get(0).getController().equals(1L));
		Assert.assertTrue(foundPairingsCriteriaControlled.get(0).getControlled().equals(2L));
		Assert.assertTrue(foundPairingsCriteriaControlled.get(0).getStatus().equals(PairingStatus.PENDING.toString()));
	}
	
	@Test
	public void deletePairing_Controller() throws NonExistingUserException, NonExistingPairingException, IncorrectPairingStatusException {
		PairingDTO pairingDto = new PairingDTO(1L, 2L);
		
		UserDTO userDto = pairingService.deleteController(pairingDto);
		
		Assert.assertNotNull(userDto);
		Assert.assertTrue(userDto.getId().equals(1L));
		
		PairingDTO criteriaController = new PairingDTO();
		criteriaController.setController(1L);
		
		List<PairingDTO> foundPairingsCriteriaController = pairingService.findPairings(pairingDto);
		
		Assert.assertTrue(foundPairingsCriteriaController.isEmpty());
		
		PairingDTO criteriaControlled = new PairingDTO();
		criteriaControlled.setControlled(2L);
		
		List<PairingDTO> foundPairingsCriteriaControlled = pairingService.findPairings(pairingDto);
		
		Assert.assertTrue(foundPairingsCriteriaControlled.isEmpty());
	}
	
	@Test
	public void deletePairing_Controlled() throws NonExistingUserException, NonExistingPairingException, IncorrectPairingStatusException {
		PairingDTO pairingDto = new PairingDTO(1L, 2L);
		
		UserDTO userDto = pairingService.deleteController(pairingDto);
		
		Assert.assertNotNull(userDto);
		Assert.assertTrue(userDto.getId().equals(2L));
		
		PairingDTO criteriaController = new PairingDTO();
		criteriaController.setController(1L);
		
		List<PairingDTO> foundPairingsCriteriaController = pairingService.findPairings(pairingDto);
		
		Assert.assertTrue(foundPairingsCriteriaController.isEmpty());
		
		PairingDTO criteriaControlled = new PairingDTO();
		criteriaControlled.setControlled(2L);
		
		List<PairingDTO> foundPairingsCriteriaControlled = pairingService.findPairings(pairingDto);
		
		Assert.assertTrue(foundPairingsCriteriaControlled.isEmpty());
	}
	
	@Test
	public void getPairing_Controller() throws NonExistingPairingException, NonExistingUserException {
		PairingDTO pairingDto = new PairingDTO(1L, 2L);
		
		UserDTO userDto = pairingService.getController(pairingDto);
		
		Assert.assertNotNull(userDto);
		Assert.assertTrue(userDto.getId().equals(1L));
	}
	
	@Test
	public void getPairing_Controlled() throws NonExistingPairingException, NonExistingUserException {
		PairingDTO pairingDto = new PairingDTO(1L, 2L);
		
		UserDTO userDto = pairingService.getController(pairingDto);
		
		Assert.assertNotNull(userDto);
		Assert.assertTrue(userDto.getId().equals(2L));
	}
	
	@Test
	public void getPairings_Controllers() throws NonExistingPairingException, NonExistingUserException {
		List<UserDTO> userDtoList = pairingService.getControllers(2L);
		
		Assert.assertFalse(userDtoList.isEmpty());
		Assert.assertTrue(userDtoList.size() == 2);
		Assert.assertTrue(userDtoList.contains(new UserDTO(1L)));
		Assert.assertTrue(userDtoList.contains(new UserDTO(3L)));
	}
	
	@Test
	public void getPairings_Controllees() throws NonExistingPairingException, NonExistingUserException {
		List<UserDTO> userDtoList = pairingService.getControllees(1L);
		
		Assert.assertFalse(userDtoList.isEmpty());
		Assert.assertTrue(userDtoList.size() == 1);
		Assert.assertTrue(userDtoList.contains(new UserDTO(2L)));
	}
	
	@Test
	public void updatePairing() throws IncorrectPairingStatusException, NonExistingUserException, DuplicatePairingException, NonExistingPairingException, ForbiddenActionException {
		PairingDTO criteriaController = new PairingDTO();
		criteriaController.setController(4L);
		
		List<PairingDTO> foundPairingsCriteriaController = pairingService.findPairings(criteriaController);
		
		Assert.assertFalse(foundPairingsCriteriaController.isEmpty());
		Assert.assertTrue(foundPairingsCriteriaController.size() == 1);
		
		PairingDTO pairingDto = new PairingDTO();
		pairingDto.setId(foundPairingsCriteriaController.get(0).getId());
		pairingDto.setStatus(PairingStatus.ACCEPTED.toString());
		
		PairingDTO updatedPairingDto = pairingService.updatePairing(pairingDto);
		
		Assert.assertTrue(updatedPairingDto.getStatus().equals(PairingStatus.ACCEPTED.toString()));
	}
}