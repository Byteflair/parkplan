package com.byteflair.parkplan.api.service;

import com.byteflair.parkplan.api.domain.dto.UserDTO;
import com.byteflair.parkplan.api.exception.NonExistingUserException;

public interface IUserService {
	public UserDTO get(Long userId) throws NonExistingUserException;
}