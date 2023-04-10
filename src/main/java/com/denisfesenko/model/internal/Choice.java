package com.denisfesenko.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {
    private String text;
    private int index;
    private Object logprobs;
    @JsonProperty("finish_reason")
    private String finishReason;

    public String getText() {
        return text;
    }

    public Choice setText(String text) {
        this.text = text;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Choice setIndex(int index) {
        this.index = index;
        return this;
    }

    public Object getLogprobs() {
        return logprobs;
    }

    public Choice setLogprobs(Object logprobs) {
        this.logprobs = logprobs;
        return this;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public Choice setFinishReason(String finishReason) {
        this.finishReason = finishReason;
        return this;
    }
}
