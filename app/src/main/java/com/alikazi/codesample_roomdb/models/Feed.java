package com.alikazi.codesample_roomdb.models;

import java.util.Date;

/**
 * Created by alikazi on 17/10/17.
 */

public class Feed {

    public int task_id;
    public int profile_id;
    public String text;
    public String created_at;
    public String event;

    public transient Task task;
    public transient Profile profile;
    public transient String processedText;
    public transient Date createdAtJavaDate;
}
