package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface RelationshipMapper {




    /**
     * 插入
     * @param record
     * @return
     */
    int insert(Relationship record);

    /**
     * 修改
     * @param relationship
     * @return
     */
    int update(Relationship relationship);

    /**
     * 获得记录历史
     * @param map
     * @return
     */
    List<Relationship> getHistory(Map map);

    /**
     * 获取当前关联关系
     * @param map
     * @return
     */
    List<Relationship> contant(Map map);

    /**
     * 获得空闲车辆
     * @param map
     * @return
     */
    List<Cart> emptyCar(Map map);

    /**
     * 获得空闲教练
     * @param map
     * @return
     */
    List<Teacher> emptyTeacher(Map map);

}