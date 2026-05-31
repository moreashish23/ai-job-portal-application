package com.portal.job.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Fully public endpoints — no JWT required under any HTTP method.
     * Matches on startsWith so /auth/signup and /auth/login both pass through.
     */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/signup",
            "/auth/login"
    );

    /**
     * Endpoints that are public for GET requests (job browsing without login)
     * but require a JWT for POST/PUT/PATCH/DELETE.
     */
    private static final List<String> PUBLIC_GET_PATHS = List.of(
            "/api/jobs",
            "/api/job-categories",
            "/api/job-skills",
            "/api/job-tags",
            "/api/companies"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        // ── 1. Always pass CORS preflight through ─────────────────────────────
        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }

        // ── 2. Fully public paths — skip JWT entirely ──────────────────────────
        if (isFullyPublic(path)) {
            return chain.filter(exchange);
        }

        // ── 3. Public GET paths — allow without JWT ────────────────────────────
        if (HttpMethod.GET.equals(method) && isPublicGet(path)) {
            return chain.filter(exchange);
        }

        // ── 4. All other requests — require and validate JWT ───────────────────
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or malformed Authorization header — path={} method={}", path, method);
            return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                    "Authorization header is missing or invalid.");
        }

        String token = authHeader.substring(7); // strip "Bearer "

        try {
            Claims claims = extractClaims(token);

            // userId is stored as Integer in JWT (JSON number) — cast carefully
            Object userIdClaim = claims.get("userId");
            if (userIdClaim == null) {
                log.warn("JWT is valid but userId claim is absent — path={}", path);
                return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                        "Invalid token: userId claim is missing.");
            }

            // Handle both Integer and Long from JWT deserialization
            Long userId = userIdClaim instanceof Number
                    ? ((Number) userIdClaim).longValue()
                    : Long.parseLong(userIdClaim.toString());

            String email = claims.get("email", String.class);
            String authorities = claims.get("authorities", String.class);

            /*
             * Inject user context into the downstream request via headers.
             * Downstream services read X-User-Id (and optionally X-User-Email,
             * X-User-Roles) — they do not touch the JWT themselves.
             *
             * The default-filters in application.yaml remove these headers from
             * the INCOMING client request first, preventing header injection
             * attacks where a malicious client sends their own X-User-Id.
             */
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id",    String.valueOf(userId))
                    .header("X-User-Email", email        != null ? email        : "")
                    .header("X-User-Roles", authorities  != null ? authorities  : "")
                    .build();

            log.debug("JWT valid — userId={} path={}", userId, path);

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT — path={} : {}", path, ex.getMessage());
            return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                    "Token has expired. Please log in again.");

        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature — path={}", path);
            return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                    "Token signature is invalid.");

        } catch (MalformedJwtException ex) {
            log.warn("Malformed JWT — path={}", path);
            return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                    "Token is malformed.");

        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT — path={}", path);
            return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                    "Token type is not supported.");

        } catch (Exception ex) {
            log.error("Unexpected JWT error — path={} : {}", path, ex.getMessage());
            return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED,
                    "Token validation failed.");
        }
    }

    /**
     * Run before all other filters — HIGHEST_PRECEDENCE ensures JWT is
     * validated before any route filter touches the request.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isFullyPublic(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isPublicGet(String path) {
        return PUBLIC_GET_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange,
                                          HttpStatus status,
                                          String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Match the ApiResponse structure used across all services: {message, status}
        String body = String.format("{\"message\":\"%s\",\"status\":false}", message);
        DataBuffer buffer = response.bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}