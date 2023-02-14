package com.singulee.carschool.controller;

import com.singulee.carschool.service.RepairService;
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
@RequestMapping(value = "/repair")
public class RepairController {
    private RepairService repairService;
    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }



    /**
     * 获得当前车辆
     * @return
     */
    @RequestMapping(value = "/getlocalCart")
    @ResponseBody
    public Map<String,Object> getloaclCart(@RequestBody (required = false) Map map,HttpServletRequest request){

        return  repairService.getloaclCar(request);
    }

    /**
     * 获得关联历史
     * @param map
     * @return
     */
    @RequestMapping(value = "/getHistory")
    @ResponseBody
    public Map<String,Object> getHistory(@RequestBody  (required = false) Map map,HttpServletRequest request){
        return repairService.getHistory(map, request);
    }

    /**
     * 获得空闲车辆
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getNullCart")
    @ResponseBody
    public Map<String,Object> getNullCart(@RequestBody  (required = false) Map map,HttpServletRequest request){
        return repairService.getNullCart(request);
    }
    /**
     *添加维修记录并重新分配车辆 carId: $("#carId").val(), cause:cause,newCarId:newCarNumber
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/addrepair")
    @ResponseBody
    public Map<String,Object> addRepair(@RequestBody Map map,HttpServletRequest request){
        return repairService.addReparir(map,request);
    }
    @RequestMapping(value = "/repairFinish")
    @ResponseBody
    public Map<String,Object> addRepair(@RequestBody Map map){
        return repairService.repairFinish(map);
    }
}
