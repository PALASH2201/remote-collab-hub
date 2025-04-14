package com.remotehub.projectservice.repository;

import com.remotehub.projectservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query(value = "SELECT * FROM Project p WHERE p.team_id=:teamId ",nativeQuery = true)
    List<Project> findProjectByTeamId(UUID teamId);
}
