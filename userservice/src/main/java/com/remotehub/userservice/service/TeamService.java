package com.remotehub.userservice.service;

import com.remotehub.userservice.dto.mapper.MemberMapper;
import com.remotehub.userservice.dto.mapper.TeamMapper;
import com.remotehub.userservice.dto.mapper.UserMapper;
import com.remotehub.userservice.dto.request.TeamRequest;
import com.remotehub.userservice.dto.response.MemberResponseDto;
import com.remotehub.userservice.dto.response.TeamResponseDto;
import com.remotehub.userservice.dto.response.UserResponseDto;
import com.remotehub.userservice.entity.TeamMemberships;
import com.remotehub.userservice.entity.Teams;
import com.remotehub.userservice.entity.User;
import com.remotehub.userservice.exceptions.ErrorCreatingEntry;
import com.remotehub.userservice.exceptions.ErrorDeletingEntry;
import com.remotehub.userservice.exceptions.ErrorUpdatingEntry;
import com.remotehub.userservice.exceptions.ResourceNotFoundException;
import com.remotehub.userservice.repository.TeamMembershipsRepository;
import com.remotehub.userservice.repository.TeamsRepository;
import com.remotehub.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TeamService {

    private final TeamsRepository teamsRepository;
    private final TeamMembershipsRepository teamMembershipsRepository;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;
    private final MemberMapper memberMapper;
    private final UserMapper userMapper;
    private static final String TEAM_NOT_FOUND_MESSAGE = "Could not find team with id : {}";

    public TeamService(TeamsRepository teamsRepository, TeamMembershipsRepository teamMembershipsRepository, UserRepository userRepository, TeamMapper teamMapper, MemberMapper memberMapper, UserMapper userMapper){
        this.teamsRepository = teamsRepository;
        this.teamMembershipsRepository = teamMembershipsRepository;
        this.userRepository = userRepository;
        this.teamMapper = teamMapper;
        this.memberMapper = memberMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public void addNewTeam(TeamRequest request) {
        User user = userRepository.findById(request.getCreatedBy())
                .orElseThrow(()->{
                    log.error("Could not find user with id : {}",request.getCreatedBy());
                    return new ResourceNotFoundException("Could not find user with id : "+request.getCreatedBy());
                });
        try{
            Teams teams = new Teams();
            teams.setTeamName(request.getTeamName());
            teams.setTeamDesc(request.getTeamDesc());
            teams.setCreatedBy(user);
            teams.setCreatedAt(LocalDateTime.now());
            teams.setUpdatedAt(LocalDateTime.now());
            teams.setTeamMemberships(new ArrayList<>());
            Teams saved = teamsRepository.save(teams);

            TeamMemberships teamMembership = new TeamMemberships();
            teamMembership.setUser(user);
            teamMembership.setTeams(saved);
            teamMembership.setRole("ADMIN");
            teamMembership.setJoinedAt(LocalDateTime.now());
            TeamMemberships saved2 = teamMembershipsRepository.save(teamMembership);

            List<Teams> teamList = user.getCreatedTeams();
            teamList.add(saved);
            List<TeamMemberships> membershipsList = user.getTeamMemberships();
            membershipsList.add(saved2);

            userRepository.save(user);
        }
        catch(DataAccessException e){
            log.error("Error creating new team with name!{}",request.getTeamName());
            throw new ErrorCreatingEntry("Error creating new team with name "+request.getTeamName());
        }
    }

    @Transactional
    public TeamResponseDto getTeamById(UUID teamId) {
        Teams teams = teamsRepository.findById(teamId)
                .orElseThrow(() -> {
                    log.error(TEAM_NOT_FOUND_MESSAGE, teamId);
                    return new ResourceNotFoundException("Could not find team with id: " + teamId);
                });
        return teamMapper.toTeamResponseDto(teams);
    }

    @Transactional
    public List<TeamResponseDto> getAllTeams() {
        List<Teams> teams = teamsRepository.findAll();
        if (teams.isEmpty()) {
            log.error("Teams not found");
            throw new ResourceNotFoundException("Teams not found");
        }
        List<TeamResponseDto> list = new ArrayList<>();
        for(Teams t : teams){
            list.add(teamMapper.toTeamResponseDto(t));
        }
        return list;
    }

    @Transactional
    public Teams updateTeam(UUID teamId,TeamRequest request) {
        Teams oldTeam = teamsRepository.findById(teamId)
                .orElseThrow(() -> {
                   log.error(TEAM_NOT_FOUND_MESSAGE,teamId);
                   return new ResourceNotFoundException("Could not find team with id:"+teamId);
                });
        try{
            oldTeam.setTeamName(request.getTeamName());
            oldTeam.setTeamDesc(request.getTeamDesc());
            oldTeam.setCreatedBy(oldTeam.getCreatedBy());
            oldTeam.setUpdatedAt(LocalDateTime.now());
            teamsRepository.save(oldTeam);
            return teamsRepository.findById(teamId).get();
        } catch (Exception e){
            log.error("Could not update team with id : {}",teamId);
            throw new ErrorUpdatingEntry("Could not update team with id : "+teamId);
        }
    }

    @Transactional
    public void deleteTeam(UUID teamId) {
        teamsRepository.findById(teamId)
                .orElseThrow(()-> {
                    log.error(TEAM_NOT_FOUND_MESSAGE,teamId);
                    return new ResourceNotFoundException("Could not find team with id "+teamId);
                });
        try{
            teamsRepository.deleteById(teamId);
        } catch (Exception e){
            log.error("Error deleting team with id : {}",teamId);
            throw new ErrorDeletingEntry("Failed in deleting team with id : "+teamId);
        }
    }

    @Transactional
    public void addNewMember(UUID teamId, UUID userId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find team with id : "+teamId));
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find user with id : "+userId));
        try{
            TeamMemberships membership = new TeamMemberships();
            membership.setUser(user);
            membership.setTeams(team);
            membership.setRole("MEMBER");
            membership.setJoinedAt(LocalDateTime.now());
            TeamMemberships saved = teamMembershipsRepository.save(membership);

            List<TeamMemberships> membersList = team.getTeamMemberships();
            membersList.add(saved);
            teamsRepository.save(team);

            List<TeamMemberships> teamMemberships = user.getTeamMemberships();
            teamMemberships.add(saved);
            userRepository.save(user);
        } catch (Exception e){
            log.error("Could not add new team member with userId : {}",userId);
            throw new ErrorCreatingEntry("Could not add new team member");
        }
    }

    @Transactional
    public List<MemberResponseDto> getMembers(UUID teamId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find team with id : "+teamId));
        try{
            List<TeamMemberships> memberShips = team.getTeamMemberships();
            List<UserResponseDto> members = new ArrayList<>();
            for(TeamMemberships memberships : memberShips){
                members.add(userMapper.toUserResponseDto(memberships.getUser()));
            }
            List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();
            for(UserResponseDto member : members){
                memberResponseDtoList.add(memberMapper.toMemberResponseDto(member));
            }
            return memberResponseDtoList;
        } catch (Exception e) {
            log.error("Cannot find team members : {}",e.getMessage());
            throw new ResourceNotFoundException("Cannot find team members");
        }
    }

    @Transactional
    public void deleteMemberFromTeam(UUID teamId, UUID userId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find team with id : "+teamId));
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("Cannot find user with id : "+userId));
        try{
            UUID teamMemberShipID = null;
            TeamMemberships membership = null;
            List<TeamMemberships> teamMembers = team.getTeamMemberships();
            List<TeamMemberships> userTeams = user.getTeamMemberships();
            for(TeamMemberships member : teamMembers){
                User dbUser = member.getUser();
                if(dbUser.getUserId() == userId){
                    membership = member;
                    teamMemberShipID = member.getMembershipId();
                    break;
                }
            }
            if(teamMemberShipID == null) throw new ResourceNotFoundException("Membership not found");
            teamMembershipsRepository.deleteById(teamMemberShipID);

            teamMembers.remove(membership);
            userTeams.remove(membership);

        } catch (Exception e){
            log.error("Could not delete team member with userId : {}",userId);
            throw new ErrorDeletingEntry("Could not delete team member with id : "+userId);
        }
    }
}
