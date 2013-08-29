package com.byteflair.parkplan.api.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.byteflair.parkplan.api.domain.Fence;
import com.byteflair.parkplan.api.domain.FencePairing;
import com.byteflair.parkplan.api.domain.FenceViolation;
import com.byteflair.parkplan.api.domain.Pairing;
import com.byteflair.parkplan.api.domain.PushNotification;
import com.byteflair.parkplan.api.domain.User;
import com.byteflair.parkplan.api.domain.dto.FenceDTO;
import com.byteflair.parkplan.api.domain.dto.FenceViolationDTO;
import com.byteflair.parkplan.api.domain.dto.PositionDTO;
import com.byteflair.parkplan.api.domain.dto.TrackDetailDTO;
import com.byteflair.parkplan.api.domain.specification.FenceSpecifications;
import com.byteflair.parkplan.api.domain.specification.PairingSpecifications;
import com.byteflair.parkplan.api.domain.type.FenceStatus;
import com.byteflair.parkplan.api.domain.type.PairingStatus;
import com.byteflair.parkplan.api.domain.type.PositionType;
import com.byteflair.parkplan.api.domain.type.TrackType;
import com.byteflair.parkplan.api.exception.ForbiddenActionException;
import com.byteflair.parkplan.api.exception.ForbiddenFenceException;
import com.byteflair.parkplan.api.exception.IncorrectFenceStatusException;
import com.byteflair.parkplan.api.exception.IncorrectPairingStatusException;
import com.byteflair.parkplan.api.exception.NonExistingFenceException;
import com.byteflair.parkplan.api.exception.NonExistingPairingException;
import com.byteflair.parkplan.api.exception.NonExistingUserException;
import com.byteflair.parkplan.api.exception.TopoosException;
import com.byteflair.parkplan.api.exception.UntrackedUserException;
import com.byteflair.parkplan.api.persistence.IFenceDAO;
import com.byteflair.parkplan.api.persistence.IFenceViolationDAO;
import com.byteflair.parkplan.api.persistence.IPairingDAO;
import com.byteflair.parkplan.api.persistence.IUserDAO;
import com.byteflair.parkplan.api.service.IFenceService;
import com.byteflair.parkplan.api.util.ListTransformer;
import com.byteflair.parkplan.api.util.UriUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FenceService implements IFenceService {
	private static final Logger logger = LoggerFactory.getLogger(FenceService.class);

	@Autowired
	IUserDAO userDAO;
	@Autowired
	IPairingDAO pairingDAO;
	@Autowired
	IFenceDAO fenceDAO;
	@Autowired
	IFenceViolationDAO fenceViolationDAO;
	
	@Autowired
	private UriUtils uriUtils;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	@Transactional
	public FenceDTO createFence(FenceDTO fenceDTO) throws NonExistingPairingException, ForbiddenFenceException, TopoosException, JsonProcessingException, IOException, NonExistingUserException {
		if (fenceDTO.getControllees().isEmpty()) {
			throw new IllegalArgumentException("Controlled user list can not be empty.");
		}
		
		Fence newFence = new Fence();
		newFence.setName(fenceDTO.getName());
		newFence.setStatus(FenceStatus.CREATED);
		
		User controller = userDAO.findOne(fenceDTO.getController());
		
		if (controller == null) {
			throw new NonExistingUserException("User (id:" + fenceDTO.getController() + ") does not exist.");
		}
		
		for (Long controlleeId : fenceDTO.getControllees()) {
			User controlled = userDAO.findOne(controlleeId);
			
			if (controlled == null) {
				throw new NonExistingUserException("User (id:" + controlleeId + ") does not exist.");
			}
			
			Pairing existingPairing = pairingDAO.findByControllerAndControlled(controller, controlled);

			if (existingPairing == null) {
				throw new NonExistingPairingException("Pairing (controller:" + fenceDTO.getController() + ", controlled:" + controlleeId + ") does not exist.");	
			}
			
			if (existingPairing.getStatus() != PairingStatus.ACCEPTED) {
				throw new ForbiddenFenceException("User (id:" + fenceDTO.getController() + ") can not track user (id:" + controlleeId + ").");
			}
			
			// create track in topoos
			ResponseEntity<String> responseTrackAdd = restTemplate.getForEntity(uriUtils.getTrackAddUri(controller.getToken().getAccessToken(), fenceDTO.getName()), String.class);
			
			if (responseTrackAdd.getStatusCode() != HttpStatus.OK) {
				logger.error("Track could not be created in TopoOS. Received status code " + responseTrackAdd.getStatusCode().toString());
				logger.error(responseTrackAdd.getBody());
				
				throw new TopoosException("Track could not be created in TopoOS. Received status code " + responseTrackAdd.getStatusCode().toString());
			}
			
			JsonNode rootNode = objectMapper.readTree(responseTrackAdd.getBody());
			
			newFence.addPairing(existingPairing, rootNode.get("id").asLong());
		}
		
		for (FencePairing fencePairing : newFence.getPairings()) {
			// create track rule in topoos
			ResponseEntity<String> responseRuleAdd = restTemplate.getForEntity(uriUtils.getRuleAddUri(controller.getToken().getAccessToken(), fencePairing.getTopoosTrackId(), fenceDTO.getLat(), fenceDTO.getLng(), fenceDTO.getRadius()), String.class);
			
			if (responseRuleAdd.getStatusCode() != HttpStatus.OK) {
				logger.error("Rule could not be created in TopoOS. Received status code " + responseRuleAdd.getStatusCode().toString());
				logger.error(responseRuleAdd.getBody());
				
				throw new TopoosException("Rule could not be created in TopoOS. Received status code " + responseRuleAdd.getStatusCode().toString());
			}			
		}
		
		newFence.setLat(fenceDTO.getLat());
		newFence.setLng(fenceDTO.getLng());
		newFence.setRadius(fenceDTO.getRadius());
		newFence.setTrackType(TrackType.TRACK_OUT_OF_BOUNDS);
		
		return new FenceDTO(fenceDAO.save(newFence));
	}
	
	@Override
	@Transactional
	public FenceDTO updateFence(Long userId, FenceDTO fenceDto) throws NonExistingFenceException, NonExistingUserException, IncorrectFenceStatusException, ForbiddenActionException {
		if (fenceDto.getId() == null) {
			throw new IllegalArgumentException("Fence's ID can not be blank.");
		}
		
		FenceStatus newStatus = null;
		
		try {
			newStatus = FenceStatus.valueOf(fenceDto.getStatus());
		} catch (IllegalArgumentException e) {
			throw new IncorrectFenceStatusException(fenceDto.getStatus() + " is not a valid track status. Acepted status are: " + StringUtils.join(FenceStatus.values(), ","), e);
		}
		
		Fence existingFence = fenceDAO.findOne(fenceDto.getId());
		
		if (existingFence == null) {
			throw new NonExistingFenceException("Fence (id:" + fenceDto.getId() + ") does not exist.");
		}
		
		User user = userDAO.findOne(userId);
		
		if (user == null) {
			throw new NonExistingUserException("User (id:" + userId + ") does not exist.");
		}
		
		if (!existingFence.getController().getId().equals(userId)) {
			throw new ForbiddenActionException("User (id:" + userId + ") is not controller of fence (id: " + fenceDto.getId() + ").");
		}
		
		switch (existingFence.getStatus()) {
			case CREATED: {
				switch (newStatus) {
					case STOPPED: {
						existingFence.setStatus(newStatus);
						break;
					}
					default: {
						throw new ForbiddenActionException(FenceStatus.CREATED + " fence can only be stopped by setting status to STOPPED.");
					}
				}
				
				break;
			}
			case RUNNING: {
				switch (newStatus) {
					case STOPPING: {
						existingFence.setStatus(newStatus);
						break;
					}
					default: {
						throw new ForbiddenActionException(FenceStatus.RUNNING + " fence can only be stopped by setting status to STOPPING.");
					}
				}
				
				break;
			}
			case STOPPING: {
				throw new ForbiddenActionException(FenceStatus.STOPPING + " fence can not be updated.");
			}
			case STOPPED: {
				throw new ForbiddenActionException(FenceStatus.STOPPED + " fence can not be updated.");
			}
			case EXPIRED: {
				throw new ForbiddenActionException(FenceStatus.EXPIRED + " fence can not be updated.");
			}
		}
		
		return new FenceDTO(fenceDAO.save(existingFence));
	}
	
	@Override
	@Transactional
	public PositionDTO createPosition(PositionDTO positionDto) throws IncorrectPairingStatusException, UntrackedUserException, TopoosException {
		if (positionDto.getUser() == null) {
			throw new IllegalArgumentException("Controlled user can not be empty.");
		}
		
		if (positionDto.getLat() == null || positionDto.getLng() == null) {
			throw new IllegalArgumentException("Incorrect coordinates.");
		}
		
		// find acepted pairings where user is controlled
		List<Pairing> pairingList = pairingDAO.findAll(PairingSpecifications.find(null, positionDto.getUser(), PairingStatus.ACCEPTED));

		if (pairingList.isEmpty()) {
			throw new IncorrectPairingStatusException("User (id:" + positionDto.getUser() + ") is not paired with somebody.");
		}
		
		boolean hasRunningFences = false;
		
		for (Pairing pairing : pairingList) {
			Set<FencePairing> fencePairingList = pairing.getFences();
			
			if (fencePairingList.isEmpty()) {
				throw new UntrackedUserException("User (id:" + positionDto.getUser() + ") is not being tracked.");
			}
			
			for (FencePairing fencePairing : fencePairingList) {
				Fence fence = fencePairing.getFence();
				
				URI uri = null;
				FenceStatus newStatus = null;
				
				switch (fence.getStatus()) {
					case CREATED: {
						uri = uriUtils.getPositionAddUri(pairing.getControlled().getToken().getAccessToken(), fencePairing.getTopoosTrackId(), positionDto.getLat(), positionDto.getLng(), PositionType.TRACK_INIT);
						newStatus = FenceStatus.RUNNING;
						
						break;
					}
					case RUNNING: {
						uri = uriUtils.getPositionAddUri(pairing.getControlled().getToken().getAccessToken(), fencePairing.getTopoosTrackId(), positionDto.getLat(), positionDto.getLng(), PositionType.POS);
						newStatus = FenceStatus.RUNNING;
						
						break;
					}
					case STOPPING: {
						uri = uriUtils.getPositionAddUri(pairing.getControlled().getToken().getAccessToken(), fencePairing.getTopoosTrackId(), positionDto.getLat(), positionDto.getLng(), PositionType.TRACK_END);
						newStatus = FenceStatus.STOPPED;
						
						break;
					}
				}
				
				if (uri != null) {
					hasRunningFences = true;
					
					// register position in topoos track
					ResponseEntity<String> responseTrackAdd = restTemplate.getForEntity(uri, String.class);
					
					if (responseTrackAdd.getStatusCode() != HttpStatus.OK) {
						logger.error("Position could not be registered in TopoOS. Received status code " + responseTrackAdd.getStatusCode().toString());
						logger.error(responseTrackAdd.getBody());
						
						throw new TopoosException("Position could not be registered in TopoOS. Received status code " + responseTrackAdd.getStatusCode().toString());
					}
				}
				
				if (newStatus != null && fence.getStatus() != newStatus) {
					fence.setStatus(newStatus);
					
					fenceDAO.save(fence);
				}
			}
		}
		
		if (!hasRunningFences) {
			throw new UntrackedUserException("User (id:" + positionDto.getUser() + ") has no running fences.");
		}
		
		return positionDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<FenceDTO> findFences(final Long userId, String status) throws IncorrectFenceStatusException {
		FenceStatus fenceStatus = null;
		
		if (StringUtils.isNotBlank(status)) {
			try {
				fenceStatus = FenceStatus.valueOf(status);
			} catch (IllegalArgumentException e) {
				throw new IncorrectFenceStatusException(status + " is not a valid fence status. Acepted status are: " + StringUtils.join(FenceStatus.values(), ","), e);
			}
		}
		
		List<Fence> foundFences = fenceDAO.findAll(FenceSpecifications.find(userId, fenceStatus));
		
		ListTransformer<Fence, FenceDTO> transformer = new ListTransformer<Fence, FenceDTO>() {
	        @Override  
	        public FenceDTO transform(Fence e) {
	        	if (e.getController().getId().equals(userId)) {
	        		return new FenceDTO(e);
	        	} else {
	        		FenceDTO temp = new FenceDTO(e);
	        		
	        		temp.getControllees().clear();
	        		temp.addControllee(userId);
	        		
	        		return temp;
	        	}
	        }
	    };
		
		return transformer.transform(foundFences);
	}
	
	@Override
	@Transactional(readOnly = true)
	public FenceDTO getFence(Long userId, Long fenceId) throws NonExistingFenceException, NonExistingUserException, ForbiddenActionException, TopoosException, JsonProcessingException, IOException {
		if (fenceId == null) {
			throw new IllegalArgumentException("Fence's ID can not be blank.");
		}
		
		Fence existingFence = fenceDAO.findOne(fenceId);
		
		if (existingFence == null) {
			throw new NonExistingFenceException("Fence (id:" + fenceId + ") does not exist.");
		}
		
		User user = userDAO.findOne(userId);
		
		if (user == null) {
			throw new NonExistingUserException("User (id:" + userId + ") does not exist.");
		}
		
		boolean userIsController = false;
		
		FenceDTO fenceDto = new FenceDTO(existingFence);
		
		if (existingFence.getController().getId().equals(userId)) {
			userIsController = true;
			
			fenceDto.getControllees().clear();
    		fenceDto.addControllee(userId);
		}
		
		if (!userIsController && !fenceDto.getControllees().contains(userId)) {
			throw new ForbiddenActionException("User (id:" + userId + ") is not controller nor controlled of fence (id: " + fenceId + ").");
		}
		
		TrackDetailDTO trackDetailDto = new TrackDetailDTO();
		
		for (FencePairing trackPairing : existingFence.getPairings()) {
			if (userIsController || (!userIsController && trackPairing.getPairing().getControlled().getId().equals(user.getId()))) {
				// get topoos track detail
				ResponseEntity<String> responseTrackDetail = restTemplate.getForEntity(uriUtils.getTrackDetailUri(trackPairing.getPairing().getController().getToken().getAccessToken(), trackPairing.getTopoosTrackId()), String.class);
				
				if (responseTrackDetail.getStatusCode() != HttpStatus.OK) {
					logger.error("Track (id:" + fenceId + ") could not be found in TopoOS. Received status code " + responseTrackDetail.getStatusCode().toString());
					logger.error(responseTrackDetail.getBody());
					
					throw new TopoosException("Track (id:" + fenceId + ") could not be found in TopoOS. Received status code " + responseTrackDetail.getStatusCode().toString());
				}
				
				User controlled = trackPairing.getPairing().getControlled();
				
				JsonNode rootNode = objectMapper.readTree(responseTrackDetail.getBody());
				
				Iterator<JsonNode> positions = rootNode.get("positions").elements();
				
				while (positions.hasNext()) {
					JsonNode position = positions.next();
					
					String sPositionType = position.get("position_type").get("description").asText();
					
					if (PositionType.valueOf(sPositionType).isReal()) {
						PositionDTO positionDto = new PositionDTO();
						positionDto.setLat(new BigDecimal(position.get("latitude").asText()));
						positionDto.setLng(new BigDecimal(position.get("longitude").asText()));
						positionDto.setDate(position.get("registerTime").asText(), "yyyy-MM-dd'T'HH:mm:ss.SSS");
						
						FenceViolation fenceViolation = fenceViolationDAO.findByTopoosPositionId(position.get("id").asLong());
						
						positionDto.setViolation(fenceViolation != null && fenceViolation.getFence().equals(existingFence));
						
						trackDetailDto.addPosition(controlled.getId(), positionDto);
					}
				}
			}
		}
		
		fenceDto.setDetail(trackDetailDto);
		
		return fenceDto;
	}
	
	@Override
	@Transactional
	public FenceViolationDTO registerFenceViolation(PushNotification pushNotification) throws NonExistingUserException, NonExistingFenceException, JsonProcessingException, IOException {
		User user = userDAO.findByTopoosId(pushNotification.getUser());
		
		if (user == null) {
			throw new NonExistingUserException("User (topoosId:" + pushNotification.getUser() + ") is not registered.");
		}
		
		JsonNode rootNode = objectMapper.readTree(pushNotification.getData());
				
		Long topoosTrackId = rootNode.get("track_id").asLong();
		
		Fence fence = fenceDAO.findOne(FenceSpecifications.findByTopoosTrackId(topoosTrackId));
		
		if (fence == null) {
			throw new NonExistingFenceException("Fence (topoosTrackId:" + topoosTrackId + ") does not exist.");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS");
		
		Date date = null;
		
		try {
			date = sdf.parse(rootNode.get("registerTime").asText());
		} catch (ParseException e) {
			throw new IllegalArgumentException("Incorrect date format (" + date + "). Expected format was yyyy-MM-dd'T'HH:mm:SSS");
		}
		
		BigDecimal lat = new BigDecimal(rootNode.get("latitude").asText());
		BigDecimal lng = new BigDecimal(rootNode.get("longitude").asText());
		
		fence.addViolation(user, date, lat, lng, rootNode.get("id").asLong());
		
		fenceDAO.save(fence);
		
		return new FenceViolationDTO(fence, user, date, lat, lng);
	}
}