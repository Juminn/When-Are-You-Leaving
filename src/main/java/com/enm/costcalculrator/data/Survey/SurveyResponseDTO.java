package com.enm.costcalculrator.data.Survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponseDTO {
    private int questionIndex;
    private Integer followUpQuestionIndex;
//    private String option1Transport;
//    private int option1Duration;
//    private String option2Transport;
//    private int option2Duration;
    private List<OptionDTO> options;
}
