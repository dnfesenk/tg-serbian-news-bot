package com.denisfesenko.model.internal;

import java.util.List;

public class OpenAiRequest {
    private String model;
    private List<Message> messages;
    private double temperature;

    public String getModel() {
        return model;
    }

    public OpenAiRequest setModel(String model) {
        this.model = model;
        return this;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public OpenAiRequest setMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    public double getTemperature() {
        return temperature;
    }

    public OpenAiRequest setTemperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    @Override
    public String toString() {
        return "OpenAiRequest{" +
                "model='" + model + '\'' +
                ", messages=" + messages +
                ", temperature=" + temperature +
                '}';
    }
}
