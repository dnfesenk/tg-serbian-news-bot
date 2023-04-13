package com.denisfesenko.model.internal;

import java.util.List;

public class OpenAiResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    public String getId() {
        return id;
    }

    public OpenAiResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getObject() {
        return object;
    }

    public OpenAiResponse setObject(String object) {
        this.object = object;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public OpenAiResponse setCreated(long created) {
        this.created = created;
        return this;
    }

    public String getModel() {
        return model;
    }

    public OpenAiResponse setModel(String model) {
        this.model = model;
        return this;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public OpenAiResponse setChoices(List<Choice> choices) {
        this.choices = choices;
        return this;
    }

    public Usage getUsage() {
        return usage;
    }

    public OpenAiResponse setUsage(Usage usage) {
        this.usage = usage;
        return this;
    }

    @Override
    public String toString() {
        return "OpenAiResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", choices=" + choices +
                ", usage=" + usage +
                '}';
    }
}
