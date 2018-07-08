package com.clikshow.Direct.Models;

public class Rooms_Model {
    int id;
    String name;
    String username;
    String message;
    String profile_pic;

    public Rooms_Model(int id, String name, String username, String message, String profile_pic) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.message = message;
        this.profile_pic = profile_pic;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
