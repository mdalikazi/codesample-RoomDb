package com.alikazi.codesample_roomdb.models;

/**
 * Created by alikazi on 17/10/17.
 */

public class Profile {

    public int id;
    public String avatar_mini_url;
    public String first_name;
    public int rating;

    public transient String avatarFullUrl;
}
