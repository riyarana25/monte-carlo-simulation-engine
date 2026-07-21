package com.riya.mcengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Monte Carlo Simulation Engine API",
        version = "1.0",
        description = "REST API for Monte Carlo simulations with variance reduction and parallel execution"
    )
)
public class McEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(McEngineApplication.class, args);
    }
}
