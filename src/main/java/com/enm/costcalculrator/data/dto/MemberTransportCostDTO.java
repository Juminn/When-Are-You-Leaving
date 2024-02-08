package com.enm.costcalculrator.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberTransportCostDTO {
    public int opportunityCost;
    public int walking;
    public int bus;
    public int subway;
}
