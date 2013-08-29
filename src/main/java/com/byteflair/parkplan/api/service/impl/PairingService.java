package com.byteflair.parkplan.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.byteflair.parkplan.api.domain.Fence;
import com.byteflair.parkplan.api.domain.Pairing;
import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.domain.dto.PairingDTO;
import com.byteflair.parkplan.api.domain.dto.UserDTO;
import com.byteflair.parkplan.api.domain.specification.FenceSpecifications;
import com.byteflair.parkplan.api.domain.specification.PairingSpecifications;
import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.byteflair.parkplan.api.domain.type.PairingStatus;
import com.byteflair.parkplan.api.exception.DuplicatePairingException;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.persistence.IFenceDAO;
import com.byteflair.parkplan.api.persistence.IPairingDAO;
import com.byteflair.parkplan.api.persistence.IUserDAO;
import com.byteflair.parkplan.api.service.IPairingService;
import com.byteflair.parkplan.api.util.ListTransformer;

@Service
public class PairingService implements IPairingService {
	
	@Autowired
	IPairingDAO pairingDAO;
	@Autowired
	IUserDAO userDAO;
	@Autowired
	IFenceDAO fenceDao;

	@Override
	public UserDTO createController(Long controlledId, UserDTO controller) throws NonExistingUserException, DuplicatePairingException {
		if (StringUtils.isBlank(controller.getUsername())) {
			throw new IllegalArgumentException("Controller username can not be blank.");
		}
		
		User existingController = userDAO.findByUsername(controller.getUsername());
		
		if (existingController == null) {
			throw new NonExistingUserException("User (username:" + controller.getUsername() + ") does not exist.");
		}
		
		return createPairing(new PairingDTO(existingController.getId(), controlledId), false);
	}
	
	@Override
	public UserDTO createControlled(Long controllerId, UserDTO controlled) throws NonExistingUserException, DuplicatePairingException {
		if (StringUtils.isBlank(controlled.getUsername())) {
			throw new IllegalArgumentException("Controlled username can not be blank.");
		}
		
		User existingControlled = userDAO.findByUsername(controlled.getUsername());
		
		if (existingControlled == null) {
			throw new NonExistingUserException("User (username:" + controlled.getUsername() + ") does not exist.");
		}
		
		return createPairing(new PairingDTO(controllerId, existingControlled.getId()), false);
	}
	
	@Transactional
	private UserDTO createPairing(PairingDTO pairingDTO, boolean returnController) throws NonExistingUserException, DuplicatePairingException {
		if (pairingDTO.getController() == null) {
			throw new IllegalArgumentException("Controller user can not be blank.");
		}
		
		User controller = userDAO.findOne(pairingDTO.getController());
		
		if (controller == null) {
			throw new NonExistingUserException("User (id:" + pairingDTO.getController() + ") does not exist.");
		}
		
		if (pairingDTO.getControlled() == null) {
			throw new IllegalArgumentException("Controlled user can not be blank.");
		}
		
		User controlled = userDAO.findOne(pairingDTO.getControlled());
		
		if (controlled == null) {
			throw new NonExistingUserException("User (id:" + pairingDTO.getControlled() + ") does not exist.");
		}
		
		Pairing existingPairing = pairingDAO.findByControllerAndControlled(controller, controlled);
		
		if (existingPairing != null) {
			if (existingPairing.getStatus() == PairingStatus.PENDING || existingPairing.getStatus() == PairingStatus.ACCEPTED) {
				throw new DuplicatePairingException("Pairing (controller:" + pairingDTO.getController() + ", controlled:" + pairingDTO.getControlled() + ") is " + existingPairing.getStatus().toString() + ".");
			}
			
			existingPairing.setStatus(PairingStatus.PENDING);
			
			pairingDAO.save(existingPairing);
		}
		
		pairingDAO.save(new Pairing(controller, controlled, PairingStatus.PENDING));
		
		if (returnController) {
			return new UserDTO(controller);
		} else {
			return new UserDTO(controlled);
		}
	}

	@Override
	public UserDTO deleteController(PairingDTO pairingDto) throws NonExistingUserException, NonExistingPairingException {
		return deletePairing(pairingDto, true);
	}
	
	@Override
	public UserDTO deleteControlled(PairingDTO pairingDto) throws NonExistingUserException, NonExistingPairingException {
		return deletePairing(pairingDto, false);
	}
	
	@Transactional
	private UserDTO deletePairing(PairingDTO pairingDto, boolean returnController) throws NonExistingUserException, NonExistingPairingException {
		if (pairingDto.getController() == null) {
			throw new IllegalArgumentException("Controller user can not be blank.");
		}
		
		User controller = userDAO.findOne(pairingDto.getController());
		
		if (controller == null) {
			throw new NonExistingUserException("User (id:" + pairingDto.getController() + ") does not exist.");
		}
		
		if (pairingDto.getControlled() == null) {
			throw new IllegalArgumentException("Controlled user can not be blank.");
		}
		
		User controlled = userDAO.findOne(pairingDto.getControlled());
		
		if (controlled == null) {
			throw new NonExistingUserException("User (id:" + pairingDto.getControlled() + ") does not exist.");
		}
		
		Pairing existingPairing = pairingDAO.findByControllerAndControlled(controller, controlled);
		
		if (existingPairing != null) {
			List<Fence> fences = fenceDao.findAll(FenceSpecifications.findByPairingId(existingPairing.getId()));

			for (Fence fence : fences) {
				fence.setStatus(FenceStatus.STOPPING);
				fenceDao.save(fence);
			}
			
			existingPairing.setStatus(PairingStatus.CANCELED);
			pairingDAO.save(existingPairing);
		} else {
			throw new NonExistingPairingException("Pairing (controller:" + pairingDto.getController() + ", controlled:" + pairingDto.getControlled() + ") does not exist.");
		}
		
		if (returnController) {
			return new UserDTO(controller);
		} else {
			return new UserDTO(controlled);
		}
	}
	
	@Override
	public UserDTO getController(PairingDTO pairingDto) throws NonExistingPairingException, NonExistingUserException {
		return getPairing(pairingDto, true);
	}

	@Override
	public UserDTO getControlled(PairingDTO pairingDto) throws NonExistingPairingException, NonExistingUserException {
		return getPairing(pairingDto, false);
	}
	
	@Transactional(readOnly = true)
	private UserDTO getPairing(PairingDTO pairingDto, boolean returnController) throws NonExistingPairingException, NonExistingUserException {
		if (pairingDto.getController() == null) {
			throw new IllegalArgumentException("Controller user can not be blank.");
		}
		
		User controller = userDAO.findOne(pairingDto.getControlled());
		
		if (controller == null) {
			throw new NonExistingUserException("User (id:" + pairingDto.getController() + ") does not exist.");
		}
		
		if (pairingDto.getControlled() == null) {
			throw new IllegalArgumentException("Controlled user can not be blank.");
		}
		
		User controlled = userDAO.findOne(pairingDto.getControlled());
		
		if (controlled == null) {
			throw new NonExistingUserException("User (id:" + pairingDto.getControlled() + ") does not exist.");
		}
		
		Pairing existingPairing = pairingDAO.findByControllerAndControlled(controller, controlled);
		
		if (existingPairing == null) {
			throw new NonExistingPairingException("Pairing (controller:" + pairingDto.getController() + ", controlled:" + pairingDto.getControlled() + ") does not exist.");
		} else if (existingPairing != null && existingPairing.getStatus() != PairingStatus.ACCEPTED) {
			throw new NonExistingPairingException("Pairing (controller:" + pairingDto.getController() + ", controlled:" + pairingDto.getControlled() + ") is not already accepted.");
		}
		
		if (returnController) {
			return new UserDTO(controller);
		} else {
			return new UserDTO(controlled);
		}
	}
	
	@Override
	public List<UserDTO> getControllees(Long controllerId) {
		return getUsers(controllerId, true);
	}
	
	@Override
	public List<UserDTO> getControllers(Long controlledId) {
		return getUsers(controlledId, false);
	}
	
	@Transactional(readOnly = true)
	private List<UserDTO> getUsers(Long userId, final boolean isController) {
		List<Pairing> foundPairings = pairingDAO.findAll(PairingSpecifications.find(isController ? userId : null, isController ? null : userId, PairingStatus.ACCEPTED));
		
		ListTransformer<Pairing, UserDTO> transformer = new ListTransformer<Pairing, UserDTO>() {
	        @Override  
	        public UserDTO transform(Pairing e) {
	        	User user = isController ? e.getControlled() : e.getController();
	        	
	        	UserDTO userDto = new UserDTO();
	        	userDto.setId(user.getId());
	        	userDto.setUsername(user.getUsername());
	        	
	            return userDto;
	        }
	    };
		
		return transformer.transform(foundPairings);
	}
	
	@Override
	@Transactional
	public PairingDTO updatePairing(PairingDTO pairingDTO) throws NonExistingUserException, DuplicatePairingException, NonExistingPairingException, ForbiddenActionException, IncorrectPairingStatusException {
		PairingStatus newStatus = null;
		
		try {
			newStatus = PairingStatus.valueOf(pairingDTO.getStatus());
		} catch (IllegalArgumentException e) {
			throw new IncorrectPairingStatusException(pairingDTO.getStatus() + " is not a valid pairing status. Acepted status are: " + StringUtils.join(PairingStatus.values(), ","), e);
		}
		
		Pairing existingPairing = pairingDAO.findOne(pairingDTO.getId());
		
		if (existingPairing == null) {
			throw new NonExistingPairingException("Pairing (id:" + pairingDTO.getId() + ") does not exist.");
		}
		
		switch (existingPairing.getStatus()) {
			case PENDING: {
				switch (newStatus) {
					case ACCEPTED: {
						existingPairing.setStatus(newStatus);
						break;
					}
					case REJECTED: {
						existingPairing.setStatus(newStatus);
						break;
					}
					default: {
						throw new ForbiddenActionException(PairingStatus.PENDING + " pairing can only be accepted or rejected.");
					}
				}
				
				break;
			}
			case ACCEPTED: {
				switch (newStatus) {
					case CANCELED: {
						existingPairing.setStatus(newStatus);
						break;
					}
					default: {
						throw new ForbiddenActionException(PairingStatus.ACCEPTED + " pairing can only be canceled.");
					}
				}
				
				break;
			}
			case CANCELED: {
				throw new ForbiddenActionException(PairingStatus.CANCELED + " pairing can not be updated. Another pairing should be created.");
			}
			case REJECTED: {
				throw new ForbiddenActionException(PairingStatus.REJECTED + " pairing can not be updated. Another pairing should be created.");
			}
		}
		
		return new PairingDTO(pairingDAO.save(existingPairing));
	}

	@Override
	@Transactional(readOnly = true)
	public PairingDTO getPairing(Long pairingId) throws NonExistingPairingException {
		if (pairingId == null) {
			throw new IllegalArgumentException("Pairing ID can not be blank.");
		}
		
		Pairing existingPairing = pairingDAO.findOne(pairingId);
		
		if (existingPairing == null) {
			throw new NonExistingPairingException("Pairing (id:" + pairingId + ") does not exist.");
		}
		
		return new PairingDTO(existingPairing);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PairingDTO> findPairings(PairingDTO pairingDTO) throws IncorrectPairingStatusException {
		PairingStatus status = null;
		
		if (StringUtils.isNotBlank(pairingDTO.getStatus())) {
			try {
				status = PairingStatus.valueOf(pairingDTO.getStatus());
			} catch (IllegalArgumentException e) {
				throw new IncorrectPairingStatusException(pairingDTO.getStatus() + " is not a valid pairing status. Acepted status are: " + StringUtils.join(PairingStatus.values(), ","), e);
			}
		}
		
		List<Pairing> foundPairings = pairingDAO.findAll(PairingSpecifications.find(pairingDTO.getController(), pairingDTO.getControlled(), status));
		
		ListTransformer<Pairing, PairingDTO> transformer = new ListTransformer<Pairing, PairingDTO>() {
	        @Override  
	        public PairingDTO transform(Pairing e) {
	            return new PairingDTO(e);
	        }
	    };
		
		return transformer.transform(foundPairings);
	}
}