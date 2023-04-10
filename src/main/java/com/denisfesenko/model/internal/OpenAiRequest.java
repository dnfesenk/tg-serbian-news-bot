package com.denisfesenko.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenAiRequest {
    private String model;
    private String prompt;
    @JsonProperty("max_tokens")
    private int maxTokens;
    private float temperature;

    public String getModel() {
        return model;
    }

    public OpenAiRequest setModel(String model) {
        this.model = model;
        return this;
    }

    public String getPrompt() {
        return prompt;
    }

    public OpenAiRequest setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public OpenAiRequest setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
        return this;
    }

    public float getTemperature() {
        return temperature;
    }

    public OpenAiRequest setTemperature(float temperature) {
        this.temperature = temperature;
        return this;
    }
}
