package com.enm.costcalculrator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice   // 모든 @RestController 대상 전역 예외 처리기
public class GlobalExceptionHandler {

    /**
     * 서비스/컨트롤러에서 던지는 ResponseStatusException 공통 처리
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        // 여기서 한 번 더 상황 로깅 (AOP랑 중복이지만, HTTP 정보까지 같이 찍을 수 있음)
        log.error("ResponseStatusException: status={}, reason={}, path={}",
                ex.getStatus(), ex.getReason(), request.getRequestURI(), ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", ex.getStatus().value());
        body.put("error", ex.getStatus().getReasonPhrase());
        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());
        // MDC에 request_id 심어둔 상태라면, 여기서도 같이 내려줄 수 있음
        // body.put("requestId", MDC.get("request_id"));

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    /**
     * 예상 못 한 모든 예외 공통 처리 (NullPointerException 등)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unhandled exception at path={}", request.getRequestURI(), ex);

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", "서버 내부 오류가 발생했습니다.");
        body.put("path", request.getRequestURI());
        // body.put("requestId", MDC.get("request_id"));

        return ResponseEntity.status(500).body(body);
    }
}
