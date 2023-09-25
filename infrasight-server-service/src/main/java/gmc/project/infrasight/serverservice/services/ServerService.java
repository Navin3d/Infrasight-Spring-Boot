package gmc.project.infrasight.serverservice.services;

import gmc.project.infrasight.serverservice.models.ServerModel;

public interface ServerService {
	public ServerModel getServer(String id, String userId) throws Exception;
	
	public void addToMonitorList(ServerModel createServerModel) throws Exception;
	public void updateServerDetails(ServerModel updatdServer) throws Exception;
}
