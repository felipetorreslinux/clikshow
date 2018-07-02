package com.clikshow.Profile.Models;

public class ServicosProfileModel {

    private int event_id;
    private String cpf;
    private int role_id;
    private String role_name;

    private int id;
    private String name;
    private String type;
    private String banner;
    private String thumb;
    private String city;
    private String state;
    private String lat;
    private String lng;
    private String classification;
    private String description;
    private int starts;
    private int ends;
    private String producer_id;
    private boolean is_private;
    private boolean is_favorite;

    public ServicosProfileModel(int event_id, String cpf, int role_id, String role_name, int id, String name, String type, String banner, String thumb, String city, String state, String lat, String lng, String classification, String description, int starts, int ends, String producer_id, boolean is_private, boolean is_favorite) {
        this.event_id = event_id;
        this.cpf = cpf;
        this.role_id = role_id;
        this.role_name = role_name;
        this.id = id;
        this.name = name;
        this.type = type;
        this.banner = banner;
        this.thumb = thumb;
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.classification = classification;
        this.description = description;
        this.starts = starts;
        this.ends = ends;
        this.producer_id = producer_id;
        this.is_private = is_private;
        this.is_favorite = is_favorite;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    public String getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(String producer_id) {
        this.producer_id = producer_id;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}