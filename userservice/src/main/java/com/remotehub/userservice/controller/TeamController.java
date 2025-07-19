package com.remotehub.userservice.controller;

import com.remotehub.userservice.dto.request.TeamRequest;
import com.remotehub.userservice.dto.response.MemberResponseDto;
import com.remotehub.userservice.dto.response.TeamResponseDto;
import com.remotehub.userservice.entity.Teams;
import com.remotehub.userservice.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> addNewTeam(@RequestBody TeamRequest request){
        teamService.addNewTeam(request);
        return ResponseEntity.status(201).body("Success");
    }

    @GetMapping("/exists/{teamId}")
    public ResponseEntity<Boolean> checkIfTeamExists(@PathVariable UUID teamId){
        boolean flag = teamService.getTeamById(teamId) != null;
        if(flag) return ResponseEntity.status(200).body(flag);
        else return ResponseEntity.status(404).body(flag);
    }


    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable UUID teamId){
        TeamResponseDto team = teamService.getTeamById(teamId);
        return ResponseEntity.status(200).body(team);
    }

    @GetMapping()
    public ResponseEntity<List<TeamResponseDto>> getAllTeams(){
        List<TeamResponseDto> list = teamService.getAllTeams();
        return ResponseEntity.status(200).body(list);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Teams> updateTeam(@PathVariable UUID teamId, @RequestBody TeamRequest teamRequest){
        Teams newTeam = teamService.updateTeam(teamId,teamRequest);
        return ResponseEntity.status(201).body(newTeam);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable UUID teamId){
        teamService.deleteTeam(teamId);
        return ResponseEntity.status(204).body("Success");
    }

    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<String> addMember(@PathVariable UUID teamId, @PathVariable UUID userId){
        teamService.addNewMember(teamId,userId);
        return ResponseEntity.status(201).body("Success");
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers(@PathVariable UUID teamId){
        List<MemberResponseDto> list = teamService.getMembers(teamId);
        return ResponseEntity.status(200).body(list);
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<String> deleteMember(@PathVariable UUID teamId , @PathVariable UUID userId){
        teamService.deleteMemberFromTeam(teamId,userId);
        return ResponseEntity.status(204).body("Success");
    }

}
