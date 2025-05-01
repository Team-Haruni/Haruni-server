package org.haruni.domain.feedback.repository;

import org.haruni.domain.feedback.entity.WeeklyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeeklyFeedbackRepository extends JpaRepository<WeeklyFeedback, Long> {

    Optional<WeeklyFeedback> findTopByUserIdOrderByIdDesc(Long userId);
}
