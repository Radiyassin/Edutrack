package org.sid.demo.repositories;


import org.sid.demo.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByProjectId(Long projectId);
    Optional<Submission> findByProjectId(Long projectId);
    void deleteByProjectId(Long projectId);
}
