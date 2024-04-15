package com.carteiro.models;

import java.net.InetAddress;
import java.util.UUID;

public class Subscription {

    private UUID id;
    private InetAddress ip;
    private int port;
    private String textFilter;
    private double ratio;

    public Subscription(String ip, int port, String textFilter, double ratio, UUID id){
        try {
            this.ip = InetAddress.getByName(ip);
            this.port = port;
            this.textFilter = textFilter;
            this.ratio = ratio;
            this.id = id;
        } catch(Exception e){
            System.out.println("Could not instantiate subscription due to its IP address ID: " + id.toString());
        }
    }

    public Subscription(SubscriptionDTO subscriptionDTO){
        try {
            this.ip = InetAddress.getByName(subscriptionDTO.getIp());
            this.port = subscriptionDTO.getPort();
            this.textFilter = subscriptionDTO.getTextFilter();
            this.ratio = subscriptionDTO.getRatio();
            this.id = UUID.randomUUID();
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

    public String getTextFilter() {
        return textFilter;
    }

    public void setTextFilter(String textFilter) {
        this.textFilter = textFilter;
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
}
