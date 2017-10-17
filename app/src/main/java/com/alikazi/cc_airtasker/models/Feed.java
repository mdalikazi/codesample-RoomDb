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

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
