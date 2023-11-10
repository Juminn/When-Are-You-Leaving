package com.enm.costcalculrator.service;

import com.enm.costcalculrator.data.dto.PathAndCostAndAnalysisDTO;
import com.enm.costcalculrator.data.dto.ScheduleDTO;


public interface CalculatorService {
    PathAndCostAndAnalysisDTO calculate(ScheduleDTO scheduleDTO);
}
