package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface RepairMapper {
    /**
     * 获得总数据
     * @param map
     * @return
     */
    Integer getHistoryCount(Map map);

    /**
     * 获得返回数据
     * @param map
     * @return
     */
   List<Map<String,Object>> getHistory(Map map);

    /**
     * 当前使用的车辆
     * @param map
     * @return
     */
    List<Map<String,Object>> getlocalCart(Map map);

    /**
     * 获得空闲车辆
     * @param map
     * @return
     */
    List<Cart> getNullCar(Map map);


   List<Map<String,Object>> getDetail(Map map);

    int deleteByPrimaryKey(Integer repairid);


    int insert(Repair record);


    int insertSelective(Repair record);


    Repair selectByPrimaryKey(Integer repairid);


    int updateByPrimaryKeySelective(Repair record);

    int updateByPrimaryKey(Repair record);
}