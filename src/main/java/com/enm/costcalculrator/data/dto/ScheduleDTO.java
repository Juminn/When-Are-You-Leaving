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
}
