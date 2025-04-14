package com.remotehub.projectservice.dto;

import com.remotehub.projectservice.dto.response.ProjectResponse;
import com.remotehub.projectservice.entity.Project;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);
}
