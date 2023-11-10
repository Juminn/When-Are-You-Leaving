package com.enm.costcalculrator.data.dao.impl;

import com.enm.costcalculrator.data.dao.MemberTransportCostDAO;
import com.enm.costcalculrator.data.entity.MemberTransportCost;
import com.enm.costcalculrator.data.repository.MemberTransportCostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Primary
public class MemberTransportCostDAOImpl implements MemberTransportCostDAO {
    private final MemberTransportCostRepository memberTransportCostRepository;


    public MemberTransportCostDAOImpl(MemberTransportCostRepository memberTransportCostRepository) {
        this.memberTransportCostRepository = memberTransportCostRepository;
    }

    @Override
    public MemberTransportCost insertMemberTransportCost(MemberTransportCost memberTransportCost) {
        MemberTransportCost savedMemberTransportCost = memberTransportCostRepository.save(memberTransportCost);
        return savedMemberTransportCost;
    }

    @Override
    public Optional<MemberTransportCost> selectMemberTransportCost(String id) {
        Optional<MemberTransportCost> selectedMemberTransportCost = memberTransportCostRepository.findById(id);
        return selectedMemberTransportCost;
    }


}
