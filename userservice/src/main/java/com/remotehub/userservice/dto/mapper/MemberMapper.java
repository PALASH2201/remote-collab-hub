package com.remotehub.userservice.dto.mapper;

import com.remotehub.userservice.dto.response.MemberResponseDto;
import com.remotehub.userservice.dto.response.TeamMembershipResponseDto;
import com.remotehub.userservice.dto.response.UserResponseDto;
import com.remotehub.userservice.entity.TeamMemberships;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" , builder = @Builder(disableBuilder = true))
public interface MemberMapper {
    MemberResponseDto toMemberResponseDto(UserResponseDto userResponseDto);
    List<TeamMembershipResponseDto> toMembershipDtoList(List<TeamMemberships> memberships);
}
