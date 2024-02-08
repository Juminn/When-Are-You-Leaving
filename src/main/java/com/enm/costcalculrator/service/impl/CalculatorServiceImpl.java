package com.enm.costcalculrator.service.impl;

import com.enm.costcalculrator.data.dto.*;
import com.enm.costcalculrator.data.enums.Transportation;
import com.enm.costcalculrator.service.CalculatorService;
import com.enm.costcalculrator.service.MapService;
import com.enm.costcalculrator.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    final MapService mapService;
    final MemberService memberService;

    @Autowired
    public CalculatorServiceImpl(MapService mapService, MemberService memberService) {
        this.mapService = mapService;
        this.memberService = memberService;
    }

    //스케쥴DTO를 기반으로 시간주기 만들어주는 함수만드는게좋을듯?




    //현재는 출발시간기준 시간범위에서 1분단위로 계산.
    //해야할사항 n분단위는 파라미터로입력받자
    //시간범위를 설정해서 보내자?
    //도착시간 설정알고리즘에서는 도착시간을 체크하고 DTO를 생성해야함. 아예새로만들어야하나?
    public PathAndCostAndAnalysisDTO calculate(ScheduleDTO scheduleDTO){

        //while 문으로 paths.startTime~endTime 까지 이때 시간주기 알고리즘은 나중에생각하고 일단은 10분단위로 해보고 1분단위로 수정하기
        ArrayList<PathAndCostAndAnalysisDTO> pathAndCostAndAnalysisDTOS = new ArrayList<>();
        LocalDateTime targetTime = LocalDateTime.parse(scheduleDTO.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(scheduleDTO.getEndTime());

        targetTime = targetTime.minusMinutes(10);
        while(targetTime.plusMinutes(10).isBefore(endTime)) {
            targetTime = targetTime.plusMinutes(10);
            PathRequestDTO pathRequestDTO = makeRouteRequestDTO(scheduleDTO, targetTime);

            ArrayList<Path> paths = mapService.getPathFromNaverMapAPI(pathRequestDTO);



            //memberTransportCostDTO = memberService.getMemberTransportCost("65f2a9c5-00c7-4c59-9b23-f984c3b50418");

            MemberTransportCostDTO memberTransportCostDTO = new MemberTransportCostDTO(scheduleDTO.getOpportunityCost(), scheduleDTO.getWalkingCost(), scheduleDTO.getBusCost(), scheduleDTO.getSubwayCost());

            System.out.println("mytest: " + memberTransportCostDTO);

            //example Path and Cost AnalysisDTO identity
            PathAndCostAndAnalysisDTO pathAndCostAndAnalysisDTO = new PathAndCostAndAnalysisDTO();
            ArrayList<PathAndCost> pathAndCosts = new ArrayList<PathAndCost>();
            pathAndCostAndAnalysisDTO.setPathAndCosts(pathAndCosts);

            //경로별 비용계산
            for (Path path : paths) {
                PathAndCost pathAndCost = makePathAndCost(path, memberTransportCostDTO);
                pathAndCosts.add(pathAndCost);

            }

            //모든 경로에서 최소비용과 번호 세팅
            setMinCostAndIndexOfPaths(pathAndCostAndAnalysisDTO);
            pathAndCostAndAnalysisDTOS.add(pathAndCostAndAnalysisDTO);

            System.out.println(pathAndCostAndAnalysisDTO.getMinCost());
        }
        //while문 끝나고 DTOS에서 최솟값찾고 그 DTO반환

        //System.out.println(pathAndCostAndAnalysisDTOS);
        pathAndCostAndAnalysisDTOS.get(0).getPathAndCosts().get(0).getPath().makeSubDuration();

        return searchMinDTO(pathAndCostAndAnalysisDTOS);
    }

    //Search minus mum
    private PathAndCostAndAnalysisDTO searchMinDTO(ArrayList<PathAndCostAndAnalysisDTO> pathAndCostAndAnalysisDTOS){
        PathAndCostAndAnalysisDTO result;
        result = pathAndCostAndAnalysisDTOS.get(0);
        int minCost = result.getMinCost();

        for(PathAndCostAndAnalysisDTO pathAndCostAndAnalysisDTO:pathAndCostAndAnalysisDTOS){
            if (pathAndCostAndAnalysisDTO.getMinCost() < minCost){
                result = pathAndCostAndAnalysisDTO;
                minCost = result.getMinCost();
            }
        }

        return result;
    }



    //makeRouteRequestDTO
    private PathRequestDTO makeRouteRequestDTO(ScheduleDTO scheduleDTO, LocalDateTime targetTime){

        PathRequestDTO pathRequestDTO = new PathRequestDTO();
        pathRequestDTO.setStart(scheduleDTO.getStartX() + ", " + scheduleDTO.getStartY());
        pathRequestDTO.setGoal(scheduleDTO.getGoalX() + ", " + scheduleDTO.getGoalY());
        pathRequestDTO.setDepartureTime(String.valueOf(targetTime));

        return pathRequestDTO;
    }


    //대중교통 손해금액계산 example this is a
    private PathAndCost makePathAndCost(Path path, MemberTransportCostDTO memberTransportCostDTO){
        PathAndCost pathAndCost = new PathAndCost();
        int cost = 0;

        cost += path.getDuration(Transportation.SUBWAY) * memberTransportCostDTO.subway / 60;
        cost += (path.getDuration(Transportation.WALKING) + path.getWaitingDuration()) * memberTransportCostDTO.walking / 60;
        cost += path.getDuration(Transportation.BUS) * memberTransportCostDTO.bus / 60;
        cost += ( path.getTransferCount() + 1 ) * 5 * memberTransportCostDTO.getOpportunityCost() / 60;

        if(cost==37700){
            System.out.println("test: " + path.getDuration(Transportation.SUBWAY));
            System.out.println(path.getDuration(Transportation.WALKING));
            System.out.println(path.getWaitingDuration());
            System.out.println(path.getDuration(Transportation.BUS));
            System.out.println(( path.getTransferCount() + 1 ) * 15 * memberTransportCostDTO.getOpportunityCost() / 60);
        }

        pathAndCost.setPath(path);
        pathAndCost.setCost(cost);

        return pathAndCost;
    }
    //
    private void setMinCostAndIndexOfPaths(PathAndCostAndAnalysisDTO pathAndCostAndAnalysisDTO){
        //pathAndCostAndAnalysisDTO.pathAndCost 어레이에서 최소 코스트 찾기
        ArrayList<PathAndCost> pathAndCosts = pathAndCostAndAnalysisDTO.getPathAndCosts();

        int minCostIndex = 0;
        int minCost = pathAndCosts.get(minCostIndex).getCost();
        for(int i=1; i<pathAndCosts.size(); i++){
            int targetIndex = i;
            int targetCost = pathAndCosts.get(targetIndex).getCost();

            if(targetCost < minCost){
                minCostIndex = targetIndex;
                minCost = targetCost;
            }
        }

        pathAndCostAndAnalysisDTO.setMinCostIndex(minCostIndex);
        pathAndCostAndAnalysisDTO.setMinCost(minCost);
    }
}