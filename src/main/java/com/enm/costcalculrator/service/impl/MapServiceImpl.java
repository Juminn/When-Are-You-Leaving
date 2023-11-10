package com.enm.costcalculrator.service.impl;

import com.enm.costcalculrator.data.dto.Path;
import com.enm.costcalculrator.data.dto.PathRequestDTO;
import com.enm.costcalculrator.data.dto.PathResponseDTO;
import com.enm.costcalculrator.service.MapService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Service
public class MapServiceImpl implements MapService {
    @Override
    public String tmapTest() {

//        WebClient webClient = WebClient.builder()
//                .baseUrl("https://apis.openapi.sk.com")
//                .defaultHeader("appKey", "N2J3eZwzTK5RYRyxg1mwS4wVI8MQ1E9f6g7zysnA")
//                .build();
//
//        RouteRequestDTO routeDTO = new RouteRequestDTO();
//        routeDTO.setStartX("126.73706789999993");
//        routeDTO.setStartY("37.54487940000018");
//        routeDTO.setEndX("126.79758700000022");
//        routeDTO.setEndY("37.546016099999925");
//        routeDTO.setCount("10");
//        //routeDTO.setSearchDttm("202301010200");
//
//        ResponseEntity<RouteResponseDTO> test = webClient.post()
//                .uri(uriBuilder -> uriBuilder.path("/transit/routes")
//                        .build())
//                .bodyValue(routeDTO)
//                .retrieve()
//                .toEntity(RouteResponseDTO.class)
//                .block();


//        ArrayList<path> itineraries = test.getBody().getItinerary();
//        System.out.println(itineraries);

//      수동 파싱
//        JSONParser parser = new JSONParser();
//        JSONObject object;
//        try {
//            object = (JSONObject) parser.parse(test.getBody());
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        JSONObject metaData = (JSONObject) object.get("metaData");
//        JSONObject plan = (JSONObject) metaData.get("plan");
//        JSONArray itineraries = (JSONArray) plan.get("itineraries");
//        for(int i=0; i< itineraries.size(); i++){
//            object = (JSONObject)itineraries.get(i);
//            JSONArray legs = (JSONArray) object.get("legs");
//            for(int j=0; j< legs.size(); j++){
//                object = (JSONObject)legs.get(j);
//                String mode = (String) object.get("mode");
//                int sectiontime = ((Long)object.get("sectionTime")).intValue();
//                System.out.println(mode + sectiontime);
//            }
//        }

        return null;
//return itineraries.toString();
    }

    @Override
    public ArrayList<Path> getPathFromNaverMapAPI(PathRequestDTO pathRequestDTO){

        WebClient webClient = WebClient.builder()
                .baseUrl("https://map.naver.com")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        ResponseEntity<PathResponseDTO> test = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/p/api/directions/pubtrans")
                        .queryParam("start", pathRequestDTO.getStart())
                        .queryParam("goal", pathRequestDTO.getGoal())
                        .queryParam("crs", "EPSG:4326")
                        .queryParam("includeDetailOperation", "true")
                        .queryParam("lang", "ko")
                        .queryParam("mode", "TIME")
                        .queryParam("departureTime", pathRequestDTO.getDepartureTime())
                        .build())
                .retrieve()
                .toEntity(PathResponseDTO.class)
                .block();


//        ResponseEntity<RouteResponseDTO> test = webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/p/api/directions/pubtrans")
//                        .query("start=126.73706789999993,37.54487940000018,name%3D%25EC%259D%25B8%25EC%25B2%259C%25EA%25B4%2591%25EC%2597%25AD%25EC%258B%259C%2520%25EA%25B3%2584%25EC%2596%2591%25EA%25B5%25AC%2520%25EC%259E%2584%25ED%2595%2599%25EB%258F%2599%252089,address%3D1&goal=126.79758700000022,37.546016099999925,placeid%3D13479477,name%3D%25EB%25A7%2588%25EA%25B3%25A1%25EC%2597%25AD%25205%25ED%2598%25B8%25EC%2584%25A0&crs=EPSG:4326&includeDetailOperation=true&lang=ko&mode=TIME&departureTime=2023-09-18T15:50:35")
//                        .build())
//                .retrieve()
//                .toEntity(RouteResponseDTO.class)
//                .block();
//
//        test.getBody().getPaths();


        return test.getBody().getPaths();
    }

}
