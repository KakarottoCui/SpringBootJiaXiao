package com.singulee.carschool.pojo;

public interface MessageMapper {

    int deleteByPrimaryKey(Integer messageid);


    int insert(Message record);


    int insertSelective(Message record);


    Message selectByPrimaryKey(Integer messageid);


    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);
}