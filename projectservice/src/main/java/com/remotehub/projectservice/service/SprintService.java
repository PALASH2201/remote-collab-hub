package com.remotehub.projectservice.service;

import com.remotehub.projectservice.dto.request.SprintRequest;
import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.entity.Sprint;
import com.remotehub.projectservice.exceptions.ErrorCreatingEntry;
import com.remotehub.projectservice.exceptions.ErrorDeletingEntry;
import com.remotehub.projectservice.exceptions.ErrorUpdatingEntry;
import com.remotehub.projectservice.exceptions.ResourceNotFoundException;
import com.remotehub.projectservice.repository.ProjectRepository;
import com.remotehub.projectservice.repository.SprintRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    
    public SprintService(SprintRepository sprintRepository, ProjectRepository projectRepository) {
        this.sprintRepository = sprintRepository;
        this.projectRepository = projectRepository;
    }
    
    @Transactional
    public void addNewSprint(UUID sprintId , SprintRequest sprintRequest) {
        Project project = projectRepository.findById(sprintId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find Project with id : " + sprintId));
        try{
            Sprint sprint = new Sprint();
            sprint.setTaskList(new ArrayList<>());
            sprint.setProject(project);
            sprint.setSprintName(sprintRequest.getSprintName());
            sprint.setSprintGoal(sprintRequest.getSprintGoal());
            sprint.setSprintCreator(sprintRequest.getSprintCreator());
            sprint.setStartDate(sprintRequest.getStartDate());
            sprint.setEndDate(sprintRequest.getEndDate());
            Sprint saved = sprintRepository.save(sprint);
            
            List<Sprint> oldList = project.getSprintList();
            oldList.add(saved);
            projectRepository.save(project);
        } catch (Exception e) {
            log.error("Error in creating a new sprint",e);
            throw new ErrorCreatingEntry("Cannot create a new sprint");
        }
    }

    public Sprint getSprintById(UUID sprintId) {
        return sprintRepository.findById(sprintId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find sprint with id : "+sprintId));
    }

    @Transactional
    public void updateSprint(UUID sprintId, SprintRequest sprintRequest) {
        Sprint dbSprint = sprintRepository.findById(sprintId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find sprint with id : "+sprintId));
        try{
            dbSprint.setSprintName(sprintRequest.getSprintName());
            dbSprint.setSprintGoal(sprintRequest.getSprintGoal());
            sprintRepository.save(dbSprint);
        } catch (Exception e){
            log.error("Error updating sprint with id : {}",sprintId);
            throw new ErrorUpdatingEntry("Cannot update sprint with id :"+sprintId);
        }
    }

    public void deleteSprint(UUID sprintId) {
        sprintRepository.findById(sprintId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find sprint with id : "+sprintId));
        try{
            sprintRepository.deleteById(sprintId);
        } catch (Exception e){
            log.error("Error deleting sprint with id : {}",sprintId);
            throw new ErrorDeletingEntry("Cannot delete sprint with id : "+sprintId);
        }
    }

    public List<Sprint> getSprintsByProjectId(UUID projectId) {
        try{
            List<Sprint> list = sprintRepository.findSprintByProjectId(projectId);
            if(list == null || list.isEmpty()) throw new RuntimeException();
            return list;
        } catch (Exception e){
            log.error("Cannot find projects for sprint with id : {}",projectId);
            throw new ResourceNotFoundException("Cannot find projects for sprint with id : "+projectId);
        }
    }
}
