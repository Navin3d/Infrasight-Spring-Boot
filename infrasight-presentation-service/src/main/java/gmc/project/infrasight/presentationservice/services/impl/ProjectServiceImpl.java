package gmc.project.infrasight.presentationservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.presentationservice.daos.ProjectDao;
import gmc.project.infrasight.presentationservice.entities.ProjectEntity;
import gmc.project.infrasight.presentationservice.services.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectDao projectDao;

	@Override
	public ProjectEntity findById(String id) {
		return projectDao.findById(id).orElse(new ProjectEntity());
	}

}
