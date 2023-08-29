package com.example.firesystemcs.dao;

import com.example.firesystemcs.entity.Pos;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PosDao {
    public void addPos(Pos pos);

    public Pos getByeid(String eid);
}
