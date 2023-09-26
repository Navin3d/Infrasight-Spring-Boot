package gmc.project.infrasight.serverservice.services;

import java.util.List;

import gmc.project.infrasight.serverservice.entities.TaskEntity;
import gmc.project.infrasight.serverservice.models.ScheduleTaskModel;

public interface TaskService {
	public void scheduleATask(ScheduleTaskModel scheduleModel);
	public void deleteTask(String taskid);
	
	public List<TaskEntity> getTasksToExecute();
}
