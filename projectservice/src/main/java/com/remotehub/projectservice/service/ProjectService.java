package com.remotehub.projectservice.service;

import com.remotehub.projectservice.dto.mapper.ProjectMapper;
import com.remotehub.projectservice.dto.request.ProjectRequest;
import com.remotehub.projectservice.dto.response.ProjectResponse;
import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.exceptions.ErrorCreatingEntry;
import com.remotehub.projectservice.exceptions.ErrorDeletingEntry;
import com.remotehub.projectservice.exceptions.ErrorUpdatingEntry;
import com.remotehub.projectservice.exceptions.ResourceNotFoundException;
import com.remotehub.projectservice.feign.ProjectInterface;
import com.remotehub.projectservice.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectInterface projectInterface;
    private final HttpStatusCode statusCode;
    
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectInterface projectInterface) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectInterface = projectInterface;
        this.statusCode = HttpStatusCode.valueOf(200);
    }

    @Transactional
    public void addNewProject(ProjectRequest projectRequest) {
        ResponseEntity<Boolean> resp = projectInterface.checkIfTeamExists(projectRequest.getTeamId());
        if(!statusCode.isSameCodeAs(resp.getStatusCode())) throw new ResourceNotFoundException("Cannot find team with id: "+projectRequest.getTeamId());
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
    public ProjectResponse getProjectById(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find project with id : "+projectId));
        return projectMapper.toProjectResponse(project);
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
    public List<ProjectResponse> getProjectsByTeamId(UUID teamId) {
        try{
            List<Project> list = projectRepository.findProjectByTeamId(teamId);
            if(list == null || list.isEmpty()) throw new RuntimeException();
            List<ProjectResponse> projectResponseList = new ArrayList<>();
            for(Project p : list){
                projectResponseList.add(projectMapper.toProjectResponse(p));
            }
            return projectResponseList;
        } catch (Exception e){
            log.error("Cannot find projects for team with id : {}",teamId);
            throw new ResourceNotFoundException("Cannot find projects for team with team id : "+teamId);
        }
    }
}
