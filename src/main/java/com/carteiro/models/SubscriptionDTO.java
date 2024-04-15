package com.carteiro.models;

public class SubscriptionDTO {
    private String ip;
    private int port;
    private String textFilter;
    private double ratio;

    public SubscriptionDTO(String ip, int port, String textFilter, double ratio){
        this.ip = ip;
        this.port = port;
        this.textFilter = textFilter;
        this.ratio = ratio;
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

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
