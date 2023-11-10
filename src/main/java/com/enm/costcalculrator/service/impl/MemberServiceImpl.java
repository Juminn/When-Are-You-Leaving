package com.enm.costcalculrator.service.impl;

import com.enm.costcalculrator.data.dao.MemberTransportCostDAO;
import com.enm.costcalculrator.data.dto.MemberTransportCostDTO;
import com.enm.costcalculrator.data.entity.MemberTransportCost;
import com.enm.costcalculrator.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberTransportCostDAO memberTransportCostDAO;

    @Autowired
    public MemberServiceImpl(MemberTransportCostDAO memberTransportCostDAO) {
        this.memberTransportCostDAO = memberTransportCostDAO;
    }


    @Override
    public MemberTransportCostDTO getMemberTransportCost(String memberID) {

        Optional<MemberTransportCost> memberTransportCostOpt = memberTransportCostDAO.selectMemberTransportCost(memberID);
        if(memberTransportCostOpt.isPresent()){
            MemberTransportCost memberTransportCost = memberTransportCostOpt.get();

            MemberTransportCostDTO memberTransportCostDTO = new MemberTransportCostDTO();
            memberTransportCostDTO.setOpportunityCost(memberTransportCost.getOpportunityCost());
            memberTransportCostDTO.setBus(memberTransportCost.getBus());
            memberTransportCostDTO.setWalking(memberTransportCost.getWalking());
            memberTransportCostDTO.setSubway(memberTransportCost.getSubway());

            return memberTransportCostDTO;
        }
        else {
            throw new RuntimeException("No memberTransportCost");
        }
    }

    @Override
    public MemberTransportCostDTO saveMemberTransportCost(MemberTransportCostDTO memberTransportCostDTO) {

        MemberTransportCost memberTransportCost = new MemberTransportCost();
        memberTransportCost.setWalking(memberTransportCostDTO.getWalking());
        memberTransportCost.setBus(memberTransportCostDTO.getBus());
        memberTransportCost.setSubway(memberTransportCostDTO.getSubway());

        memberTransportCostDAO.insertMemberTransportCost(memberTransportCost);

        return null;
    }
}
