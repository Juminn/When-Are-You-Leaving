package com.enm.costcalculrator.data.dao;

import com.enm.costcalculrator.data.entity.MemberTransportCost;

import java.util.Optional;

public interface MemberTransportCostDAO {

    MemberTransportCost insertMemberTransportCost(MemberTransportCost memberTransportCost);

    Optional<MemberTransportCost> selectMemberTransportCost(String id);


}
