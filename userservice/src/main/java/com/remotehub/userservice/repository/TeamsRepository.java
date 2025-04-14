package com.remotehub.userservice.repository;

import com.remotehub.userservice.entity.Teams;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, UUID> {
    @EntityGraph(attributePaths = {"teamMemberships", "createdBy"})
    Optional<Teams> findById(UUID userId);
}
