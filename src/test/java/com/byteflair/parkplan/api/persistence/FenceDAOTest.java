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
import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.github.springtestdbunit.DbUnitRule;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:parkplan-api.xml")
@DatabaseSetup(value = "fenceDAOTest.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "fenceDAOTest.xml", type = DatabaseOperation.DELETE)
@Transactional
public class FenceDAOTest {
	@Rule
	public DbUnitRule dbUnit = new DbUnitRule();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private IFenceDAO fenceDao;
	@Autowired
	private IPairingDAO pairingDao;
	
	@Test
	public void getTrackAndTrackRuleAndPairing() {
		Fence fence1 = fenceDao.findOne(1L);
		
		Assert.notNull(fence1);
		Assert.notNull(fence1.getPairings());
	}
	
	@Test
	public void saveFence() {
		Fence newFence = new Fence();
		newFence.setId(999999L);
		newFence.setName("demo-fence999999");
		newFence.setStatus(FenceStatus.CREATED);
		newFence.setLat(new BigDecimal(40.252525));
		newFence.setLng(new BigDecimal(-2.676767));
		newFence.setRadius(new BigDecimal(10));
		
		Pairing pairing = pairingDao.findOne(1L);
		
		newFence.addPairing(pairing, 9L);
		
		fenceDao.save(newFence);
		
		Fence fence = fenceDao.findOne(999999L);
		
		Assert.notNull(fence);
		Assert.notEmpty(fence.getPairings());
		
		Pairing p = fence.getPairings().iterator().next().getPairing();
		
		Assert.notNull(p.getController());
		Assert.notNull(p.getControlled());
		Assert.isTrue(p.getController().getUsername().equalsIgnoreCase("demo1"));
		Assert.isTrue(p.getControlled().getUsername().equalsIgnoreCase("demo2"));
	}
	
	@Test
	public void stopTrack() {
		Fence fence2 = fenceDao.findOne(2L);
		
		fence2.setStatus(FenceStatus.STOPPING);
		
		fenceDao.save(fence2);
		
		Fence stoppedTrack = fenceDao.findOne(2L);
		
		Assert.notNull(stoppedTrack);
		Assert.isTrue(stoppedTrack.getStatus().equals(FenceStatus.STOPPING));
	}
}
