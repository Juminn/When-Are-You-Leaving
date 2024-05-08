package com.enm.costcalculrator.service.Survey.impl;

import com.enm.costcalculrator.data.Survey.*;
import com.enm.costcalculrator.service.Survey.SurveyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    final int initTrasnPortDuration = 30;
    final int initTransferNum = 2;

    final int transportDurationChange = 5;
    final int transperDuraitonChange = 1;


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

        if(opt1Duration < initTrasnPortDuration){
            return 0;
        } else if (opt1Duration > initTrasnPortDuration) {
            return 1;
        }
        else {
            throw new RuntimeException();
        }
    }

    private SurveyResponseDTO getNthQuestion(int n){
        if(n == 1) {
            List<OptionDTO> options = Arrays.asList(
                    new OptionDTO("Walking", initTrasnPortDuration),
                    new OptionDTO("Bus", initTrasnPortDuration)
            );
            return new SurveyResponseDTO(1, 0, options, false);
        }
        else if(n==2){
            List<OptionDTO> options = Arrays.asList(
                    new OptionDTO("Walking", initTrasnPortDuration),
                    new OptionDTO("Subway", initTrasnPortDuration)
            );
            return new SurveyResponseDTO(2, 0, options, false);
        }
        else if(n==3){
            List<OptionDTO> options = Arrays.asList(
                    new OptionDTO("Walking", initTrasnPortDuration),
                    new OptionDTO("Transfer", initTransferNum)
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

        int durationChange = surveyRequestDTO.getTransportOfOption(1).equals("Transfer") ? transperDuraitonChange : transportDurationChange;

        if(surveyRequestDTO.getSelectedOption()==0){
            Option1Duration = surveyRequestDTO.getDurationOfOption(1) - durationChange;
        } else if (surveyRequestDTO.getSelectedOption()==1) {
            Option1Duration = surveyRequestDTO.getDurationOfOption(1) + durationChange;
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

    //result관련 함수 시작
//
//    Null 29.99``` 50 80  3회
//
//    고르면 그 교통이 커진다. 즉 값이 클수록  좋은거잖아
//    Walking 29.999분 = subway 60분= Bus 15분 = 환승 2회
//
//
//    위의 경우 Bus > Subway > Walking 이고 환승 은 n번 평균을 기준으로 뭔가 추가하자
//
//    리턴값은 MBTI와
//    Mbti는 walking 29.99로하고 큰순서대로 반환
//    Walking은 14000 고정에
//            Subway = 14000  * 30 / subway
//    환승 = 14000 / ( 2 * 환승횟수)  = 7000 / 환승 횟수
//
//    Ex)
//    {
//        Mbti : BSW;
//
//        Walking : 14000 기준
//        Subway: 7000
//        Bus : 28000
//    }

    public SurveyResultResponseDTO makeResult(SurveyResultRequestDTO surveyResultRequestDTO) {
        surveyResultRequestDTO.getSelectedOption().get(0);



        return null;
    }
}
