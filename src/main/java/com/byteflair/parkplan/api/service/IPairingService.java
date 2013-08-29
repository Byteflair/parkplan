package com.byteflair.parkplan.api.service;

import java.util.List;

import com.byteflair.parkplan.api.domain.dto.PairingDTO;
import com.byteflair.parkplan.api.domain.dto.UserDTO;
import com.byteflair.parkplan.api.exception.DuplicatePairingException;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;


public interface IPairingService {
	public UserDTO createController(Long controlledId, UserDTO controllerUsername) throws NonExistingUserException, DuplicatePairingException;
	public UserDTO createControlled(Long controllerId, UserDTO controlledUsername) throws NonExistingUserException, DuplicatePairingException;
	public UserDTO deleteController(PairingDTO pairingDto) throws NonExistingUserException, NonExistingPairingException;
	public UserDTO deleteControlled(PairingDTO pairingDto) throws NonExistingUserException, NonExistingPairingException;	
	public UserDTO getController(PairingDTO pairingDto) throws NonExistingPairingException, NonExistingUserException;
	public UserDTO getControlled(PairingDTO pairingDto) throws NonExistingPairingException, NonExistingUserException;
	public List<UserDTO> getControllers(Long controlledId);
	public List<UserDTO> getControllees(Long controllerId);
	
	public PairingDTO updatePairing(PairingDTO pairingDto) throws NonExistingUserException, DuplicatePairingException, NonExistingPairingException, ForbiddenActionException, IncorrectPairingStatusException;
	public PairingDTO getPairing(Long pairingId) throws NonExistingPairingException;
	public List<PairingDTO> findPairings(PairingDTO pairingDto) throws IncorrectPairingStatusException;
}