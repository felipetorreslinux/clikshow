package com.clikshow.Direct.Models;

public class Rooms_Model {
    String id;
    String name;
    String username;
    String profile_pic;

    public Rooms_Model(String id, String name, String username, String profile_pic) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profile_pic = profile_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
