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

import java.util.*;
import java.util.logging.Logger;

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

    @Override
    public ResponseEntity<?> listWeightRecordOfGoal(HttpServletRequest http, Long idGoal, Pageable pageable) {

        try {

            Optional<Goal> goalOptional = goalRepository.findById(idGoal);

            if(goalOptional.isPresent()) { // Si la meta existe entonces

                User user = userUtil.getAuthenticatedUserFromToken(http);
                Goal goal = goalOptional.get();

                // Verificamos si la meta le pertecene al usuario del token
                if(Objects.equals(goal.getUser().getId(), user.getId())) {

                    // Como la meta si pertenece al usuario, obtenemos todas las marcas de peso relacionadas a esa meta
                    Page<WeightRecord> listWeightRecordForGoal = weightRecordRepository.findByGoal(goal, pageable);
                    return new ResponseEntity<>(listWeightRecordForGoal.getContent() ,HttpStatus.OK);

                } else {
                    return new ResponseEntity<>("No tienes permisos para acceder a la meta con id : " + idGoal,HttpStatus.BAD_REQUEST);
                }


            } else {
                Map<String, String> map = new HashMap<>();
                map.put("error", "La meta a la que intentas acceder no existe");
                return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            return new ResponseEntity<>("Ha ocurrido un error inesperado",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
