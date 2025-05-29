package com.remotehub.projectservice.controller;

import com.remotehub.projectservice.dto.request.TaskRequest;
import com.remotehub.projectservice.dto.response.TaskResponse;
import com.remotehub.projectservice.entity.Task;
import com.remotehub.projectservice.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping("/projects/{projectId}/task")
    public ResponseEntity<String> addNewTask(@PathVariable UUID projectId , @RequestBody TaskRequest taskRequest){
        taskService.addNewTask(projectId,taskRequest);
        return ResponseEntity.status(201).body("Success");
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID taskId){
        TaskResponse task = taskService.getTaskById(taskId);
        return ResponseEntity.status(200).body(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable UUID taskId , @RequestBody TaskRequest taskRequest){
        taskService.updateTask(taskId,taskRequest);
        return ResponseEntity.status(201).body("Success");
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.status(204).body("Success");
    }

    @PostMapping("sprint/{sprintId}/task/{taskId}")
    public ResponseEntity<String> assignTask(@PathVariable UUID sprintId,@PathVariable UUID taskId){
        taskService.assignTask(sprintId,taskId);
        return ResponseEntity.status(200).body("Success");
    }
}
