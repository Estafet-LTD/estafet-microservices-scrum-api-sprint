package com.estafet.microservices.api.sprint.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.sprint.dao.SprintDAO;
import com.estafet.microservices.api.sprint.message.StartSprint;
import com.estafet.microservices.api.sprint.model.Sprint;

@Service
public class SprintService {

	@Autowired
	private SprintDAO sprintDAO;

	@Transactional
	public Sprint startSprint(StartSprint message) {
		if (!hasActiveSprint(message.getProjectId())) {
			Sprint sprint = new Sprint().start(message.getNoDays());
			List<Sprint> projectSprints = getProjectSprints(message.getProjectId());
			sprint.setNumber(projectSprints.size() + 1);
			sprint.setProjectId(message.getProjectId());
			sprintDAO.create(sprint);
			return sprint;
		} else {
			throw new RuntimeException("Cannot start a new sprint when one is active");
		}
	}

	private boolean hasActiveSprint(int projectId) {
		for (Sprint sprint : getProjectSprints(projectId)) {
			if (sprint.getStatus().equals("Active")) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public List<Sprint> getProjectSprints(int projectId) {
		return sprintDAO.getProjectSprints(projectId);
	}

	@Transactional
	public void deleteSprint(int sprintId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", sprintId);
		template.delete(System.getenv("PROJECT_REPOSITORY_SERVICE_URI") + "/sprint/{id}", params);
	}

	@Transactional(readOnly = true)
	public Sprint getSprint(int sprintId) {
		return sprintDAO.getSprint(sprintId);
	}
	
	@Transactional
	public Sprint updateSprint(Sprint update) {
		Sprint sprint = getSprint(update.getId());
		return sprintDAO.update(sprint.update(update));
	}

}
