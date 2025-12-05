package com.enm.costcalculrator.config;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;          // ★ Boot 2.x는 javax
import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCLoggingFilter implements Filter {   // ★ public + Filter 구현

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String requestId = UUID.randomUUID().toString();
        MDC.put("request_id", requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            // 이 스레드에서만 쓰던 MDC 정리
            MDC.remove("request_id");   // 또는 MDC.clear();
        }
    }
}