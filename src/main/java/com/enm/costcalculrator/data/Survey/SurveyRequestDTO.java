package com.enm.costcalculrator.data.Survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyRequestDTO {
    private int questionIndex;
    private Integer followUpQuestionIndex;
    private int selectedOption;
//    private String option1Transport;
//    private int option1Duration;
//    private String option2Transport;
//    private int option2Duration;
    private List<OptionDTO> options;

    public int getDurationOfOption(int optionIndex) {
        return options.get(optionIndex).getDuration();
    }
}
