package com.example.firesystemcs.entity;

public class Radio {
    private String rid;
    private String ip;
    private String status;

    public Radio(){    }

    public Radio(String rid, String ip, String status){
        this.rid = rid;
        this.ip = ip;
        this.status = status;
    }
}
