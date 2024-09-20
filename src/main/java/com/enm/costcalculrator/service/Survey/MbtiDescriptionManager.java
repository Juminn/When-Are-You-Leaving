package com.enm.costcalculrator.service.Survey;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MbtiDescriptionManager {
    private Map<String, String> descriptions;

    public MbtiDescriptionManager() {
        descriptions = new HashMap<>();

        descriptions.put("BSW", "BSW유형 - 대한민국에서 두번째로 많은 유형입니다. " +
                "특징 - 지하철 계단을 싫어 하거나, 출퇴근 붐비는 지하철을 싫어한다. 걷는 것을 싫어 한다." +
                "버스 이용을 가장 선호하며, 다음으로 지하철, 마지막으로 걷기 순으로 선호합니다." +
                "보완점 - 건강을 위해 짧은 거리는 걸어가보는게 어떨까요?\"");

        descriptions.put("BWS", "BWS유형 " +
                "특징 - 지하철 계단을 싫어 하거나, 출퇴근 붐비는 지하철을 싫어한다." +
                "버스 이용을 가장 선호하며, 다음으로 걷기, 마지막으로 지하철 순으로 선호합니다.");

        descriptions.put("SBW", "SBW유형 - 대한민국에서 가장 많은 유형입니다. " +
                "특징 - 멀미, 규칙적인 운행시간 등의 이유로 지하철을 가장 선호, 걷는 것을 싫어 한다. " +
                "보완점 - 건강을 위해 짧은 거리는 걸어가보는게 어떨까요? " +
                "지하철 이용을 가장 선호하며, 다음으로 버스, 마지막으로 걷기 순으로 선호합니다.");

        descriptions.put("SWB", "SWB유형 지하철 이용이 가장 선호하며, 다음으로 걷기, 마지막으로 버스 순으로 선호합니다. " +
                "특징 - 멀미, 규칙적인 운행시간 등의 이유로 지하철을 가장 선호"
                );

        descriptions.put("WBS", "WBS유형 걷기를 가장 선호하며, 다음으로 버스, 마지막으로 지하철 순으로 선호합니다. " +
                "특징 - 갇혀있는 대중교통보다 걷기를 좋아한다. 지하철 계단이나 출퇴근 붐비는 이유로 지하철을 싫어할 수 있다. " +
                "보완점 - 단순 걷기보단 조금 빠른 걸음으로 심박수를 높여 더욱 건강을 챙기세요 ");

        descriptions.put("WSB", "WSB유형 걷기를 가장 선호하며, 다음으로 지하철, 마지막으로 버스 순으로 선호합니다. " +
                "특징 - 갇혀있는 대중교통보다 걷기를 좋아한다.  멀미, 규칙적인 운행시간 등의 이유로 지하철을 선호 " +
                "보완점 - 단순 걷기보단 조금 빠른 걸음으로 심박수를 높여 더욱 건강을 챙기세요 ");
    }

    public String getDescription(String mbtiCode) {
        return descriptions.getOrDefault(mbtiCode, "설명 없음");
    }
}