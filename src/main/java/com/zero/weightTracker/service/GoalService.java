package com.zero.weightTracker.service;

import com.zero.weightTracker.dto.request.NewGoalRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public interface GoalService {
    ResponseEntity<?> createNewGoal(HttpServletRequest http, NewGoalRequest newGoalRequest, BindingResult result);

    ResponseEntity<?> listAllGoalsForUsername(HttpServletRequest http, Pageable pageable);

    ResponseEntity<?> deleteGoal(HttpServletRequest http, Long id);

    ResponseEntity<?> editGoal(HttpServletRequest http, Long id, NewGoalRequest goalRequest, BindingResult result);
}