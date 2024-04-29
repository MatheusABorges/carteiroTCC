package com.carteiro.models;

import org.springframework.expression.Expression;

import java.net.InetAddress;
import java.util.UUID;

public class Subscription {

    private UUID id;
    private InetAddress ip;
    private int port;
    private Expression contentFilter;
    private double ratio;
    private String messageType;

    public Subscription(SubscriptionDTO subscriptionDTO, Expression expression){
        try {
            this.ip = InetAddress.getByName(subscriptionDTO.getIp());
            this.port = subscriptionDTO.getPort();
            this.contentFilter = expression;
            this.ratio = subscriptionDTO.getRatio();
            this.id = UUID.randomUUID();
            this.messageType = subscriptionDTO.getMessageType();
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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
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
}
