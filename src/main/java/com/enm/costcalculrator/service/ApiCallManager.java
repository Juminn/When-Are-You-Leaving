package com.enm.costcalculrator.service;

import org.springframework.stereotype.Component;


import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ApiCallManager {

    private final AtomicInteger pendingApiCalls = new AtomicInteger(0);

    // API 레이트 리밋 설정 (10초에 1회 호출 가능)
    private static final int API_LIMIT_INTERVAL_MILLIS = 10 * 1000;

    // 최대 허용 대기 시간 (9.5분)
    private static final int MAX_WAIT_TIME_MILLIS = (int) (9.5 * 60 * 1000);

    /**
     * 현재 대기 중인 API 호출 수를 반환합니다.
     */
    public int getPendingApiCalls() {
        return pendingApiCalls.get();
    }

    /**
     * API 호출을 시작할 때 호출하여 카운터를 증가시킵니다.
     */
    public void incrementPendingApiCalls() {
        pendingApiCalls.incrementAndGet();
    }

    /**
     * API 호출이 완료되면 호출하여 카운터를 감소시킵니다.
     */
    public void decrementPendingApiCalls() {
        pendingApiCalls.decrementAndGet();
    }

    /**
     * 주어진 추가 API 호출 수에 대해 예상 대기 시간이 10분을 초과하는지 확인합니다.
     * @param additionalApiCalls 필요한 추가 API 호출 수
     * @return 대기 시간이 10분을 초과하면 true, 그렇지 않으면 false
     */
    public boolean isExpectedWaitTimeExceedingLimit(int additionalApiCalls) {
        int totalPendingCalls = getPendingApiCalls() + additionalApiCalls;
        int totalEstimatedTimeMillis = totalPendingCalls * API_LIMIT_INTERVAL_MILLIS;
        return totalEstimatedTimeMillis > MAX_WAIT_TIME_MILLIS;
    }
}