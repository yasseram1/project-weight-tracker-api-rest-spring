package com.zero.weightTracker.repository;

import com.zero.weightTracker.entity.Goal;
import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.entity.WeightRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

    Page<WeightRecord> findByGoal(Goal goal, Pageable pageable);

}
