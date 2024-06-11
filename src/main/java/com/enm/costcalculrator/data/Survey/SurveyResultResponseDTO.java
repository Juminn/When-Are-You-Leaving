package com.enm.costcalculrator.data.Survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultResponseDTO {
    private String mbti;
    private String mbtiDescription;

    private int walkingCost;
    private int busCost;
    private int subwayCost;
    private int transferCost;
}