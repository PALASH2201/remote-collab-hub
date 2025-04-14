package com.remotehub.projectservice.service;

import com.remotehub.projectservice.dto.request.TaskRequest;
import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.entity.Sprint;
import com.remotehub.projectservice.entity.Task;
import com.remotehub.projectservice.exceptions.ErrorCreatingEntry;
import com.remotehub.projectservice.exceptions.ErrorDeletingEntry;
import com.remotehub.projectservice.exceptions.ErrorUpdatingEntry;
import com.remotehub.projectservice.exceptions.ResourceNotFoundException;
import com.remotehub.projectservice.repository.ProjectRepository;
import com.remotehub.projectservice.repository.SprintRepository;
import com.remotehub.projectservice.repository.TaskRepository;
import com.remotehub.projectservice.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, SprintRepository sprintRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
    }


    @Transactional
    public void addNewTask(UUID projectId , TaskRequest taskRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find Project with id : " + projectId));
        try{
            Task task = new Task();
            task.setTaskTitle(task.getTaskTitle());
            task.setTaskDesc(task.getTaskDesc());
            task.setTaskStatus(task.getTaskStatus());
            task.setTaskPriority(task.getTaskPriority());
            task.setProject(project);
            task.setSprint(null);
            task.setAssigneeId(task.getAssigneeId());
            task.setDueDate(taskRequest.getDueDate());
            Task saved = taskRepository.save(task);
            
            List<Task> oldList = project.getTaskList();
            oldList.add(saved);
            projectRepository.save(project);
        } catch (Exception e) {
            log.error("Error in creating a new task",e);
            throw new ErrorCreatingEntry("Cannot create a new task");
        }
    }

    public Task getTaskById(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find task with id : "+taskId));
    }

    @Transactional
    public void updateTask(UUID taskId, TaskRequest taskRequest) {
        Task dbTask = taskRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find task with id : "+taskId));
        try{
            dbTask.setTaskTitle(taskRequest.getTaskTitle());
            dbTask.setTaskDesc(taskRequest.getTaskDesc());
            dbTask.setTaskStatus(taskRequest.getTaskStatus());
            dbTask.setTaskPriority(taskRequest.getTaskPriority());
            dbTask.setAssigneeId(taskRequest.getAssigneeId());
            dbTask.setDueDate(taskRequest.getDueDate());
            taskRepository.save(dbTask);
        } catch (Exception e){
            log.error("Error updating task with id : {}",taskId);
            throw new ErrorUpdatingEntry("Cannot update task with id :"+taskId);
        }
    }

    public void deleteTask(UUID taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find task with id : "+taskId));
        try{
            taskRepository.deleteById(taskId);
        } catch (Exception e){
            log.error("Error deleting task with id : {}",taskId);
            throw new ErrorDeletingEntry("Cannot delete task with id : "+taskId);
        }
    }

    @Transactional
    public void assignTask(UUID sprintId,UUID taskId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find sprint with id : "+sprintId));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find task with id : "+taskId));
        try{
            task.setSprint(sprint);
            Task saved = taskRepository.save(task);

            List<Task> oldList = sprint.getTaskList();
            oldList.add(saved);
            sprintRepository.save(sprint);
        } catch (Exception e) {
            log.error("Error assigning task to sprint");
            throw new ErrorUpdatingEntry("Cannot assign task to sprint");
        }
    }
}
