package com.enm.costcalculrator.service.Survey.impl;

import com.enm.costcalculrator.data.Survey.*;
import com.enm.costcalculrator.service.Survey.MbtiDescriptionManager;
import com.enm.costcalculrator.service.Survey.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private MbtiDescriptionManager mbtiDescriptionManager;

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

        int initDuraiton = surveyRequestDTO.getTransportOfOption(1).equals("Transfer") ? initTransferNum : initTrasnPortDuration;


        if(opt1Duration < initDuraiton){
            return 0;
        } else if (opt1Duration > initDuraiton) {
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
    public SurveyResultResponseDTO makeResult(SurveyResultRequestDTO surveyResultRequestDTO) {
        int walkingCost = 14000;

        int busCost = walkingCost*30 / surveyResultRequestDTO.getBus();
        int subwayCost = walkingCost*30 / surveyResultRequestDTO.getSubway();
        int transferCost = walkingCost / 2 / surveyResultRequestDTO.getTransfer();

        SurveyResultResponseDTO surveyResultResponseDTO = new SurveyResultResponseDTO();
        surveyResultResponseDTO.setWalkingCost(walkingCost);
        surveyResultResponseDTO.setBusCost(busCost);
        surveyResultResponseDTO.setSubwayCost(subwayCost);
        surveyResultResponseDTO.setTransferCost(transferCost);

        walkingCost = 14000 + 1; // 동일한 14000의 다른 대중교통보다 선택이안됫으므로 비용이높음

        String mbti = makeMbti(walkingCost, busCost, subwayCost);
        String mbtiDescription = mbtiDescriptionManager.getDescription(mbti);

        surveyResultResponseDTO.setMbti(mbti);
        surveyResultResponseDTO.setMbtiDescription(mbtiDescription);

        return surveyResultResponseDTO;
    }

    private String makeMbti( int walkingCost, int busCost, int subwayCost) {

        // HashMap으로 교통수단과 비용 관리
        Map<String, Integer> costMap = new HashMap<>();
        costMap.put("B", busCost);
        costMap.put("S", subwayCost);
        costMap.put("W", walkingCost);

        // HashMap을 스트림으로 정렬하고 결과 문자열 생성
        String modeSequence = costMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining());

        return modeSequence;
    }
}
