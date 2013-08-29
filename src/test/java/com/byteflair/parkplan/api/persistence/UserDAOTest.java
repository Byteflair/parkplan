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

import com.byteflair.parkplan.api.domain.OauthToken;
import com.byteflair.parkplan.api.domain.User;
import com.github.springtestdbunit.DbUnitRule;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:parkplan-api.xml")
@DatabaseSetup(value = "userDAOTest.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "userDAOTest.xml", type = DatabaseOperation.DELETE)
@Transactional
public class UserDAOTest {
	@Rule
	public DbUnitRule dbUnit = new DbUnitRule();
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private IUserDAO userDao;
	@Autowired
	private ITokenDAO tokenDao;
	
	@Test
	public void getUserAndToken() {
		User demo1 = userDao.findOne(1L);
		
		Assert.assertNotNull(demo1);
		Assert.assertNotNull(demo1.getToken());
	}
	
	@Test
	public void saveUser() {
		User user = new User();
		user.setUsername("demo9");
		user.setEmail("demo9@demo.com");
		user.setTopoosId("9");
		
		User savedUser = userDao.save(user);
		
		User demo9 = userDao.findOne(savedUser.getId());
		
		Assert.assertNotNull(demo9);
	}
	
	@Test
	public void saveNewUserAndToken() {
		User newUser = new User();
		newUser.setUsername("demo9");
		newUser.setEmail("demo9@demo.com");
		newUser.setTopoosId("9");
		
		User existingUser = userDao.findByTopoosId(newUser.getTopoosId());
		
		if (existingUser == null) {
			existingUser = userDao.save(newUser);
		}
		
		if (existingUser.getToken() == null) {
			OauthToken token = new OauthToken();
			token.setAccessToken("9");
			token.setRefreshToken("9");
			token.setTokenType("bearer");
			token.setExpiresIn(99999999L);
			
			token.setUser(existingUser);
			token.setUserId(existingUser.getId());
			
			existingUser.setToken(token);
			
			existingUser = userDao.save(existingUser);
			
			Assert.assertNotNull(existingUser.getToken());
		}
		
		User createdUser = userDao.findOne(existingUser.getId());
		
		Assert.assertNotNull(createdUser);
		Assert.assertNotNull(createdUser.getToken());
		Assert.assertTrue(createdUser.getToken().getAccessToken().equals("9"));
	}
	
	@Test
	public void saveExistingUserAndToken() {
		User existingUser = userDao.findByTopoosId("1");
		
		OauthToken token = existingUser.getToken();
		token.setAccessToken("9");
		token.setRefreshToken("9");
		token.setTokenType("bearer");
		token.setExpiresIn(99999999L);
		
		tokenDao.save(token);
		
		User updatedUser = userDao.findOne(existingUser.getId());
		
		Assert.assertNotNull(updatedUser);
		Assert.assertNotNull(updatedUser.getToken());
		Assert.assertTrue(updatedUser.getToken().getAccessToken().equals("9"));
	}
}