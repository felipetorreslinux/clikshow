package com.clikshow.Fragmentos.Models;

public class IngressosModel {
    private int id;
    private int event_id;
    private String event_name;
    private String event_thumb;
    private String ticket_name;
    private String type;
    private int status;
    private double price;
    private String description;
    private int starts;
    private int ends;
    private int qtdIngressos;

    public IngressosModel(int id, int event_id, String event_name, String event_thumb, String ticket_name, String type, int status, double price, String description, int starts, int ends, int qtdIngressos) {
        this.id = id;
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_thumb = event_thumb;
        this.ticket_name = ticket_name;
        this.type = type;
        this.status = status;
        this.price = price;
        this.description = description;
        this.starts = starts;
        this.ends = ends;
        this.qtdIngressos = qtdIngressos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_thumb() {
        return event_thumb;
    }

    public void setEvent_thumb(String event_thumb) {
        this.event_thumb = event_thumb;
    }

    public String getTicket_name() {
        return ticket_name;
    }

    public void setTicket_name(String ticket_name) {
        this.ticket_name = ticket_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }

    public int getEnds() {
        return ends;
    }

    public void setEnds(int ends) {
        this.ends = ends;
    }

    public int getQtdIngressos() {
        return qtdIngressos;
    }

    public void setQtdIngressos(int qtdIngressos) {
        this.qtdIngressos = qtdIngressos;
    }
}
