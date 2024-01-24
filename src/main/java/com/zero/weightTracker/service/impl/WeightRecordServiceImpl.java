package com.zero.weightTracker.service.impl;

import com.zero.weightTracker.dto.request.NewWeightRecordRequest;
import com.zero.weightTracker.entity.Goal;
import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.entity.WeightRecord;
import com.zero.weightTracker.repository.GoalRepository;
import com.zero.weightTracker.repository.WeightRecordRepository;
import com.zero.weightTracker.service.WeightRecordService;
import com.zero.weightTracker.util.Codes;
import com.zero.weightTracker.util.CustomResponses;
import com.zero.weightTracker.util.ErrorsUtil;
import com.zero.weightTracker.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WeightRecordServiceImpl implements WeightRecordService {

    @Autowired
    private WeightRecordRepository weightRecordRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private CustomResponses customResponses;

    @Autowired
    private ErrorsUtil errorsUtil;

    @Override
    public ResponseEntity<?> createNewWeightRecord(HttpServletRequest http, NewWeightRecordRequest weightRecordRequest, BindingResult result, Long id) {

        if(result.hasErrors()) {
            return customResponses.customErrorResponse(errorsUtil.getListErrors(result), "Error en los campos al crear nuevo registro de peso.");
        }

        try {
            Optional<Goal> goalOptional = goalRepository.findById(id);

            if(goalOptional.isPresent()) {

                Goal goal = goalOptional.get();
                User user = userUtil.getAuthenticatedUserFromToken(http);

                if(Objects.equals(goal.getUser().getId(), user.getId())) {
                    WeightRecord weightRecord = WeightRecord.builder()
                            .weight(weightRecordRequest.getWeight())
                            .recordDate(weightRecordRequest.getRecordDate())
                            .goal(goal)
                            .build();

                    weightRecordRepository.save(weightRecord);

                    return new ResponseEntity<>("Registro de peso creado.", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("No tienes permisos para crear este registro de peso", HttpStatus.UNAUTHORIZED);
                }

            }

            return new ResponseEntity<>("El registro de peso con el id " + id + " no existe", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return customResponses.customErrorResponse(List.of(e.getMessage()), "Ha ocurrido un error al crear nuevo objetivo de pérdida de peso");
        }
    }
}
