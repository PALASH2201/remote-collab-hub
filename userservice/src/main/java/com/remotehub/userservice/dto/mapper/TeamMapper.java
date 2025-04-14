package com.remotehub.userservice.dto.mapper;

import com.remotehub.userservice.dto.response.TeamMembershipResponseDto;
import com.remotehub.userservice.dto.response.TeamResponseDto;
import com.remotehub.userservice.entity.TeamMemberships;
import com.remotehub.userservice.entity.Teams;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" ,builder = @Builder(disableBuilder = true))
public interface TeamMapper {

    @Mapping(target = "createdBy", source = "createdBy.userId")
    TeamResponseDto toTeamResponseDto(Teams team);

    @Mapping(target = "userId" , source = "user.userId")
    @Mapping(target = "teamId" , source = "teams.teamId")
    TeamMembershipResponseDto toTeamMembershipResponseDto(TeamMemberships teamMemberships);

    List<TeamMembershipResponseDto> toMembershipDtoList(List<TeamMemberships> memberships);
}