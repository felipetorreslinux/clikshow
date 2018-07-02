package com.clikshow.Fragmentos.Models;

public class FeedSlide {

    private int id;
    private String name;
    private String type;
    private int starts;
    private int ends;
    private String banner;
    private String thumb;
    private String city;
    private String state;
    private String lat;
    private String lng;
    private String classification;
    private String description;
    private String producer_name;
    private boolean is_private;
    private boolean is_favorite;

    public FeedSlide(int id, String name, String type, int starts, int ends, String banner, String thumb, String city, String state, String lat, String lng, String classification, String description, String producer_name, boolean is_private, boolean is_favorite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.starts = starts;
        this.ends = ends;
        this.banner = banner;
        this.thumb = thumb;
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
        this.classification = classification;
        this.description = description;
        this.producer_name = producer_name;
        this.is_private = is_private;
        this.is_favorite = is_favorite;
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

    public String getProducer_name() {
        return producer_name;
    }

    public void setProducer_name(String producer_name) {
        this.producer_name = producer_name;
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
