package com.byteflair.parkplan.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.byteflair.parkplan.api.domain.User;

public interface IUserDAO extends JpaRepository<User, Long> {
	public User findByTopoosId(String topoosId);
	public User findByUsername(String username);
}