package com.singulee.carschool.controller;


import com.singulee.carschool.pojo.Orders;
import com.singulee.carschool.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Controller
@RequestMapping("/order")
public class OrdersController {
    private OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * 查询我的课程
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("/getMyOrder")
    @ResponseBody
    public Map<String,Object> getMyOrder(@RequestBody Map map, HttpServletRequest request){
        return ordersService.getMyOrder(map,request);
    }

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    @RequestMapping("/canelOrder")
    @ResponseBody
    public Map<String,Object> canelOrder(@RequestBody Orders orders, HttpServletRequest request){
        return ordersService.canelOrder(orders,request);
    }
    /**
     * 查看预定的学员
     * courseClass  courseDate  studentId teacherId  stautes
     *
     * @param request
     * @return
     */
    @RequestMapping("/getOrderDeatil")
    @ResponseBody
    public Map<String,Object> getOrderDeatil(@RequestBody Map map, HttpServletRequest request){
        return ordersService.getOrderDeatil(map,request);
    }
    /**
     * 用户查看订单详情
     *
     *
     * @return
     */
    @RequestMapping("/getOrderInfo")
    @ResponseBody
    public Map<String,Object> getOrderInfo(@RequestBody Map map){
        return ordersService.getOrderInfo(map);
    }

    /**
     * 保存日志
     * @param map
     * @return
     */
    @RequestMapping("/saveLog")
    @ResponseBody
    public Map<String,Object> saveLog(@RequestBody Map map){
        return ordersService.save(map);
    }

    /**
     * 传入  type类型(7 一周,30 一月) studentId:  日期 date:
     * @param map
     * @return
     */
    @RequestMapping("/getGreadList")
    @ResponseBody
    public Map<String,Object> getGreadList(@RequestBody Map map)
    {
        return ordersService.getGreadList(map);
    }


}
