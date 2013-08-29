package com.byteflair.parkplan.api.domain.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackDetailDTO {
	private Map<Long, List<PositionDTO>> positions;

	public TrackDetailDTO() {
	}

	public Map<Long, List<PositionDTO>> getPositions() {
		return positions;
	}

	public void setPositions(Map<Long, List<PositionDTO>> positions) {
		this.positions = positions;
	}
	
	public void addPosition(Long controlleeId, PositionDTO position) {
		if (positions == null) {
			positions = new HashMap<Long, List<PositionDTO>>();
		}
		
		if (positions.containsKey(controlleeId)) {
			positions.get(controlleeId).add(position);
		} else {
			List<PositionDTO> positions = new ArrayList<PositionDTO>(1);
			positions.add(position);
			
			this.positions.put(controlleeId, positions);
		}
	}
}