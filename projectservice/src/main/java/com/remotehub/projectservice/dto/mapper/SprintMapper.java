package com.remotehub.projectservice.dto.mapper;

import com.remotehub.projectservice.dto.response.ProjectResponse;
import com.remotehub.projectservice.dto.response.SprintResponse;
import com.remotehub.projectservice.dto.response.TaskResponse;
import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.entity.Sprint;
import com.remotehub.projectservice.entity.Task;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface SprintMapper {

    @Mapping(target = "projectId" , source = "project.projectId")
    SprintResponse toSprintResponse(Sprint sprint);

    @Mapping(target = "projectId" , source = "project.projectId")
    @Mapping(target = "sprintId" , source = "sprint.sprintId")
    TaskResponse toTaskResponse(Task task);
    List<TaskResponse> toTaskResponseList(List<Task> task);
}
