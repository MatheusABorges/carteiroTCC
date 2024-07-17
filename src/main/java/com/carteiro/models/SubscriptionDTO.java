package com.carteiro.models;

public class SubscriptionDTO {
    private String ip;
    private int port;
    private String textFilter;
    private Double frequency;
    private String messageType;
    private Integer period;
    private Long minTimeIntervalInMicroSecs;

    public SubscriptionDTO(String ip, int port, String textFilter, Double frequency, String messageType, Integer period, Long minTimeIntervalInMicroSecs){
        this.ip = ip;
        this.port = port;
        this.textFilter = textFilter;
        this.frequency = frequency;
        this.messageType = messageType;
        this.period = period == null || period == 0 ? null : period;
        this.minTimeIntervalInMicroSecs = minTimeIntervalInMicroSecs;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTextFilter() {
        return textFilter;
    }

    public void setTextFilter(String textFilter) {
        this.textFilter = textFilter;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getMinTimeIntervalInMicroSecs() { return minTimeIntervalInMicroSecs;}

    public void setMinTimeIntervalInMicroSecs(Long minTimeIntervalInMicroSecs) { this.minTimeIntervalInMicroSecs = minTimeIntervalInMicroSecs; }
}
