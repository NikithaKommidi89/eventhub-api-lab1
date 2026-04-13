package com.eventhub.api.dto.ai;

import jakarta.validation.constraints.NotBlank;

public class FaqSearchRequest {

    @NotBlank(message = "Query cannot be blank")
    private String query;

    private int topK = 3;
    private double threshold = 0.7;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}