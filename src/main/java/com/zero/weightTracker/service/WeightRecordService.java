package com.zero.weightTracker.service;

import com.zero.weightTracker.dto.request.NewWeightRecordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface WeightRecordService {
    ResponseEntity<?> createNewWeightRecord(HttpServletRequest http, NewWeightRecordRequest weightRecordRequest, BindingResult result, Long id);
    ResponseEntity<?> listWeightRecordOfGoal(HttpServletRequest http, Long idGoal, Pageable pageable);
    ResponseEntity<?> deleteWeightRecord(HttpServletRequest http, Long idWeightRecord);
}
