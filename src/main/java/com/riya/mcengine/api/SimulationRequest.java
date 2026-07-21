package com.riya.mcengine.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to run a Monte Carlo simulation")
public class SimulationRequest {

    @NotNull(message = "domain is required")
    @Schema(description = "Simulation domain", example = "buffon", allowableValues = {"buffon", "portfolio_var", "barrier_option", "rare_event"})
    private String domain;

    @NotNull(message = "samples is required")
    @Min(value = 1, message = "samples must be > 0")
    @Schema(description = "Number of samples per replication", example = "10000")
    private Integer samples;

    @Schema(description = "Number of replications", example = "100")
    private Integer replications = 100;

    @Min(value = 1, message = "threads must be > 0")
    @Schema(description = "Number of threads for parallel execution", example = "4")
    private Integer threads = 1;

    @Schema(description = "Random seed for reproducibility", example = "42")
    private Long seed = 42L;

    public SimulationRequest() {}

    public SimulationRequest(String domain, Integer samples, Integer replications, Integer threads, Long seed) {
        this.domain = domain;
        this.samples = samples;
        this.replications = replications;
        this.threads = threads;
        this.seed = seed;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getSamples() {
        return samples;
    }

    public void setSamples(Integer samples) {
        this.samples = samples;
    }

    public Integer getReplications() {
        return replications;
    }

    public void setReplications(Integer replications) {
        this.replications = replications;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }
}
