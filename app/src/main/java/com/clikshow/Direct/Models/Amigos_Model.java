package com.clikshow.Direct.Models;

public class Amigos_Model {
    int id;
    String thumb;
    String name;
    String username;

    public Amigos_Model(int id, String thumb, String name, String username) {
        this.id = id;
        this.thumb = thumb;
        this.name = name;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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
}
