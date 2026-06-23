package com.bpb.APIgateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/products")
    public Mono<String> orderFallback() {
        return Mono.just("Product Service is currently unavailable. Please try again later.");
    }
}
