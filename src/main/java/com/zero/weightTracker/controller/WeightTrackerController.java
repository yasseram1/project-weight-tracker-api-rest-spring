package com.zero.weightTracker.controller;

import com.zero.weightTracker.dto.request.NewGoalRequest;
import com.zero.weightTracker.dto.request.NewWeightRecordRequest;
import com.zero.weightTracker.entity.WeightRecord;
import com.zero.weightTracker.service.GoalService;
import com.zero.weightTracker.service.WeightRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/api/v1/weightTracker")
public class WeightTrackerController {

    @Autowired
    private GoalService goalService;

    @Autowired
    private WeightRecordService weightRecordService;

    @PostMapping("/createNewGoal")
    public ResponseEntity<?> createNewGoal(HttpServletRequest http, @Valid @RequestBody NewGoalRequest newGoalRequest, BindingResult result) {
        return goalService.createNewGoal(http, newGoalRequest, result);
    }

    @GetMapping("/listAllGoalsForUser")
    public ResponseEntity<?> listAllGoalsForUser(HttpServletRequest http, Pageable pageable) {
        return goalService.listAllGoalsForUsername(http, pageable);
    }

    @DeleteMapping("/deleteGoal/{id}")
    public ResponseEntity<?> deleteGoal(HttpServletRequest http, @PathVariable(name = "id") Long id) {
        return goalService.deleteGoal(http, id);
    }

    @PutMapping("/editGoal/{id}")
    public ResponseEntity<?> editGoal(HttpServletRequest http, @PathVariable(name = "id") Long id, @Valid @RequestBody NewGoalRequest goalRequest, BindingResult result) {
        return goalService.editGoal(http, id, goalRequest, result);
    }

    // WeightRecord EndPoints
    @PostMapping("/createNewWeightRecord/{id}")
    public ResponseEntity<?> createNewWeightRecord(HttpServletRequest http, @Valid @RequestBody NewWeightRecordRequest weightRecordRequest, BindingResult result, @PathVariable(name = "id") Long goalId) {
        return weightRecordService.createNewWeightRecord(http, weightRecordRequest, result, goalId);
    }


    @GetMapping("/listWeightRecordOfGoal/{id}")
    public ResponseEntity<?> listWeightRecordOfGoal(HttpServletRequest http, @PathVariable(name = "id") Long idGoal, Pageable pageable) {
        return weightRecordService.listWeightRecordOfGoal(http, idGoal, pageable);
    }

    @DeleteMapping("/deleteWeightRecord/{id}")
    public ResponseEntity<?> deleteWeightRecord(HttpServletRequest http ,@PathVariable(name = "id") Long idWeightRecord) {
        return weightRecordService.deleteWeightRecord(http, idWeightRecord);
    }


}
