package com.example.firesystemcs.entity;

import java.sql.Timestamp;
import java.util.Date;

public class Pos {
    private String eid;
    private String rid;
    private String time;//类型待定
    private String lat;
    private String lon;
    private String height;

    public Pos() {}

    public Pos(String eid, String rid, String time, String lat, String lon, String height) {
        this.eid = eid;
        this.rid = rid;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
        this.height = height;
    }

    public Pos(String eid, String rid, String lat, String lon, String height) {
        this.eid = eid;
        this.rid = rid;
        this.lat = lat;
        this.lon = lon;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "eid='" + eid + '\'' +
                ", rid='" + rid + '\'' +
                ", time=" + time +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", height='" + height + '\'' +
                '}';
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
