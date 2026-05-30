package com.portal.job.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;

/**
 * Catches exceptions thrown AFTER the gateway filter chain — mainly
 * Eureka "no instances available" and downstream connection failures.
 * Order(-1) runs before Spring Boot's default error handler.
 */
@Component
@Order(-1)
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus status;
        String message;

        if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.resolve(rse.getStatusCode().value());
            if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = rse.getReason() != null ? rse.getReason() : ex.getMessage();

        } else if (isConnectionFailure(ex)) {
            status  = HttpStatus.SERVICE_UNAVAILABLE;
            message = "The requested service is temporarily unavailable. Please try again shortly.";
            log.error("Downstream connection failure: {}", ex.getMessage());

        } else if (isNoInstanceAvailable(ex)) {
            status  = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Service is not reachable. It may not be registered or is currently starting.";
            log.error("Eureka: no instance available — {}", ex.getMessage());

        } else {
            status  = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "An unexpected gateway error occurred.";
            log.error("Gateway unhandled exception: {}", ex.getMessage(), ex);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"message\":\"%s\",\"status\":false}", message);
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private boolean isConnectionFailure(Throwable ex) {
        return ex instanceof ConnectException
                || (ex.getMessage() != null && ex.getMessage().contains("Connection refused"));
    }

    private boolean isNoInstanceAvailable(Throwable ex) {
        String msg = ex.getMessage();
        return msg != null && (
                msg.contains("Unable to find instance")
                        || msg.contains("No instances available")
                        || msg.contains("503")
        );
    }
}