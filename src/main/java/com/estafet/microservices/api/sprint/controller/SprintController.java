package com.estafet.microservices.api.sprint.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.sprint.messages.CalculateSprints;
import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.service.SprintService;

@RestController
public class SprintController {

	@Value("${app.version}")
	private String appVersion;
	
	@Autowired
	private SprintService sprintService;
	
	@GetMapping(value = "/api")
	public Sprint getAPI() {
		return Sprint.getAPI(appVersion);
	}
			
	@GetMapping(value = "/sprint/{id}")
	public Sprint getSprint(@PathVariable int id) {
		return sprintService.getSprint(id);
	}
	
	@GetMapping(value = "/project/{id}/sprints")
	public List<Sprint> getProjectSprints(@PathVariable int id) {
		return sprintService.getProjectSprints(id);
	}
	
	@GetMapping(value = "/sprint/{id}/days")
	public List<String> getSprintDays(@PathVariable int id) {
		return sprintService.getSprintDays(id);
	}
	
	@GetMapping(value = "/sprint/{id}/day")
	public String getSprintDay(@PathVariable int id) {
		return sprintService.getSprintDay(id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/calculate-sprints")
	public ResponseEntity calculateSprints(@RequestBody CalculateSprints message) {
		return new ResponseEntity(sprintService.calculateSprints(message), HttpStatus.OK);
	}
	
}
