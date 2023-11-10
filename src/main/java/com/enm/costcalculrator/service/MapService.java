package com.enm.costcalculrator.service;


import com.enm.costcalculrator.data.dto.Path;
import com.enm.costcalculrator.data.dto.PathRequestDTO;

import java.util.ArrayList;

public interface MapService {

    public String tmapTest();



    public ArrayList<Path> getPathFromNaverMapAPI(PathRequestDTO scheduleDTO);
}

