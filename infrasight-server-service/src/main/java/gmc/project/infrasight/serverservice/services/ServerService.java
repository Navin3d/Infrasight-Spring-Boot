package gmc.project.infrasight.serverservice.services;

import gmc.project.infrasight.serverservice.models.CreateServerModel;

public interface ServerService {
	public void addToMonitorList(CreateServerModel createServerModel) throws Exception ;
}
