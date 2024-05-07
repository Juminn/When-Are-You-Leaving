package com.enm.costcalculrator.controller;

import com.enm.costcalculrator.data.Survey.SurveyRequestDTO;
import com.enm.costcalculrator.data.Survey.SurveyResponseDTO;
import com.enm.costcalculrator.data.dto.PathAndCostAndAnalysisDTO;
import com.enm.costcalculrator.data.dto.ScheduleDTO;
import com.enm.costcalculrator.service.CalculatorService;
import com.enm.costcalculrator.service.Survey.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class Main {

    private final CalculatorService calculatorService;
    private final SurveyService surveyService;

    @Autowired
    public Main(CalculatorService calculatorService, SurveyService surveyService){
        this.calculatorService = calculatorService;
        this.surveyService = surveyService;
    }

    @GetMapping(value = "/")
    public String test(){
        return "hello";
    }
     
    @GetMapping(value = "/a")
    public ResponseEntity<?> test2(ScheduleDTO scheduleDTO)
    {
        //임시
        if(scheduleDTO.getGoalY() == null) {
            scheduleDTO.setStartX("126.79758700000022");
            scheduleDTO.setStartY("37.546016099999925");
            scheduleDTO.setGoalX("126.73706789999993");
            scheduleDTO.setGoalY("37.54487940000018");
            scheduleDTO.setStartTime("2023-09-18T18:00:00");
            scheduleDTO.setEndTime("2023-09-18T19:00:00");
        }

        System.out.println("요청 start time:  " + LocalDateTime.now());
        System.out.println("controlloer schedulDTO: " +scheduleDTO);
        PathAndCostAndAnalysisDTO result = calculatorService.calculate(scheduleDTO);

        System.out.println("요청 end time:  " + LocalDateTime.now());
        try {
            return ResponseEntity.ok(calculatorService.calculate(scheduleDTO));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        }
    }

    @PostMapping(value =  "/survey/nextQuestion")
    public ResponseEntity<?> createNextQuestion(@RequestBody SurveyRequestDTO surveyRequestDTO){

        System.out.println("survey/nextQuestion Request:" +surveyRequestDTO);
        //임시
        if(surveyRequestDTO.getOptions() == null){
            surveyRequestDTO = new SurveyRequestDTO();
            surveyRequestDTO.setQuestionIndex(0);
        }
        System.out.println(surveyRequestDTO);

        try {
            SurveyResponseDTO response = surveyService.makeNextQuestion(surveyRequestDTO);
            System.out.println("response: " + response);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        }
    }

    @PostMapping(value =  "/survey/result")
    public ResponseEntity<?> createSurveyResult(@RequestBody SurveyRequestDTO surveyRequestDTO){

        System.out.println("survey/result Request:" +surveyRequestDTO);
        //임시
        if(surveyRequestDTO.getOptions() == null){
            surveyRequestDTO = new SurveyRequestDTO();
            surveyRequestDTO.setQuestionIndex(0);
        }
        System.out.println(surveyRequestDTO);

        try {
            SurveyResponseDTO response = surveyService.makeNextQuestion(surveyRequestDTO);
            System.out.println("response: " + response);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        }
    }


}
