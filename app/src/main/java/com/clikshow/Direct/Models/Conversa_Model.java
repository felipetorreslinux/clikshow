package com.clikshow.Direct.Models;

public class Conversa_Model {
    int id;
    int de;
    int para;
    String name;
    String username;
    String thumb;
    String message;
    String data;

    public Conversa_Model(int id, int de, int para, String name, String username, String thumb, String message, String data) {
        this.id = id;
        this.de = de;
        this.para = para;
        this.name = name;
        this.username = username;
        this.thumb = thumb;
        this.message = message;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDe() {
        return de;
    }

    public void setDe(int de) {
        this.de = de;
    }

    public int getPara() {
        return para;
    }

    public void setPara(int para) {
        this.para = para;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
