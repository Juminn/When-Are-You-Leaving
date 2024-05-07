package com.enm.costcalculrator.data.Survey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResultRequestDTO {
    private List SelectedOption;
}
