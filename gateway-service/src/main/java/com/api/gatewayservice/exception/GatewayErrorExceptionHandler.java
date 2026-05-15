package com.api.gatewayservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Order(-2)
@Slf4j
public class GatewayErrorExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Gateway Error: {}", ex.getMessage());

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        String error = "Service Unavailable";
        String message = "Cổng Gateway không thể kết nối tới dịch vụ đích";

        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            error = "Not Found";
            message = "Đường dẫn không tồn tại trên hệ thống";
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorBody = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", error,
                "message", message
        );

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorBody);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}

/*
Tầm ảnh hưởng (Scope): @RestControllerAdvice chỉ hoạt động trong tầng Controller của Spring MVC.
Trong Gateway, nhiều lỗi xảy ra trước khi request kịp chạm đến controller (ví dụ: lỗi Routing, lỗi Filter, hoặc lỗi kết nối đến Service đích).

Cơ chế Web Stack: Gateway (bản Reactive) hoạt động trên nền tảng WebFlux.
ErrorWebExceptionHandler là interface cấp thấp hơn, cho phép can thiệp trực tiếp vào chuỗi xử lý của Web Server để bắt mọi ngoại lệ phát sinh trong toàn bộ vòng đời của một request.
* */
