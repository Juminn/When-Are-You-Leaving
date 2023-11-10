package com.enm.costcalculrator.data.repository;

import com.enm.costcalculrator.data.entity.MemberTransportCost;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@EnableScan
public interface MemberTransportCostRepository extends CrudRepository<MemberTransportCost, String> {
}
