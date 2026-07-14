package com.sanosysalvos.apigateway.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PropagateUserHeaderFilterTest {

    private PropagateUserHeaderFilter filter;
    private GatewayFilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new PropagateUserHeaderFilter();
        chain = mock(GatewayFilterChain.class);
    }

    @Test
    void testFilterWithJwtPrincipal() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600),
                Map.of("alg", "none"), Map.of("sub", "auth0|12345"));
        Authentication authentication = new JwtAuthenticationToken(jwt);
        SecurityContext context = new SecurityContextImpl(authentication);

        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange ex = invocation.getArgument(0);
            assertEquals("auth0|12345", ex.getRequest().getHeaders().getFirst("X-Auth0-Id"));
            return Mono.empty();
        });

        Mono<Void> result = filter.filter(exchange, chain)
                .contextWrite(org.springframework.security.core.context.ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testFilterWithoutJwtPrincipal() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(chain.filter(any(ServerWebExchange.class))).thenAnswer(invocation -> {
            ServerWebExchange ex = invocation.getArgument(0);
            assertNull(ex.getRequest().getHeaders().getFirst("X-Auth0-Id"));
            return Mono.empty();
        });

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
