package com.example.firesystemcs.entity;

public class Equipment {
    private String eid;
    private String ename;
    private String ip;
    private String status;

    public Equipment(){}
    public Equipment(String eid, String ip, String status, String ename){
        this.eid = eid;
        this.ename = ename;
        this.ip = ip;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "eid='" + eid + '\'' +
                ", ename='" + ename + '\'' +
                ", ip='" + ip + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


