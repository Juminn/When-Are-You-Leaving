package com.enm.costcalculrator.data.dto;

import lombok.Data;

import java.util.ArrayList;


@Data
public class PathAndCostAndAnalysisDTO {
    int minCostIndex;
    int minCost;
    int minDuration;
    ArrayList<PathAndCost> PathAndCosts;

}
