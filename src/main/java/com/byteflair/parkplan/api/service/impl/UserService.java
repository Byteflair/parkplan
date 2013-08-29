package com.byteflair.parkplan.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.domain.dto.UserDTO;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.persistence.IUserDAO;
import com.byteflair.parkplan.api.service.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	IUserDAO userDAO;
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO get(Long userId) throws NonExistingUserException {
		if (userId == null) {
			throw new IllegalArgumentException("User's ID can not be blank.");
		}
		
		User user = userDAO.findOne(userId);
		
		if (user == null) {
			throw new NonExistingUserException("User (id:" + userId + ") does not exist.");
		}
		
		return new UserDTO(user);
	}
}