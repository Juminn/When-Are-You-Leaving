package com.enm.costcalculrator.data.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteTimeDTO {
    String startX;
    String startY;

    String goalX;
    String goalY;

    LocalDateTime startTime;
}
