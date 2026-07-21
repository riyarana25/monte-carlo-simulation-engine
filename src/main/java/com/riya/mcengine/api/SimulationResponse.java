package com.riya.mcengine.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of a Monte Carlo simulation")
public class SimulationResponse {

    @Schema(description = "Simulation domain", example = "buffon")
    private String domain;

    @Schema(description = "Estimator used", example = "Naive")
    private String estimator;

    @Schema(description = "Point estimate (e.g., π, VaR, price)", example = "3.14159")
    private double pointEstimate;

    @Schema(description = "Standard error of the estimate", example = "0.004")
    private double standardError;

    @Schema(description = "Bias (estimate - ground truth)", example = "0.002")
    private double bias;

    @Schema(description = "Mean squared error", example = "0.0019")
    private double mse;

    @Schema(description = "Lower bound of 95% confidence interval", example = "3.135")
    private double ciLower;

    @Schema(description = "Upper bound of 95% confidence interval", example = "3.152")
    private double ciUpper;

    @Schema(description = "Whether the true value is contained in the CI", example = "true")
    private boolean containsTruth;

    @Schema(description = "Variance reduction factor (if applicable)", example = "3.45")
    private Double varianceReduction;

    @Schema(description = "Correlation between X and control variate (if applicable)", example = "0.84")
    private Double correlation;

    @Schema(description = "Computation time in milliseconds", example = "1234")
    private long computeTimeMs;

    public SimulationResponse() {}

    public SimulationResponse(String domain, String estimator, double pointEstimate, double standardError,
                            double bias, double mse, double ciLower, double ciUpper, boolean containsTruth,
                            long computeTimeMs) {
        this.domain = domain;
        this.estimator = estimator;
        this.pointEstimate = pointEstimate;
        this.standardError = standardError;
        this.bias = bias;
        this.mse = mse;
        this.ciLower = ciLower;
        this.ciUpper = ciUpper;
        this.containsTruth = containsTruth;
        this.computeTimeMs = computeTimeMs;
    }

    // Getters and setters
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getEstimator() { return estimator; }
    public void setEstimator(String estimator) { this.estimator = estimator; }

    public double getPointEstimate() { return pointEstimate; }
    public void setPointEstimate(double pointEstimate) { this.pointEstimate = pointEstimate; }

    public double getStandardError() { return standardError; }
    public void setStandardError(double standardError) { this.standardError = standardError; }

    public double getBias() { return bias; }
    public void setBias(double bias) { this.bias = bias; }

    public double getMse() { return mse; }
    public void setMse(double mse) { this.mse = mse; }

    public double getCiLower() { return ciLower; }
    public void setCiLower(double ciLower) { this.ciLower = ciLower; }

    public double getCiUpper() { return ciUpper; }
    public void setCiUpper(double ciUpper) { this.ciUpper = ciUpper; }

    public boolean isContainsTruth() { return containsTruth; }
    public void setContainsTruth(boolean containsTruth) { this.containsTruth = containsTruth; }

    public Double getVarianceReduction() { return varianceReduction; }
    public void setVarianceReduction(Double varianceReduction) { this.varianceReduction = varianceReduction; }

    public Double getCorrelation() { return correlation; }
    public void setCorrelation(Double correlation) { this.correlation = correlation; }

    public long getComputeTimeMs() { return computeTimeMs; }
    public void setComputeTimeMs(long computeTimeMs) { this.computeTimeMs = computeTimeMs; }
}
