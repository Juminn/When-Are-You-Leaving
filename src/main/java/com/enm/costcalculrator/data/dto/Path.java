package com.enm.costcalculrator.data.dto;

import com.enm.costcalculrator.data.enums.Transportation;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

//여러 경로중 하나
@Data
public class Path {
    private String departureTime;
    private String arrivalTime;
    private ArrayList<Leg> legs;
    private int waitingDuration;
    private int transferCount;


    //알고리즘 최적화 할수있음. - make 형식으로 각 타입별 소요시간을 필드로 만들자?
    //get Duration
    public int getDuration(Transportation transportation){

        int totalDuration = 0;
        Leg leg = legs.get(0);

        for(Step step: leg.getSteps()){
            if (Objects.equals(step.getType(), transportation.toString())) {
                totalDuration += step.getDuration();

            }
        }

        return totalDuration;
    }


    //일단 보류
    public void makeSubDuration(){

        for(Leg leg: legs){
            for(Step step: leg.getSteps()){
                if (Objects.equals(step.getType(), Transportation.SUBWAY.toString())) {
                    System.out.println("hi");
                }
            }
        }
    }

    //test
//    public void walkDestination(){
//        for(Step step: legs.get(0).getSteps()){
//            step.getWalkpath()
//        }
//            System.out.println();
//    }


}

@Data
class Leg{
    private ArrayList<Step> steps;

}

//하나의 구간
@Data
class Step {
    private String type;
    private int duration;
    private String departureTime;
    private ArrayList<Route> routes; // 한 구간일 이동할때 가능한 버스들, 지하철들
    private Walkpath walkpath; // 상세경로 - 걷기 전용
    private ArrayList<Station> stations; // 상세경로 - 버스,지하철 전용
}

//버스, 지하철전용, 하나의 구간을 이동하는 여러루트 중 하나
@Data
class Route{
    private String name;
}

@Data
class Walkpath{
    private Summary summary;
}

@Data
class Summary{
    private ArrayList<way> ways;
}

@Data
class way{
    private String name;
}

@Data
class Station{
    private String name;
}
