package com.example.videochatapp;

public class Story {

    private String imageUrl;
    private long timestart;
    private long timeend;
    private String storyId;
    private String userId;

    public Story(String imageUrl, long timestart, long timeend, String storyId, String userId) {
        this.imageUrl = imageUrl;
        this.timestart = timestart;
        this.timeend = timeend;
        this.storyId = storyId;
        this.userId = userId;
    }

    public Story(){

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
