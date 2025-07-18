package com.remotehub.projectservice.controller;

import com.remotehub.projectservice.dto.request.SprintRequest;
import com.remotehub.projectservice.dto.response.SprintResponse;
import com.remotehub.projectservice.service.SprintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sprint")
public class SprintController {
    private final SprintService sprintService;
    private static final String SUCCESS_MSG = "Success";

    public SprintController(SprintService sprintService){
        this.sprintService = sprintService;
    }

    @PostMapping("/projects/{projectId}/sprint")
    public ResponseEntity<String> addNewSprint(@PathVariable UUID projectId , @RequestBody SprintRequest sprintRequest){
        sprintService.addNewSprint(projectId,sprintRequest);
        return ResponseEntity.status(201).body(SUCCESS_MSG);
    }

    @GetMapping("/{sprintId}")
    public ResponseEntity<SprintResponse> getSprint(@PathVariable UUID sprintId){
        SprintResponse sprint = sprintService.getSprintById(sprintId);
        return ResponseEntity.status(200).body(sprint);
    }

    @PutMapping("/{sprintId}")
    public ResponseEntity<String> updateSprint(@PathVariable UUID sprintId , @RequestBody SprintRequest sprintRequest){
        sprintService.updateSprint(sprintId,sprintRequest);
        return ResponseEntity.status(201).body(SUCCESS_MSG);
    }

    @DeleteMapping("/{sprintId}")
    public ResponseEntity<String> deleteSprint(@PathVariable UUID sprintId){
        sprintService.deleteSprint(sprintId);
        return ResponseEntity.status(204).body(SUCCESS_MSG);
    }

    @GetMapping("/projects/{projectId}/sprints")
    public ResponseEntity<List<SprintResponse>> getSprints(@PathVariable UUID projectId){
        List<SprintResponse> list = sprintService.getSprintsByProjectId(projectId);
        return ResponseEntity.status(200).body(list);
    }
}
