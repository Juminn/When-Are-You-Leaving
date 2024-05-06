package com.enm.costcalculrator.service.Survey;


import com.enm.costcalculrator.data.Survey.SurveyRequestDTO;
import com.enm.costcalculrator.data.Survey.SurveyResponseDTO;

public interface SurveyService {

    SurveyResponseDTO makeNextQuestion(SurveyRequestDTO surveyRequestDTO);
}
