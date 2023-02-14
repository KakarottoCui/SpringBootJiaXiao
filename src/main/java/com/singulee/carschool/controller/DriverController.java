package com.singulee.carschool.controller;


import com.singulee.carschool.pojo.Driver;
import com.singulee.carschool.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Controller
@RequestMapping("/driver")
public class DriverController {
    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @ResponseBody
    @RequestMapping(value = "getDriverList")
    public Map<String,Object> getDriverList(){
        Map<String,Object> mapjson=new HashMap<>();
        List<Driver> driverList=driverService.getDriverList();
        if(driverList.size()>0){
            mapjson.put("code",200);
            mapjson.put("data",driverList);
        }
        else {
            mapjson.put("code",201);
            mapjson.put("data","数据不存在");
        }
        return mapjson;

    }




}
