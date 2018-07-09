package com.clikshow.Comments.Model;

public class Comment_Model {
    String event_id;
    String user_id;
    String name;
    String username;
    String thumb;
    String comment;
    String type;
    String create_at;
    String is_like;
    String count_like;

    public Comment_Model(String event_id, String user_id, String name, String username, String thumb, String comment, String type, String create_at, String is_like, String count_like) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.name = name;
        this.username = username;
        this.thumb = thumb;
        this.comment = comment;
        this.type = type;
        this.create_at = create_at;
        this.is_like = is_like;
        this.count_like = count_like;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getCount_like() {
        return count_like;
    }

    public void setCount_like(String count_like) {
        this.count_like = count_like;
    }
}
