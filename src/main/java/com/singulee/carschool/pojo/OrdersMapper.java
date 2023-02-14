package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface OrdersMapper {


    int insert(Orders record);


    int insertByNull(Orders record);

    List<Map<String, Object>> getMyOrder(Map map);

    List<Map<String, Object>> getMyOrderDate(Map map);


    Orders selectById(Integer ordid);


    List<Map> getGreadList(Map map);

    int update(Orders record);
}