package com.example.firesystemcs.dao;

import com.example.firesystemcs.entity.Radio;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RadioDao {
    public void addARadio(Radio radio);
}
