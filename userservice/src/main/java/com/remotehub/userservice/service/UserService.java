package com.remotehub.userservice.service;

import com.remotehub.userservice.dto.mapper.TeamMapper;
import com.remotehub.userservice.dto.response.TeamResponseDto;
import com.remotehub.userservice.entity.TeamMemberships;
import com.remotehub.userservice.entity.Teams;
import com.remotehub.userservice.entity.User;
import com.remotehub.userservice.exceptions.ResourceNotFoundException;
import com.remotehub.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;

    public UserService(UserRepository userRepository, TeamMapper teamMapper){
        this.userRepository = userRepository;
        this.teamMapper = teamMapper;
    }

    @Transactional
    public List<TeamResponseDto> getUserTeams(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("Could not find user with id : "+userId));
        try{
            List<TeamMemberships> teamMemberships = user.getTeamMemberships();
            List<Teams> teamsList = new ArrayList<>();
            for(TeamMemberships memberShip:teamMemberships){
                teamsList.add(memberShip.getTeams());
            }
            List<TeamResponseDto> newList = new ArrayList<>();
            for(Teams team : teamsList){
                newList.add(teamMapper.toTeamResponseDto(team));
            }
            return newList;
        } catch (Exception e){
            log.error("Error in finding teams for user with id : {}",userId);
            throw new ResourceNotFoundException("Could not find teams for the user with id : "+userId);
        }
    }

    public List<TeamResponseDto> getTeams() {
        return new ArrayList<>();
    }
}
