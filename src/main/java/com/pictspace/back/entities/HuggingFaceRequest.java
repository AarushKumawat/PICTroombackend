package com.pictspace.back.entities;

import java.util.HashMap;
import java.util.Map;

public class HuggingFaceRequest {
    private String inputs;
    private Map<String, Object> parameters;

    public HuggingFaceRequest(String inputs) {
        this.inputs = inputs;
        this.parameters = new HashMap<>();
        // Default parameters
        this.parameters.put("max_length", 100);
        this.parameters.put("temperature", 0.7);
    }

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}