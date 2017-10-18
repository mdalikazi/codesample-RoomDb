package com.alikazi.cc_airtasker.models;

/**
 * Created by alikazi on 17/10/17.
 */

public class Profile {

    private int id;
    private String avatar_mini_url;
    private String first_name;
    private int rating;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setAvatar_mini_url(String avatar_mini_url) {
        this.avatar_mini_url = avatar_mini_url;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
