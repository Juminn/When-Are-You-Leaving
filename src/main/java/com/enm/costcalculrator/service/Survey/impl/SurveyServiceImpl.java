package com.enm.costcalculrator.service.Survey.impl;

import com.enm.costcalculrator.data.Survey.*;
import com.enm.costcalculrator.service.Survey.SurveyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    final int middleDuration = 30;
    public SurveyResponseDTO makeNextQuestion(SurveyRequestDTO surveyRequestDTO) {

        if(isFirstQuestion(surveyRequestDTO)){
            //retunr 첫질문
            return getNthQuestion(1);
        }

        int nowSelectedOption = surveyRequestDTO.getSelectedOption();
        if(isFollowUpQuestion(surveyRequestDTO) && firstSelectedOption(surveyRequestDTO) != nowSelectedOption){
            //return 다음질문
            return getNthQuestion(surveyRequestDTO.getQuestionIndex() + 1);
        }
        else{
            //return 추적질문
            return FollowUpQuestion(surveyRequestDTO);
        }

    }


    public SurveyResultResponseDTO makeResult(SurveyResultRequestDTO surveyResultRequestDTO) {

        return null;
    }

    private boolean isFirstQuestion(SurveyRequestDTO surveyRequestDTO){
        if(surveyRequestDTO.getQuestionIndex() == 0) {
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isFollowUpQuestion(SurveyRequestDTO surveyRequestDTO){
        if(surveyRequestDTO.getFollowUpQuestionIndex() != 0){
            return true;
        }
        else {
            return false;
        }
    }

    private int firstSelectedOption(SurveyRequestDTO surveyRequestDTO){
        int opt1Duration = surveyRequestDTO.getDurationOfOption(1);

        if(opt1Duration < middleDuration){
            return 0;
        } else if (opt1Duration > middleDuration ) {
            return 1;
        }
        else {
            throw new RuntimeException();
        }
    }

    private SurveyResponseDTO getNthQuestion(int n){
        if(n == 1) {
            List<OptionDTO> options = Arrays.asList(
                    new OptionDTO("Walking", middleDuration),
                    new OptionDTO("Bus", middleDuration)
            );
            return new SurveyResponseDTO(1, 0, options, false);
        }
        else if(n==2){
            List<OptionDTO> options = Arrays.asList(
                    new OptionDTO("Walking", middleDuration),
                    new OptionDTO("Subway", middleDuration)
            );
            return new SurveyResponseDTO(2, 0, options, false);
        }
        else if(n==3){
            List<OptionDTO> options = Arrays.asList(
                    new OptionDTO("Walking", middleDuration),
                    new OptionDTO("Transfer", middleDuration)
            );
            return new SurveyResponseDTO(3, 0, options, false);
        }
        else if(n==4){
            return new SurveyResponseDTO(4, 0, null, true);
        }

        throw new RuntimeException();
    }

    private SurveyResponseDTO FollowUpQuestion(SurveyRequestDTO surveyRequestDTO){

        //요청 시간 계산
        int Option1Duration;

        if(surveyRequestDTO.getSelectedOption()==0){
            Option1Duration = surveyRequestDTO.getDurationOfOption(1) -5;
        } else if (surveyRequestDTO.getSelectedOption()==1) {
            Option1Duration = surveyRequestDTO.getDurationOfOption(1) +5;
        }
        else{
            throw  new RuntimeException("func: firstQuestion , SelectedOption != 1 or 2");
        }

        //응답 객체만들기
        SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setQuestionIndex(surveyRequestDTO.getQuestionIndex());
        surveyResponseDTO.setFollowUpQuestionIndex(surveyRequestDTO.getFollowUpQuestionIndex() + 1);

        List<OptionDTO> newOptions = new ArrayList<>(surveyRequestDTO.getOptions());
        if (newOptions.size() > 1) {
            OptionDTO option1 = newOptions.get(1);
            int currentDuration = option1.getDuration();
            option1.setDuration(Option1Duration);
        }
        surveyResponseDTO.setOptions(newOptions);

        return surveyResponseDTO;
    }
}
