package com.enm.costcalculrator.service.impl;

import com.enm.costcalculrator.data.dto.Path;
import com.enm.costcalculrator.data.dto.PathRequestDTO;
import com.enm.costcalculrator.data.dto.PathResponseDTO;
import com.enm.costcalculrator.service.ApiCallManager;
import com.enm.costcalculrator.service.MapService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


import java.util.ArrayList;

@Service
public class MapServiceImpl implements MapService {

    private static final Logger logger = LoggerFactory.getLogger(MapServiceImpl.class);
    private final RateLimiter rateLimiter;
    private final ApiCallManager apiCallManager;

    @Autowired
    public MapServiceImpl(RateLimiter rateLimiter, ApiCallManager apiCallManager) {
        this.rateLimiter = rateLimiter;
        this.apiCallManager = apiCallManager;
    }

    @Override
    public Mono<ArrayList<Path>> getPathFromNaverMapAPI(PathRequestDTO pathRequestDTO){

        // API 호출 전에 카운터 증가
        apiCallManager.incrementPendingApiCalls();
        logger.info("mytest apicalls: {}", apiCallManager.getPendingApiCalls());


        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom")
                .maxConnections(50)
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider);

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://map.naver.com")
                .defaultHeader("Referer", "https://map.naver.com/p/directions/14149906.4245121,4509840.9725981,%EC%84%9C%EC%9A%B8%20%EC%86%A1%ED%8C%8C%EA%B5%AC%20%EB%B0%B1%EC%A0%9C%EA%B3%A0%EB%B6%84%EB%A1%9C42%EA%B8%B8%2013,,SIMPLE_POI/14138767.3237372,4506020.1451819,%EC%84%9C%EC%9A%B8%20%EC%84%9C%EC%B4%88%EA%B5%AC%20%EC%84%9C%EC%B4%88%EB%8F%99%20%EC%82%B0144-4,,SIMPLE_POI/-/transit?c=10.07,0,0,0,dh")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .filter(logRequest())   // 요청 로그 필터 추가
                .filter(logResponse())  // 응답 로그 필터 추가
                .build();

        return webClient.get()
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
                .onStatus(HttpStatus::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        //logger.error()
                        return Mono.error(new RuntimeException("MAP API 에러: " + errorBody));
                    });
                })
                .toEntity(PathResponseDTO.class)
                .map(responseEntity -> {
                    //System.out.println("naverMapApI endTime: " + LocalDateTime.now());

                    if(responseEntity.getBody() == null){
                        return new ArrayList<Path>();
                    }
                    else {
                        return responseEntity.getBody().getPaths();
                    }
                })
                .transformDeferred(RateLimiterOperator.of(rateLimiter)) // 레이트 리미터 적용;
                .doFinally(signalType -> {
                    // API 호출 완료 후 카운터 감소
                    apiCallManager.decrementPendingApiCalls();
                })
                .onErrorResume(throwable -> {
                    //logger
                    return Mono.error(new RuntimeException("MAP API 요청 중 내부 서버 에러: " + throwable));
                });

    }

    // 요청 시 로그를 출력하는 필터 함수
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value ->
                            logger.info("Header '{}': {}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    // 응답 시 로그를 출력하는 필터 함수
    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Response Status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

}
