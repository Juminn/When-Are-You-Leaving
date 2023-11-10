package com.enm.costcalculrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableDynamoDBRepositories(basePackages = "com.enm.costcalculrator.data.repository")
public class CostCalculratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CostCalculratorApplication.class, args);
    }

}
