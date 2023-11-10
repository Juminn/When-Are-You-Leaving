package com.enm.costcalculrator.controller;

import com.enm.costcalculrator.data.dto.PathAndCostAndAnalysisDTO;
import com.enm.costcalculrator.data.dto.ScheduleDTO;
import com.enm.costcalculrator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Main {

    private final CalculatorService calculatorService;

    @Autowired
    public Main(CalculatorService calculatorService){
        this.calculatorService = calculatorService;
    }

    @GetMapping(value = "/")
    public String test(){
        return "hello";
    }
     
    @GetMapping(value = "/a")
    public PathAndCostAndAnalysisDTO test2(ScheduleDTO scheduleDTO)
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

        PathAndCostAndAnalysisDTO result = calculatorService.calculate(scheduleDTO);

        return result;
    }

}
