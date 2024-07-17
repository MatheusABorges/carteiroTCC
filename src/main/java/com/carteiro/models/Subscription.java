package com.carteiro.models;

import org.springframework.expression.Expression;

import java.net.InetAddress;
import java.util.UUID;

public class Subscription {

    private UUID id;
    private InetAddress ip;
    private int port;
    private Expression contentFilter;
    private String messageType;
    private Integer period;
    private Double frequency;
    private Long minTimeIntervalInNanoSecs;

    public Subscription(SubscriptionDTO subscriptionDTO, Expression expression){
        try {
            this.ip = InetAddress.getByName(subscriptionDTO.getIp());
            this.port = subscriptionDTO.getPort();
            this.contentFilter = expression;
            this.id = UUID.randomUUID();
            this.messageType = subscriptionDTO.getMessageType();
            this.period =  subscriptionDTO.getPeriod();
            this.frequency = subscriptionDTO.getFrequency();
            this.minTimeIntervalInNanoSecs = subscriptionDTO.getMinTimeIntervalInMicroSecs() != null ? subscriptionDTO.getMinTimeIntervalInMicroSecs()*1000 : null;
        } catch(Exception e){
            System.out.println("Could not instantiate subscription due to its IP address");
        }
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Expression getContentFilter() {
        return contentFilter;
    }

    public void setContentFilter(Expression contentFilter) {
        this.contentFilter = contentFilter;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public Long getMinTimeIntervalInNanoSecs() { return minTimeIntervalInNanoSecs; }

    public void setMinTimeIntervalInNanoSecs(Long minTimeIntervalInNanoSecs) { this.minTimeIntervalInNanoSecs = minTimeIntervalInNanoSecs; }
}
