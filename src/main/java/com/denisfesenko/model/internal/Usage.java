package com.denisfesenko.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usage {
    @JsonProperty("prompt_tokens")
    private int promptTokens;
    @JsonProperty("completion_tokens")
    private int completionTokens;
    @JsonProperty("total_tokens")
    private int totalTokens;

    public int getPromptTokens() {
        return promptTokens;
    }

    public Usage setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
        return this;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public Usage setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
        return this;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public Usage setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
        return this;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "promptTokens=" + promptTokens +
                ", completionTokens=" + completionTokens +
                ", totalTokens=" + totalTokens +
                '}';
    }
}
