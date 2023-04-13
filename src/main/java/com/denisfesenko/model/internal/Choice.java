package com.denisfesenko.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
    private int index;

    public Message getMessage() {
        return message;
    }

    public Choice setMessage(Message message) {
        this.message = message;
        return this;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public Choice setFinishReason(String finishReason) {
        this.finishReason = finishReason;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Choice setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "message=" + message +
                ", finishReason='" + finishReason + '\'' +
                ", index=" + index +
                '}';
    }
}
