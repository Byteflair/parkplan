package com.byteflair.parkplan.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.byteflair.parkplan.api.domain.Fence;

public interface IFenceDAO extends JpaRepository<Fence, Long>, JpaSpecificationExecutor<Fence> {
}