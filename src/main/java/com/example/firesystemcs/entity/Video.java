package com.example.firesystemcs.entity;

public class Video {
    private String eid;
    private String time;
    private String videoname;

    public Video(String eid, String time, String videoname) {
        this.eid = eid;
        this.time = time;
        this.videoname = videoname;
    }

    public Video() {
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }
}
