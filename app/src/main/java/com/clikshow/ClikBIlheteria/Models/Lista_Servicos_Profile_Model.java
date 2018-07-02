package com.clikshow.ClikBIlheteria.Models;

public class Lista_Servicos_Profile_Model {

    private int event_id;
    private int user_id;
    private int role_id;
    private String role_name;

    private int objeto_event_id;
    private String objeto_event_name;
    private String objeto_event_type;
    private String objeto_event_banner;
    private String objeto_event_thumb;
    private String objeto_event_city;
    private String objeto_event_state;
    private String objeto_event_lat;
    private String obejto_event_lng;
    private String obejto_event_classification;
    private String objeto_event_description;
    private int objeto_event_starts;
    private int obejto_event_ends;
    private String objeto_event_producer_id;
    private boolean objeto_event_is_private;
    private boolean objeto_event_is_favorite;

    public Lista_Servicos_Profile_Model(int event_id, int user_id, int role_id, String role_name, int objeto_event_id, String objeto_event_name, String objeto_event_type, String objeto_event_banner, String objeto_event_thumb, String objeto_event_city, String objeto_event_state, String objeto_event_lat, String obejto_event_lng, String obejto_event_classification, String objeto_event_description, int objeto_event_starts, int obejto_event_ends, String objeto_event_producer_id, boolean objeto_event_is_private, boolean objeto_event_is_favorite) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.role_id = role_id;
        this.role_name = role_name;
        this.objeto_event_id = objeto_event_id;
        this.objeto_event_name = objeto_event_name;
        this.objeto_event_type = objeto_event_type;
        this.objeto_event_banner = objeto_event_banner;
        this.objeto_event_thumb = objeto_event_thumb;
        this.objeto_event_city = objeto_event_city;
        this.objeto_event_state = objeto_event_state;
        this.objeto_event_lat = objeto_event_lat;
        this.obejto_event_lng = obejto_event_lng;
        this.obejto_event_classification = obejto_event_classification;
        this.objeto_event_description = objeto_event_description;
        this.objeto_event_starts = objeto_event_starts;
        this.obejto_event_ends = obejto_event_ends;
        this.objeto_event_producer_id = objeto_event_producer_id;
        this.objeto_event_is_private = objeto_event_is_private;
        this.objeto_event_is_favorite = objeto_event_is_favorite;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getObjeto_event_id() {
        return objeto_event_id;
    }

    public void setObjeto_event_id(int objeto_event_id) {
        this.objeto_event_id = objeto_event_id;
    }

    public String getObjeto_event_name() {
        return objeto_event_name;
    }

    public void setObjeto_event_name(String objeto_event_name) {
        this.objeto_event_name = objeto_event_name;
    }

    public String getObjeto_event_type() {
        return objeto_event_type;
    }

    public void setObjeto_event_type(String objeto_event_type) {
        this.objeto_event_type = objeto_event_type;
    }

    public String getObjeto_event_banner() {
        return objeto_event_banner;
    }

    public void setObjeto_event_banner(String objeto_event_banner) {
        this.objeto_event_banner = objeto_event_banner;
    }

    public String getObjeto_event_thumb() {
        return objeto_event_thumb;
    }

    public void setObjeto_event_thumb(String objeto_event_thumb) {
        this.objeto_event_thumb = objeto_event_thumb;
    }

    public String getObjeto_event_city() {
        return objeto_event_city;
    }

    public void setObjeto_event_city(String objeto_event_city) {
        this.objeto_event_city = objeto_event_city;
    }

    public String getObjeto_event_state() {
        return objeto_event_state;
    }

    public void setObjeto_event_state(String objeto_event_state) {
        this.objeto_event_state = objeto_event_state;
    }

    public String getObjeto_event_lat() {
        return objeto_event_lat;
    }

    public void setObjeto_event_lat(String objeto_event_lat) {
        this.objeto_event_lat = objeto_event_lat;
    }

    public String getObejto_event_lng() {
        return obejto_event_lng;
    }

    public void setObejto_event_lng(String obejto_event_lng) {
        this.obejto_event_lng = obejto_event_lng;
    }

    public String getObejto_event_classification() {
        return obejto_event_classification;
    }

    public void setObejto_event_classification(String obejto_event_classification) {
        this.obejto_event_classification = obejto_event_classification;
    }

    public String getObjeto_event_description() {
        return objeto_event_description;
    }

    public void setObjeto_event_description(String objeto_event_description) {
        this.objeto_event_description = objeto_event_description;
    }

    public int getObjeto_event_starts() {
        return objeto_event_starts;
    }

    public void setObjeto_event_starts(int objeto_event_starts) {
        this.objeto_event_starts = objeto_event_starts;
    }

    public int getObejto_event_ends() {
        return obejto_event_ends;
    }

    public void setObejto_event_ends(int obejto_event_ends) {
        this.obejto_event_ends = obejto_event_ends;
    }

    public String getObjeto_event_producer_id() {
        return objeto_event_producer_id;
    }

    public void setObjeto_event_producer_id(String objeto_event_producer_id) {
        this.objeto_event_producer_id = objeto_event_producer_id;
    }

    public boolean isObjeto_event_is_private() {
        return objeto_event_is_private;
    }

    public void setObjeto_event_is_private(boolean objeto_event_is_private) {
        this.objeto_event_is_private = objeto_event_is_private;
    }

    public boolean isObjeto_event_is_favorite() {
        return objeto_event_is_favorite;
    }

    public void setObjeto_event_is_favorite(boolean objeto_event_is_favorite) {
        this.objeto_event_is_favorite = objeto_event_is_favorite;
    }
}
