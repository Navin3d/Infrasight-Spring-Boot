package gmc.project.infrasight.presentationservice.services;

import gmc.project.infrasight.presentationservice.entities.ProjectEntity;

public interface ProjectService {
	public ProjectEntity findById(String id);
}
