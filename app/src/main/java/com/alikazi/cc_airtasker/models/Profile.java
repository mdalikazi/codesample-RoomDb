package com.alikazi.cc_airtasker.models;

/**
 * Created by alikazi on 17/10/17.
 */

public class Profile {

    private int id;
    private String avatar_mini_url;
    private String first_name;
    private int rating;

    private transient String avatarFullUrl;

    public int getId() {
        return id;
    }

    public String getAvatar_mini_url() {
        return avatar_mini_url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public int getRating() {
        return rating;
    }

    public String getAvatarFullUrl() {
        return avatarFullUrl;
    }

    public void setAvatarFullUrl(String fullUrl) {
        this.avatarFullUrl = fullUrl;
    }
}
