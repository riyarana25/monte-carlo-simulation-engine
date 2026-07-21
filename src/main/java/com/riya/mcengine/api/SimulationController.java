package com.riya.mcengine.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/simulate")
@Tag(name = "Monte Carlo Simulations", description = "Run Monte Carlo simulations on various domains")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    @PostMapping
    @Operation(
        summary = "Run a Monte Carlo simulation",
        description = "Execute a Monte Carlo simulation on one of the supported domains (buffon, portfolio_var, barrier_option, rare_event)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Simulation completed successfully",
            content = @Content(schema = @Schema(implementation = SimulationResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SimulationResponse>> simulate(@Valid @RequestBody SimulationRequest request) {
        try {
            List<SimulationResponse> results = simulationService.simulate(request);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/domains")
    @Operation(summary = "List available simulation domains")
    public ResponseEntity<List<String>> getDomains() {
        return ResponseEntity.ok(List.of("buffon", "portfolio_var", "barrier_option", "rare_event"));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("MC Engine API is running");
    }
}
