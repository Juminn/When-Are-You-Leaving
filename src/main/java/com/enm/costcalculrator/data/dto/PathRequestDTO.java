package com.enm.costcalculrator.data.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PathRequestDTO {
    private String start;
    private String goal;
    private String crs;
    private String includeDetailOperation;
    private String lang;
    private String mode;
    private String departureTime;
}
