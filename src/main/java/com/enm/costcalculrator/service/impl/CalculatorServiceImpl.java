package com.enm.costcalculrator.service.impl;

import com.enm.costcalculrator.data.dto.*;
import com.enm.costcalculrator.data.enums.Transportation;
import com.enm.costcalculrator.service.ApiCallManager;
import com.enm.costcalculrator.service.CalculatorService;
import com.enm.costcalculrator.service.MapService;
import com.enm.costcalculrator.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    final MapService mapService;
    final MemberService memberService;
    private final ApiCallManager apiCallManager;

    @Autowired
    public CalculatorServiceImpl(MapService mapService, MemberService memberService, ApiCallManager apiCallManager) {
        this.mapService = mapService;
        this.memberService = memberService;
        this.apiCallManager = apiCallManager;
    }

    //스케쥴DTO를 기반으로 시간주기 만들어주는 함수만드는게좋을듯




    //현재는 출발시간기준 시간범위에서 1분단위로 계산.
    //해야할사항 n분단위는 파라미터로입력받자
    //시간범위를 설정해서 보내자?
    //도착시간 설정알고리즘에서는 도착시간을 체크하고 DTO를 생성해야함. 아예새로만들어야하나
    public PathAndCostAndAnalysisDTO calculate(ScheduleDTO scheduleDTO){

        //while 문으로 paths.startTime~endTime 까지 이때 시간주기 알고리즘은 나중에생각하고 일단은 10분단위로 해보고 1분단위로 수정하기\
        ArrayList<PathAndCostAndAnalysisDTO> pathAndCostAndAnalysisDTOS = new ArrayList<>();
        LocalDateTime targetTime = LocalDateTime.parse(scheduleDTO.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(scheduleDTO.getEndTime());

        //10분내 결과 받을 수 있는지 체크
        // 필요한 API 호출 수 계산
        long intervalCount = Duration.between(targetTime, endTime).toMinutes() / 10 + 1;
        int totalApiCallsNeeded = (int) intervalCount;
        // 예상 대기 시간이 10분을 초과하는지 확인
        if (apiCallManager.isExpectedWaitTimeExceedingLimit(totalApiCallsNeeded)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "현재 서비스가 많이 사용 중입니다. 10분 뒤에 요청해 주세요.");
        }

        //비동기 실해으로 동시에 접근할 수 도 있기 때문에
        List<Throwable> errorList = new CopyOnWriteArrayList<>();

        // 현재 targetTime부터 endTime까지의 간격을 계산
        CountDownLatch latch = new CountDownLatch((int) intervalCount);

        targetTime = targetTime.minusMinutes(10);
        while(!targetTime.plusMinutes(10).isAfter(endTime)) {
            targetTime = targetTime.plusMinutes(10);
            PathRequestDTO pathRequestDTO = makeRouteRequestDTO(scheduleDTO, targetTime);

            //ArrayList<Path> paths = mapService.getPathFromNaverMapAPI(pathRequestDTO);
            //그렇다면 업무를 최소화
            mapService.getPathFromNaverMapAPI(pathRequestDTO)
                .subscribe(
                    paths -> {
                        if(paths.size() == 0){
                            latch.countDown();
                            return;
                        }

                        //memberTransportCostDTO = memberService.getMemberTransportCost("65f2a9c5-00c7-4c59-9b23-f984c3b50418");
                        MemberTransportCostDTO memberTransportCostDTO = new MemberTransportCostDTO(scheduleDTO.getWalkingCost(),scheduleDTO.getBusCost(),  scheduleDTO.getSubwayCost(),  scheduleDTO.getTransferCost());

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

                        latch.countDown();
                    },
                    throwable -> {
                        latch.countDown();
                        //logger
                        errorList.add(throwable);
                    }
                );
        }
        //while문 끝나고 DTOS에서 최솟값찾고 그 DTO반환
        try {
            latch.await();  // 모든 subscribe 호출이 완료될 때까지 대기
            System.out.println("All intervals processed. Proceeding with result aggregation.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Calculation interrupted.");
        }

        //System.out.println(pathAndCostAndAnalysisDTOS);
        if (pathAndCostAndAnalysisDTOS.isEmpty()) {
            System.out.println(errorList);
            if(errorList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "대중교통 이용이 불가능한 시간입니다.");
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MAP API 사용중 에러가 발생 했습니다.");
            }
        }

        //pathAndCostAndAnalysisDTOS.get(0).getPathAndCosts().get(0).getPath().makeSubDuration();
        System.out.println("cal start time:  " + LocalDateTime.now());

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

        cost += path.getTransportDuration(Transportation.SUBWAY) * memberTransportCostDTO.subway / 60;
        //System.out.println("mytest cost: "  + cost);
        cost += (path.getTransportDuration(Transportation.WALKING) + path.getWaitingDuration()) * memberTransportCostDTO.walking / 60;
        //System.out.println("mytest cost: "  + cost);
        cost += path.getTransportDuration(Transportation.BUS) * memberTransportCostDTO.bus / 60;
        //System.out.println("mytest cost: "  + cost);
        cost +=path.getTransferCount() * memberTransportCostDTO.getTransferCost();
        //System.out.println("mytest cost: "  + cost);

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
        int minDuraiton = pathAndCosts.get(minCostIndex).getPath().getDuration();
        for(int i=1; i<pathAndCosts.size(); i++){
            int targetIndex = i;
            int targetCost = pathAndCosts.get(targetIndex).getCost();
            int targetDuration = pathAndCosts.get(targetIndex).getPath().getDuration();

            if(targetCost < minCost){
                minCostIndex = targetIndex;
                minCost = targetCost;
                minDuraiton = targetDuration;
            }
        }

        pathAndCostAndAnalysisDTO.setMinCostIndex(minCostIndex);
        pathAndCostAndAnalysisDTO.setMinCost(minCost);
        pathAndCostAndAnalysisDTO.setMinDuration(minDuraiton);
    }
}