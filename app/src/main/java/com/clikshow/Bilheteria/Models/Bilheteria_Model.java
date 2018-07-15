package com.clikshow.Bilheteria.Models;

public class Bilheteria_Model {

    private int id;
    private int event_id;
    private String event_name;
    private String event_thumb;
    private String type;
    private int status;
    private double price;
    private String description;
    private String name;
    private int starts;
    private int ends;

    public Bilheteria_Model(int id, int event_id, String event_name, String event_thumb, String type, int status, double price, String description, String name, int starts, int ends) {
        this.id = id;
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_thumb = event_thumb;
        this.type = type;
        this.status = status;
        this.price = price;
        this.description = description;
        this.name = name;
        this.starts = starts;
        this.ends = ends;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
