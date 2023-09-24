package gmc.project.infrasight.authservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.authservice.services.UserService;

@RequestMapping(path = "/user")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
//	@GetMapping(path = "/profile")
//	private ResponseEntity<ResponseModel> getUser() {
//		
//	}

}
