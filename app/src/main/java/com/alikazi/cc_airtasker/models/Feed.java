package com.alikazi.cc_airtasker.models;

import java.util.Date;

/**
 * Created by alikazi on 17/10/17.
 */

public class Feed {

    private int task_id;
    private int profile_id;
    private String text;
    private String created_at;
    private String event;

    private transient Task task;
    private transient Profile profile;
    private transient String processedText;
    private transient Date createdAtJavaDate;

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

    public Task getTask() {
        return task;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getProcessedText() {
        return processedText;
    }

    public Date getCreatedAtJavaDate() {
        return createdAtJavaDate;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProcessedText(String processedText) {
        this.processedText = processedText;
    }

    public void setCreatedAtJavaDate(Date date) {
        this.createdAtJavaDate = date;
    }
}
