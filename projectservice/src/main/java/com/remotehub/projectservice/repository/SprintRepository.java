package com.remotehub.projectservice.repository;

import com.remotehub.projectservice.entity.Project;
import com.remotehub.projectservice.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, UUID> {
    @Query(value = "SELECT * FROM Sprint s WHERE s.project_id=:projectId ",nativeQuery = true)
    List<Sprint> findSprintByProjectId(UUID projectId);
}
