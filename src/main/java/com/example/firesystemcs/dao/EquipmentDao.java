package com.example.firesystemcs.dao;

import com.example.firesystemcs.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentDao {
    public List<Equipment> getAllEquip();

    //联合dradioequip表，通过eid获取其对应的rid
    public String getRidByeid(String eid);
    //根据eid取得装备这一条记录
    public Equipment getEquipByeid(String eid);

    //根据eid获取其ip
    public String getIpByeid(String eid);
}
