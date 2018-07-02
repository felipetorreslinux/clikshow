package com.clikshow.Carrinho.Models;

public class ListaCarrinhoModel {

    private int id;
    private int event_id;
    private String event_name;
    private String description;
    private String event_thumb;
    private int qtd;
    private double price;
    private double total;

    public ListaCarrinhoModel(int id, int event_id, String event_name, String description, String event_thumb, int qtd, double price, double total) {
        this.id = id;
        this.event_id = event_id;
        this.event_name = event_name;
        this.description = description;
        this.event_thumb = event_thumb;
        this.qtd = qtd;
        this.price = price;
        this.total = total;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent_thumb() {
        return event_thumb;
    }

    public void setEvent_thumb(String event_thumb) {
        this.event_thumb = event_thumb;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}