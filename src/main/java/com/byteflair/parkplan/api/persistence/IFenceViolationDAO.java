package com.byteflair.parkplan.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.byteflair.parkplan.api.domain.FenceViolation;
import com.byteflair.parkplan.api.domain.FenceViolationId;

public interface IFenceViolationDAO extends JpaRepository<FenceViolation, FenceViolationId> {
	public FenceViolation findByTopoosPositionId(Long topoosPositionId);
}