package com.byteflair.parkplan.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.byteflair.parkplan.api.domain.Pairing;
import com.byteflair.parkplan.api.domain.User;

public interface IPairingDAO extends JpaRepository<Pairing, Long>, JpaSpecificationExecutor<Pairing> {
	public Pairing findByControllerAndControlled(User controller, User controlled);
}