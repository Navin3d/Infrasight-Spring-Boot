package gmc.project.infrasight.authservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.authservice.models.ResponseModel;
import gmc.project.infrasight.authservice.models.UserModel;
import gmc.project.infrasight.authservice.services.AuthService;

@RequestMapping(path = "/auth")
@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping
	private ResponseEntity<ResponseModel> registerUser(@RequestBody UserModel userModel) {
		UserModel createdUser = null;
		ResponseModel response = null;
		HttpStatus status;
		try {
			createdUser = authService.createUser(userModel);
			response = new ResponseModel("User created Successfully", true);
			response.setData(createdUser);
			status = HttpStatus.CREATED;
		} catch(Exception e) {
			e.printStackTrace();
			response = new ResponseModel("Error Creating user.", false);
			response.setError(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return ResponseEntity.status(status).body(response);
	}

}
