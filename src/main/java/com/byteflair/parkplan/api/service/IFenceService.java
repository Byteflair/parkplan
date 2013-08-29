package com.byteflair.parkplan.api.service;

import java.io.IOException;
import java.util.List;

import com.byteflair.parkplan.api.domain.PushNotification;
import com.byteflair.parkplan.api.domain.dto.FenceDTO;
import com.byteflair.parkplan.api.domain.dto.FenceViolationDTO;
import com.byteflair.parkplan.api.domain.dto.PositionDTO;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.ForbiddenFenceException;
import com.byteflair.parkplan.api.exception.IncorrectFenceStatusException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingFenceException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.exception.UntrackedUserException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IFenceService {
	public FenceDTO createFence(FenceDTO trackDTO) throws NonExistingPairingException, ForbiddenFenceException, TopoosException, JsonProcessingException, IOException, NonExistingUserException;
	public FenceDTO updateFence(Long userId, FenceDTO fenceDto) throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException;
	public PositionDTO createPosition(PositionDTO positionDto) throws IncorrectPairingStatusException, UntrackedUserException, TopoosException;
	public List<FenceDTO> findFences(Long userId, String status) throws IncorrectFenceStatusException;
	public FenceDTO getFence(Long userId, Long trackId) throws NonExistingFenceException, NonExistingUserException, ForbiddenActionException, TopoosException, JsonProcessingException, IOException;
	public FenceViolationDTO registerFenceViolation(PushNotification pushNotification) throws NonExistingUserException, NonExistingFenceException, JsonProcessingException, IOException;
}