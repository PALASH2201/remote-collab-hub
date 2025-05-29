package com.remotehub.projectservice.service;

import com.remotehub.projectservice.dto.mapper.TaskMapper;
import com.remotehub.projectservice.dto.request.TaskRequest;
import com.remotehub.projectservice.dto.response.TaskResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, SprintRepository sprintRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
        this.taskMapper = taskMapper;
    }


    @Transactional
    public void addNewTask(UUID projectId , TaskRequest taskRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find Project with id : " + projectId));
        try{
            Task task = new Task();
            task.setTaskTitle(taskRequest.getTaskTitle());
            task.setTaskDesc(taskRequest.getTaskDesc());
            task.setTaskStatus(taskRequest.getTaskStatus());
            task.setTaskPriority(taskRequest.getTaskPriority());
            task.setProject(project);
            task.setAssigneeId(taskRequest.getAssigneeId());
            task.setDueDate(taskRequest.getDueDate());
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            Task saved = taskRepository.save(task);
            
            List<Task> oldList = project.getTaskList();
            oldList.add(saved);
            projectRepository.save(project);
        } catch (Exception e) {
            log.error("Error in creating a new task",e);
            throw new ErrorCreatingEntry("Cannot create a new task");
        }
    }

    @Transactional
    public TaskResponse getTaskById(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find task with id : "+taskId));
        return taskMapper.toTaskResponse(task);
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
            dbTask.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(dbTask);
        } catch (Exception e){
            log.error("Error updating task with id : {}",taskId);
            throw new ErrorUpdatingEntry("Cannot update task with id :"+taskId);
        }
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find task with id : "+taskId));
        try{
            if(task.getProject() != null){
                Project p = task.getProject();
                p.getTaskList().remove(task);
            }
            if(task.getSprint() != null){
                Sprint s = task.getSprint();
                s.getTaskList().remove(task);
            }
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
