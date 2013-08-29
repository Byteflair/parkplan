package com.byteflair.parkplan.api.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.byteflair.parkplan.api.domain.OauthToken;

public interface ITokenDAO extends JpaRepository<OauthToken, String> {
}