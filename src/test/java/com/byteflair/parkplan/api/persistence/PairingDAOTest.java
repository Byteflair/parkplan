package com.byteflair.parkplan.api.persistence;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.byteflair.parkplan.api.domain.Fence;
import com.byteflair.parkplan.api.domain.Pairing;
import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.github.springtestdbunit.DbUnitRule;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:parkplan-api.xml")
@DatabaseSetup(value = "pairingDAOTest.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "pairingDAOTest.xml", type = DatabaseOperation.DELETE)
@Transactional
public class PairingDAOTest {
	@Rule
	public DbUnitRule dbUnit = new DbUnitRule();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private IPairingDAO pairingDao;
	@Autowired
	private IUserDAO userDao;
	
	@Test
	public void getPairingAndFenceList() {
		Pairing pairing1 = pairingDao.findOne(1L);
		
		Assert.notNull(pairing1);
		Assert.notEmpty(pairing1.getFences());
	}
	
	@Test
	public void getPairing_Fences() {
		User controller = userDao.findOne(17L);
		User controlled = userDao.findOne(18L);
		
		Pairing existingPairing = pairingDao.findByControllerAndControlled(controller, controlled);
		
		Assert.notNull(existingPairing);
		Assert.notEmpty(existingPairing.getFences());
	}
	
	@Test
	public void addFenceToPairing() {
		Pairing pairing1 = pairingDao.findOne(1L);
		
		int beforeSize = pairing1.getFences().size();
		
		Fence newFence = new Fence();
		newFence.setId(9L);
		newFence.setName("demo-track9");
		newFence.setStatus(FenceStatus.CREATED);
		newFence.setLat(new BigDecimal(40.252525));
		newFence.setLng(new BigDecimal(-2.676767));
		newFence.setRadius(new BigDecimal(10));
		
		pairing1.addFence(newFence, 9L);
		
		pairingDao.save(pairing1);
		
		Pairing foundPairing = pairingDao.findOne(1L);
		
		Assert.notNull(pairing1);
		Assert.notEmpty(pairing1.getFences());
		Assert.isTrue(foundPairing.getFences().size() == beforeSize + 1);
	}
}