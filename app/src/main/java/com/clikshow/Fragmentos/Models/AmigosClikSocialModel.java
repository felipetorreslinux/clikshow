package com.clikshow.Fragmentos.Models;

public class AmigosClikSocialModel {
    private int user_id;
    private String profile_pic_thumb;
    private String username;
    private String name;
    private boolean is_following;

    public AmigosClikSocialModel(int user_id, String profile_pic_thumb, String username, String name, boolean is_following) {
        this.user_id = user_id;
        this.profile_pic_thumb = profile_pic_thumb;
        this.username = username;
        this.name = name;
        this.is_following = is_following;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProfile_pic_thumb() {
        return profile_pic_thumb;
    }

    public void setProfile_pic_thumb(String profile_pic_thumb) {
        this.profile_pic_thumb = profile_pic_thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_following() {
        return is_following;
    }

    public void setIs_following(boolean is_following) {
        this.is_following = is_following;
    }
}
