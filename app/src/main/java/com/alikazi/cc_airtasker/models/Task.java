package com.alikazi.cc_airtasker.models;

/**
 * Created by alikazi on 17/10/17.
 */

public class Task {

    private int id;
    private String name;
    private String description;
    private String state;
    private int poster_id;
    private int worker_id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public int getPoster_id() {
        return poster_id;
    }

    public int getWorker_id() {
        return worker_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPoster_id(int poster_id) {
        this.poster_id = poster_id;
    }

    public void setWorker_id(int worker_id) {
        this.worker_id = worker_id;
    }
}
