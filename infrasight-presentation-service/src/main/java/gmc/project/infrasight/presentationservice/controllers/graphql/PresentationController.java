package gmc.project.infrasight.presentationservice.controllers.graphql;

import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.presentationservice.entities.ProjectEntity;
import gmc.project.infrasight.presentationservice.entities.ServerEntity;
import gmc.project.infrasight.presentationservice.entities.UserEntity;
import gmc.project.infrasight.presentationservice.services.ProjectService;
import gmc.project.infrasight.presentationservice.services.ServerService;
import gmc.project.infrasight.presentationservice.services.UserService;

@RestController
public class PresentationController {

	@Autowired
	private ServerService serverService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;

	@QueryMapping
	public List<ServerEntity> servers(@Argument Integer limit, @Argument Integer page, @Argument String from,
			@Argument String to) {
		try {
			if (from != null && to != null)
				return serverService.findServersByDates(page, limit, from, to);
			else
				return serverService.findAll(page, limit);
		} catch (Exception e) {
			e.printStackTrace();
			return serverService.findAll(page, limit);
		}
	}

	@QueryMapping
	public ServerEntity server(@Argument String id, @Argument String from, @Argument String to) {
		ServerEntity foundServer = null;
		try {
			if (from != null && to != null)
				foundServer = serverService.findByDateTime(id, from, to);
			else if (to != null)
				foundServer = serverService.findByDateTime(id, to);
			else
				foundServer = serverService.findOne(id);
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
			foundServer = new ServerEntity();
		} catch (Exception e) {
			e.printStackTrace();
			foundServer = new ServerEntity();
		}
		return foundServer;
	}
	
	@QueryMapping
	public List<UserEntity> users() {
		List<UserEntity> foundUser = null;
		try {
			foundUser = userService.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			foundUser = new ArrayList<>();
		}
		return foundUser;
	}
	
	@QueryMapping
	public UserEntity user(@Argument String id) {
		UserEntity foundUser = null;
		try {
			foundUser = userService.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
			foundUser = new UserEntity();
		}
		return foundUser;
	}
	
	@QueryMapping
	public List<ServerEntity> userServers(@Argument String userId, @Argument String from, @Argument String to) {
		List<ServerEntity> returnValue = null;
		try {
			returnValue = userService.getUserAccessServers(userId, from, to);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = new ArrayList<>();
		}
		return returnValue;
	}
	
	@QueryMapping
	public ProjectEntity project(@Argument String uniqueId) {
		ProjectEntity returnValue = null;
		try {
			returnValue = projectService.findById(uniqueId);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = new ProjectEntity();
		}
		return returnValue;
	}

}
