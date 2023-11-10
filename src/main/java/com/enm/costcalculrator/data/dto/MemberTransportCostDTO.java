package com.enm.costcalculrator.data.dto;

import lombok.Data;

@Data
public class MemberTransportCostDTO {
    public int opportunityCost;
    public int walking;
    public int bus;
    public int subway;
}
