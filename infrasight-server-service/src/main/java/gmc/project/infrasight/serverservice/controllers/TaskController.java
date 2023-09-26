package gmc.project.infrasight.serverservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.serverservice.models.ResponseModel;
import gmc.project.infrasight.serverservice.models.ScheduleTaskModel;
import gmc.project.infrasight.serverservice.services.TaskService;

@RequestMapping(path = "/task")
@RestController
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@PostMapping
	public ResponseEntity<ResponseModel<String>> createTask(@RequestBody ScheduleTaskModel scheduleTaskModel) {
		HttpStatus status = HttpStatus.OK;
		ResponseModel<String> body = new ResponseModel<String>("Successfully schudled the task.", true);
		try {
			taskService.scheduleATask(scheduleTaskModel);
		} catch(Exception e) {
			e.printStackTrace();
			body.setMessage("Error scheduling the task.");
			body.setStatus(false);
			body.setError(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return ResponseEntity.status(status).body(body);
	}
	
	@GetMapping(path = "/delete/{taskId}")
	public ResponseEntity<ResponseModel<String>> createTask(@PathVariable String taskId) {
		HttpStatus status = HttpStatus.OK;
		ResponseModel<String> body = new ResponseModel<String>("Successfully removed schudled task.", true);
		try {
			taskService.deleteTask(taskId);
		} catch(Exception e) {
			e.printStackTrace();
			body.setMessage("Error scheduling the task.");
			body.setStatus(false);
			body.setError(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return ResponseEntity.status(status).body(body);
	}

}
