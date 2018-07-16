package com.clikshow.Direct.Models;

public class Usuarios_Online_Model {
    String id;
    String name;
    String username;
    String thumb;

    public Usuarios_Online_Model(String id, String name, String username, String thumb) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.thumb = thumb;
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
