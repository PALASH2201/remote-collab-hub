package com.remotehub.userservice.repository;

import com.remotehub.userservice.entity.TeamMemberships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamMembershipsRepository extends JpaRepository<TeamMemberships, UUID> {

    @Query(value = "SELECT t.team_id FROM team_memberships t WHERE t.user_id=:userId",nativeQuery = true)
    List<UUID> findTeamsByTeamId(UUID userId);
}
