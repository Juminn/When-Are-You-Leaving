package com.enm.costcalculrator.data.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    String startX;
    String startY;

    String goalX;
    String goalY;

    String startTime;
    String endTime;

    int walkingCost;
    int subwayCost;
    int busCost;
    int transferCost;

    //기본적으로 20~30대 중위 시급: 1.4만원, subway , 버스, 걷기순으로선호
}
