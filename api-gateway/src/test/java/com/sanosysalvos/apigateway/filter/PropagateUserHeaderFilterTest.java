package com.sanosysalvos.apigateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PropagateUserHeaderFilterTest {

    private PropagateUserHeaderFilter filter;
    private GatewayFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new PropagateUserHeaderFilter();
        filterChain = mock(GatewayFilterChain.class);
    }

    @Test
    void filter_withJwtPrincipal_shouldAddHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("auth0|123456");

        TestingAuthenticationToken authentication = new TestingAuthenticationToken(jwt, null);
        SecurityContext securityContext = new SecurityContextImpl(authentication);

        when(filterChain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange modifiedExchange = invocation.getArgument(0);
            HttpHeaders headers = modifiedExchange.getRequest().getHeaders();
            assertEquals("auth0|123456", headers.getFirst("X-Auth0-Id"));
            return Mono.empty();
        });

        Mono<Void> result = filter.filter(exchange, filterChain)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void filter_withoutJwtPrincipal_shouldNotAddHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        TestingAuthenticationToken authentication = new TestingAuthenticationToken("user", "pass");
        SecurityContext securityContext = new SecurityContextImpl(authentication);

        when(filterChain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange modifiedExchange = invocation.getArgument(0);
            HttpHeaders headers = modifiedExchange.getRequest().getHeaders();
            assertNull(headers.getFirst("X-Auth0-Id"));
            return Mono.empty();
        });

        Mono<Void> result = filter.filter(exchange, filterChain)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));

        StepVerifier.create(result)
                .verifyComplete();
    }
}
