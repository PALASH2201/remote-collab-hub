package com.remotehub.projectservice.controller;

import com.remotehub.projectservice.dto.request.SprintRequest;
import com.remotehub.projectservice.entity.Sprint;
import com.remotehub.projectservice.service.SprintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sprint")
public class SprintController {
    private final SprintService sprintService;

    public SprintController(SprintService sprintService){
        this.sprintService = sprintService;
    }

    @PostMapping("/projects/{projectId}/sprint")
    public ResponseEntity<String> addNewSprint(@PathVariable UUID projectId , @RequestBody SprintRequest sprintRequest){
        sprintService.addNewSprint(projectId,sprintRequest);
        return ResponseEntity.status(201).body("Success");
    }

    @GetMapping("/{sprintId}")
    public ResponseEntity<Sprint> getSprint(@PathVariable UUID sprintId){
        Sprint sprint = sprintService.getSprintById(sprintId);
        return ResponseEntity.status(200).body(sprint);
    }

    @PutMapping("/{sprintId}")
    public ResponseEntity<String> updateSprint(@PathVariable UUID sprintId , @RequestBody SprintRequest sprintRequest){
        sprintService.updateSprint(sprintId,sprintRequest);
        return ResponseEntity.status(201).body("Success");
    }

    @DeleteMapping("/{sprintId}")
    public ResponseEntity<String> deleteSprint(@PathVariable UUID sprintId){
        sprintService.deleteSprint(sprintId);
        return ResponseEntity.status(204).body("Success");
    }

    @GetMapping("/projects/{projectId}/sprints")
    public ResponseEntity<List<Sprint>> getSprints(@PathVariable UUID projectId){
        List<Sprint> list = sprintService.getSprintsByProjectId(projectId);
        return ResponseEntity.status(200).body(list);
    }
}
