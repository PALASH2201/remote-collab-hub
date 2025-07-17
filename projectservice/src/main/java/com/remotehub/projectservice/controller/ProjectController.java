package com.remotehub.projectservice.controller;

import com.remotehub.projectservice.dto.request.ProjectRequest;
import com.remotehub.projectservice.dto.response.ProjectResponse;
import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addNewProject(@RequestBody ProjectRequest projectRequest){
        projectService.addNewProject(projectRequest);
        return ResponseEntity.status(201).body("Success");
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable UUID projectId){
        ProjectResponse projectResponse = projectService.getProjectById(projectId);
        return ResponseEntity.status(200).body(projectResponse);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable UUID projectId , @RequestBody ProjectRequest projectRequest){
        projectService.updateProject(projectId,projectRequest);
        return ResponseEntity.status(201).body("Success");
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable UUID projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.status(204).body("Success");
    }

    @GetMapping("/teams/{teamId}/projects")
    public ResponseEntity<List<ProjectResponse>> getProjects(@PathVariable UUID teamId){
        List<ProjectResponse> list = projectService.getProjectsByTeamId(teamId);
        return ResponseEntity.status(200).body(list);
    }
}
