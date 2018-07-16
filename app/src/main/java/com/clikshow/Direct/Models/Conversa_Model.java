package com.clikshow.Direct.Models;

public class Conversa_Model {
    String create_at;
    String message;
    String receiver;
    String sender;

    public Conversa_Model(String create_at, String message, String receiver, String sender) {
        this.create_at = create_at;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
