package com.enm.costcalculrator.service;

import com.enm.costcalculrator.data.dto.MemberTransportCostDTO;

public interface MemberService {

    public MemberTransportCostDTO getMemberTransportCost(String memberID);
    public MemberTransportCostDTO saveMemberTransportCost(MemberTransportCostDTO memberTransportCostDTO);
}
