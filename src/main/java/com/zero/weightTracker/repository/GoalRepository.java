package com.zero.weightTracker.repository;

import com.zero.weightTracker.entity.Goal;
import com.zero.weightTracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findByUser(User user, Pageable pageable);
}
