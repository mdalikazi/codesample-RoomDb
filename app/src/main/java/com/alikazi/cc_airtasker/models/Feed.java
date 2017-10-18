package com.alikazi.cc_airtasker.models;

/**
 * Created by alikazi on 17/10/17.
 */

public class Feed {

    private int task_id;
    private int profile_id;
    private String text;
    private String created_at;
    private String event;

    public int getTask_id() {
        return task_id;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public String getText() {
        return text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEvent() {
        return event;
    }

    public void setText(String text) {
        this.text = text;
    }
}
