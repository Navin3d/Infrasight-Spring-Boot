package gmc.project.infrasight.presentationservice.controllers.graphql;

import java.rmi.server.ServerNotActiveException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.infrasight.presentationservice.entities.ServerEntity;
import gmc.project.infrasight.presentationservice.services.ServerService;

@RestController
public class PresentationController {

	@Autowired
	private ServerService serverService;

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
			foundServer = new ServerEntity();
		}
		return foundServer;
	}

}
