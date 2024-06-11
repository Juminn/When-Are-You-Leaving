package com.enm.costcalculrator.service.Survey;


import com.enm.costcalculrator.data.Survey.SurveyRequestDTO;
import com.enm.costcalculrator.data.Survey.SurveyResponseDTO;
import com.enm.costcalculrator.data.Survey.SurveyResultRequestDTO;
import com.enm.costcalculrator.data.Survey.SurveyResultResponseDTO;

public interface SurveyService {

    SurveyResponseDTO makeNextQuestion(SurveyRequestDTO surveyRequestDTO);
    SurveyResultResponseDTO makeResult(SurveyResultRequestDTO surveyResultRequestDTO);
}
