package com.clikshow.Fragmentos.Models;

public class MeusIngressosModel {

    private int my_pass_id;
    private int pass_id;
    private String pass_name;
    private String pass_description;
    private double price;
    private String cpf;
    private int status;
    private String event_thumb;
    private int payment_type;
    private String origin;
    private int starts;
    private int ends;

    public MeusIngressosModel(int my_pass_id, int pass_id, String pass_name, String pass_description, double price, String cpf, int status, String event_thumb, int payment_type, String origin, int starts, int ends) {
        this.my_pass_id = my_pass_id;
        this.pass_id = pass_id;
        this.pass_name = pass_name;
        this.pass_description = pass_description;
        this.price = price;
        this.cpf = cpf;
        this.status = status;
        this.event_thumb = event_thumb;
        this.payment_type = payment_type;
        this.origin = origin;
        this.starts = starts;
        this.ends = ends;
    }

    public int getMy_pass_id() {
        return my_pass_id;
    }

    public void setMy_pass_id(int my_pass_id) {
        this.my_pass_id = my_pass_id;
    }

    public int getPass_id() {
        return pass_id;
    }

    public void setPass_id(int pass_id) {
        this.pass_id = pass_id;
    }

    public String getPass_name() {
        return pass_name;
    }

    public void setPass_name(String pass_name) {
        this.pass_name = pass_name;
    }

    public String getPass_description() {
        return pass_description;
    }

    public void setPass_description(String pass_description) {
        this.pass_description = pass_description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEvent_thumb() {
        return event_thumb;
    }

    public void setEvent_thumb(String event_thumb) {
        this.event_thumb = event_thumb;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
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
