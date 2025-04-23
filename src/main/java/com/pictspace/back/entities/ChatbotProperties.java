package com.pictspace.back.entities;

public class ChatbotProperties {
    private String initialGreeting;
    private ResponseProperties response = new ResponseProperties();

    public String getInitialGreeting() {
        return initialGreeting;
    }

    public void setInitialGreeting(String initialGreeting) {
        this.initialGreeting = initialGreeting;
    }

    public ResponseProperties getResponse() {
        return response;
    }

    public void setResponse(ResponseProperties response) {
        this.response = response;
    }

    public static class ResponseProperties {
        private int maxLength = 100;
        private double temperature = 0.7;

        public int getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
    }
}