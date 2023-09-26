package gmc.project.infrasight.serverservice.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.infrasight.serverservice.daos.ServerDao;
import gmc.project.infrasight.serverservice.daos.TaskDao;
import gmc.project.infrasight.serverservice.entities.ServerEntity;
import gmc.project.infrasight.serverservice.entities.TaskEntity;
import gmc.project.infrasight.serverservice.models.ScheduleTaskModel;
import gmc.project.infrasight.serverservice.services.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	private ServerDao serverDao;
	@Autowired
	private TaskDao taskDao;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public void scheduleATask(ScheduleTaskModel scheduleModel) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		TaskEntity task = modelMapper.map(scheduleModel, TaskEntity.class);
		ServerEntity foundServer = serverDao.findById(scheduleModel.getServerId()).orElse(null);
		task.setRunOnServers(foundServer);
		task.setDeleteAfterExecution(false);
		TaskEntity savedTask = taskDao.save(task);
		foundServer.getTasks().add(savedTask);
		serverDao.save(foundServer);
	}

	@Override
	public void deleteTask(String taskid) {
		taskDao.deleteById(taskid);
	}

	@Override
	public List<TaskEntity> getTasksToExecute() {
		return taskDao.findByAtEndOfDay(true);
	}

}
