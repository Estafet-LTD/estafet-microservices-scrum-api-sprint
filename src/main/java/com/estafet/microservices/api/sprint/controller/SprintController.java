package com.estafet.microservices.api.sprint.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.sprint.message.StartSprint;
import com.estafet.microservices.api.sprint.model.Sprint;
import com.estafet.microservices.api.sprint.model.Story;
import com.estafet.microservices.api.sprint.service.SprintService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class SprintController {

	@Autowired
	private SprintService sprintService;
	
	@PostMapping("/start-sprint")
	public ResponseEntity startSprint(@RequestBody StartSprint message) {
		return new ResponseEntity(sprintService.startSprint(message), HttpStatus.OK);
	}
	
	@DeleteMapping("/sprint/{id}")
	public ResponseEntity deleteSprint(@PathVariable int id) {
		sprintService.deleteSprint(id);
		return new ResponseEntity(id, HttpStatus.OK);
	}
	
	@GetMapping(value = "/sprint/{id}/stories")
	public List<Story> getSprintStories(@PathVariable int id) {
		return sprintService.getSprintStories(id);
	}
	
	@GetMapping(value = "/sprint/{id}")
	public Sprint getSprint(@PathVariable int id) {
		return sprintService.getSprint(id);
	}
	
}
