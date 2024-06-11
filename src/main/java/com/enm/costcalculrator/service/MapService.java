package com.enm.costcalculrator.service;


import com.enm.costcalculrator.data.dto.Path;
import com.enm.costcalculrator.data.dto.PathRequestDTO;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public interface MapService {

    public String tmapTest();



    public Mono<ArrayList<Path>> getPathFromNaverMapAPI(PathRequestDTO scheduleDTO);
}

