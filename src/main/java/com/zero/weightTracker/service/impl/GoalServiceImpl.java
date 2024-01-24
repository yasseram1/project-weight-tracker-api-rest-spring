package com.zero.weightTracker.service.impl;

import com.zero.weightTracker.dto.request.NewGoalRequest;
import com.zero.weightTracker.entity.Goal;
import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.repository.GoalRepository;
import com.zero.weightTracker.repository.UserRepository;
import com.zero.weightTracker.service.GoalService;
import com.zero.weightTracker.service.JwtService;
import com.zero.weightTracker.util.Codes;
import com.zero.weightTracker.util.CustomResponses;
import com.zero.weightTracker.util.ErrorsUtil;
import com.zero.weightTracker.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
public class GoalServiceImpl implements GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomResponses customResponses;

    @Autowired
    private ErrorsUtil errorsUtil;

    @Autowired
    private UserUtil userUtil;

    @Override
    public ResponseEntity<?> createNewGoal(HttpServletRequest http, NewGoalRequest request, BindingResult result) {

        if(result.hasErrors()) {
            return customResponses.customErrorResponse(errorsUtil.getListErrors(result), "Error en los campos al crear nuevo objetivo de pérdida de peso.");
        }

        try {
            User user = userUtil.getAuthenticatedUserFromToken(http);

            Goal goal = Goal
                    .builder()
                    .startWeight(request.getStartWeight())
                    .goalWeight(request.getGoalWeight())
                    .startDate(request.getStartDate())
                    .user(user)
                    .build();

            goalRepository.save(goal);
            return customResponses.defaultResponse(Codes.SUCCESS.name(), "Nuevo objetivo de pérdida de peso creado con éxito.", HttpStatus.CREATED);
        } catch (Exception e) {
            return customResponses.customErrorResponse(List.of(e.getMessage()), "Ha ocurrido un error al crear nuevo objetivo de pérdida de peso");
        }

    }

    @Override
    public ResponseEntity<?> listAllGoalsForUsername(HttpServletRequest http, Pageable pageable) {

        try {
            User user = userUtil.getAuthenticatedUserFromToken(http);

            Page<Goal> goalsPage = goalRepository.findByUser(user, pageable);

            return new ResponseEntity<>(goalsPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return customResponses.customErrorResponse(List.of(e.getMessage()), "Ha ocurrido un error al crear nuevo objetivo de pérdida de peso");
        }
    }

    @Override
    public ResponseEntity<?> deleteGoal(HttpServletRequest http, Long id) {
        try {
            User user = userUtil.getAuthenticatedUserFromToken(http);

            Optional<Goal> optionalGoal = goalRepository.findById(id);

            if(optionalGoal.isPresent()) {
                Goal goal = optionalGoal.get();

                if(Objects.equals(goal.getUser().getId(), user.getId())) {
                    goalRepository.delete(goal);
                    return new ResponseEntity<>("El objetivo de pérdida de peso con id " + id + " ha sido eliminado" , HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No tienes permisos para eliminar este objetivo de pérdida de peso" , HttpStatus.UNAUTHORIZED);
                }

            }

            return new ResponseEntity<>("El objetivo de perdida de peso con el id " + id + " no existe", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return customResponses.customErrorResponse(List.of(e.getMessage()), "Ha ocurrido un error al crear nuevo objetivo de pérdida de peso");
        }
    }

    @Override
    public ResponseEntity<?> editGoal(HttpServletRequest http, Long id, NewGoalRequest goalRequest, BindingResult result) {

        if(result.hasErrors()) {
            return customResponses.customErrorResponse(errorsUtil.getListErrors(result), "Error en los campos al crear nuevo objetivo de pérdida de peso.");
        }

        try {
            User user = userUtil.getAuthenticatedUserFromToken(http);

            Optional<Goal> optionalGoal = goalRepository.findById(id);

            if(optionalGoal.isPresent()) {
                Goal goal = optionalGoal.get();

                if(Objects.equals(goal.getUser().getId(), user.getId())) {

                    goal.setGoalWeight(goalRequest.getGoalWeight());
                    goal.setStartWeight(goalRequest.getStartWeight());
                    goal.setStartDate(goalRequest.getStartDate());

                    goalRepository.save(goal);
                    return new ResponseEntity<>("El objetivo de pérdida de peso con id " + id + " ha sido editado" , HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("No tienes permisos para editar este objetivo de pérdida de peso" , HttpStatus.UNAUTHORIZED);
                }

            }

            return new ResponseEntity<>("El objetivo de perdida de peso con el id " + id + " no existe", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return customResponses.customErrorResponse(List.of(e.getMessage()), "Ha ocurrido un error al editar nuevo objetivo de pérdida de peso");
        }
    }


}
