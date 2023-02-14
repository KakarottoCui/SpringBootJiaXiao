package com.singulee.carschool.service;

import com.singulee.carschool.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Service
public class OrdersService {
    final private OrdersMapper ordersMapper;
    final private DetailMapper detailMapper;

    @Autowired
    public OrdersService(OrdersMapper ordersMapper, DetailMapper detailMapper) {
        this.ordersMapper = ordersMapper;
        this.detailMapper = detailMapper;
    }

    /**
     * 查询当前预约的课程  学员
     * courseClass  courseDate  studentId  stautes(0 预约 1 取消  不限)
     *
     * @param map
     * @param request
     * @return
     */
    public Map<String, Object> getMyOrder(Map map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        if (map == null) {
            map = new HashMap();
        }
        Student student = (Student) request.getSession().getAttribute("user");
        map.put("studentId", student.getStuid());

        List<Map<String, Object>> listOder = ordersMapper.getMyOrder(map);
        if (listOder.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", listOder);
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "当前无预约课程");
        }
        return mapjson;
    }
    /**
     * 查询当前预约的课程  学员
     * courseClass  courseDate  studentId teacherId  stautes
     *
     * @param map
     * @param request
     * @return
     */
    public Map<String, Object> getOrderDeatil(Map map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        if (map == null) {
            map = new HashMap();
        }
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        map.put("teacherId", teacher.getTeaid());
        map.put("stautes",0);
        List<Map<String, Object>> listOder = ordersMapper.getMyOrder(map);
        if (listOder.size()> 0) {
            mapjson.put("code", 200);
            mapjson.put("data", listOder);
        }else {
            mapjson.put("code",201);
        }
        return mapjson;
    }

    /**
     * 取消预约
     *
     * @param request
     * @return
     */
    public Map<String, Object> canelOrder(Orders orders, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        Orders ordersTmp = ordersMapper.selectById(orders.getOrdid());
        Detail detail = detailMapper.selectById(ordersTmp.getDetailid());
        if (detail.getCourseDate().getTime() - System.currentTimeMillis() < 1000 * 60 * 60 * 24) {
            mapjson.put("code", 201);
            mapjson.put("msg", "课程前24小时不能取消预约");
            return mapjson;
        }
        ordersTmp.setStates(1);
        ordersMapper.update(ordersTmp);
        detail.setStusta("0");
        detailMapper.updateById(detail);
        //修改订单表
        mapjson.put("code",200);
        mapjson.put("msg","取消成功");

        return mapjson;
    }



    /**
     * 用户查看某订单的详情
     * courseClass  courseDate  studentId teacherId  stautes
     *
     * @param map
     * @return
     */
    public Map<String, Object> getOrderInfo(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        if (map == null) {
            map = new HashMap();
        }
        List<Map<String, Object>> listOder = ordersMapper.getMyOrder(map);
        if (listOder.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", listOder);
        }
        return mapjson;
    }

    /**
     * 保存日志
     * @param map
     * @return
     */
    public Map<String, Object> save(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        Orders orders=ordersMapper.selectById(Integer.parseInt(map.get("orderId").toString()));
        orders.setGrade(Integer.parseInt(map.get("grade").toString()));
        orders.setLog(map.get("log").toString());
        orders.setLogdate(new Timestamp(System.currentTimeMillis()));
        if(ordersMapper.update(orders)>0){
            mapjson.put("code",200);
        }

        return mapjson;
    }

    /**
     * 获得评分
     * @param map
     * @return
     */
    public  Map<String, Object> getGreadList(Map map){
        Map<String, Object> mapjson = new HashMap<>();

        List<Map> greadList=ordersMapper.getGreadList(map);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>."+greadList);
        if (greadList.size()>0){
        mapjson.put("code",200);
        mapjson.put("data",greadList);
        }else {
            mapjson.put("code",201);
        }



        return mapjson;

    }

}
