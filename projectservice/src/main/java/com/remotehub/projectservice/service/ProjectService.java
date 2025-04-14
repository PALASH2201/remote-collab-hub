package com.remotehub.projectservice.service;

import com.remotehub.projectservice.dto.request.ProjectRequest;
import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.exceptions.ErrorCreatingEntry;
import com.remotehub.projectservice.exceptions.ErrorDeletingEntry;
import com.remotehub.projectservice.exceptions.ErrorUpdatingEntry;
import com.remotehub.projectservice.exceptions.ResourceNotFoundException;
import com.remotehub.projectservice.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void addNewProject(ProjectRequest projectRequest) {
        try{
            Project project = new Project();
            project.setTeamId(projectRequest.getTeamId());
            project.setProjectName(projectRequest.getProjectName());
            project.setProjectDesc(projectRequest.getProjectDesc());
            project.setStartDate(projectRequest.getStartDate());
            project.setCreatedBy(projectRequest.getCreatedBy());
            project.setCreatedAt(LocalDateTime.now());
            project.setLastUpdatedAt(LocalDateTime.now());
            project.setSprintList(new ArrayList<>());
            project.setTaskList(new ArrayList<>());
            projectRepository.save(project);
        } catch (Exception e) {
            log.error("Error in creating a new project",e);
            throw new ErrorCreatingEntry("Cannot create a new project");
        }
    }

    @Transactional
    public Project getProjectById(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find project with id : "+projectId));
    }

    @Transactional
    public void updateProject(UUID projectId, ProjectRequest projectRequest) {
        Project dbProject = projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find project with id : "+projectId));
        try{
            dbProject.setTeamId(projectRequest.getTeamId());
            dbProject.setProjectName(projectRequest.getProjectName());
            dbProject.setProjectDesc(projectRequest.getProjectDesc());
            dbProject.setStartDate(projectRequest.getStartDate());
            dbProject.setCreatedBy(projectRequest.getCreatedBy());
            dbProject.setLastUpdatedAt(LocalDateTime.now());
            projectRepository.save(dbProject);
        } catch (Exception e){
            log.error("Error updating project with id : {}",projectId);
            throw new ErrorUpdatingEntry("Cannot update project with id :"+projectId);
        }
    }

    @Transactional
    public void deleteProject(UUID projectId) {
        projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find project with id : "+projectId));
        try{
            projectRepository.deleteById(projectId);
        } catch (Exception e){
            log.error("Error deleting project with id : {}",projectId);
            throw new ErrorDeletingEntry("Cannot delete project with id : "+projectId);
        }
    }

    @Transactional
    public List<Project> getProjectsByTeamId(UUID teamId) {
        try{
            List<Project> list = projectRepository.findProjectByTeamId(teamId);
            if(list == null || list.isEmpty()) throw new RuntimeException();
            return list;
        } catch (Exception e){
            log.error("Cannot find projects for team with id : {}",teamId);
            throw new ResourceNotFoundException("Cannot find projects for team with team id : "+teamId);
        }
    }
}
