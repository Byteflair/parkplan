package com.byteflair.parkplan.api.persistence;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.byteflair.parkplan.api.domain.FenceViolation;
import com.github.springtestdbunit.DbUnitRule;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:parkplan-api.xml")
@DatabaseSetup(value = "fenceViolationDAOTest.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "fenceViolationDAOTest.xml", type = DatabaseOperation.DELETE)
@Transactional
public class FenceViolationDAOTest {
	@Rule
	public DbUnitRule dbUnit = new DbUnitRule();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private IFenceViolationDAO fenceViolationDao;
	
	@Test
	public void getFenceViolationByTopoosPositionId() {
		FenceViolation fenceViolation = fenceViolationDao.findByTopoosPositionId(1L);
		
		Assert.assertNotNull(fenceViolation);
		Assert.assertEquals(new Long(1L), fenceViolation.getTopoosPositionId());
	}
}
