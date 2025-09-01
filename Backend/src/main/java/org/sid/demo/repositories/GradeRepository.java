package org.sid.demo.repositories;


import org.sid.demo.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    boolean existsBySubmissionId(Long submissionId);
    Optional<Grade> findBySubmissionId(Long submissionId);
    void deleteBySubmissionId(Long submissionId);
}
