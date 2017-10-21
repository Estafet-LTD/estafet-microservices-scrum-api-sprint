package com.estafet.microservices.api.sprint.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.sprint.dao.SprintDAO;
import com.estafet.microservices.api.sprint.message.StartSprint;
import com.estafet.microservices.api.sprint.model.Sprint;

@Component
public class SprintFactory {
	
	@Autowired
	private SprintDAO sprintDAO;
		
	public Sprint createSprint(StartSprint message) {
		if (!hasActiveSprint(message.getProjectId())) {
			List<Sprint> projectSprints = sprintDAO.getProjectSprints(message.getProjectId());
			return new Sprint().start(message.getProjectId(), message.getNoDays(), projectSprints);
		} else {
			throw new RuntimeException("Cannot start a new sprint when one is active");
		}
	}

	private boolean hasActiveSprint(int projectId) {
		for (Sprint sprint : sprintDAO.getProjectSprints(projectId)) {
			if (sprint.getStatus().equals("Active")) {
				return true;
			}
		}
		return false;
	}
	
}