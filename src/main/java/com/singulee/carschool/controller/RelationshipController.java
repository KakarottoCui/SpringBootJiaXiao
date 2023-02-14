package com.singulee.carschool.controller;

import com.singulee.carschool.pojo.Relationship;
import com.singulee.carschool.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Controller
@RequestMapping("/relation")
public class RelationshipController {
    private RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    /**
     * 添加关系列表
     * @param relationship
     * @return
     */

    @RequestMapping(value = "/addrelation")
    @ResponseBody
    public Map<String,Object> addRelationship(@RequestBody Relationship relationship){
        return relationshipService.addRelationship(relationship);
    }

    /**
     * 获得relationship的id
     * @param relationship
     * @return
     */
    @RequestMapping(value = "/updaterelation")
    @ResponseBody
    public Map<String,Object> updateRelationship(@RequestBody Relationship relationship){
        return relationshipService.updateRelationship(relationship);
    }

    /**
     * 获得空闲教练
     * @param map
     * @return
     */
    @RequestMapping(value = "/getEmptyTeacher")
    @ResponseBody
    public Map<String,Object> getEmptyTeacher(@RequestBody(required = false)Map map){
        return relationshipService.getTeacher(map);
    }

    /**
     * 获得空闲车辆
     * @param map
     * @return
     */
    @RequestMapping(value = "/getEmptyCar")
    @ResponseBody
    public Map<String,Object> getEmptyCar(@RequestBody(required = false)Map map){
        return relationshipService.getCart(map);
    }

    /**
     * 获得当前关系
     * @param map
     * @return
     */
    @RequestMapping(value = "/getNow")
    @ResponseBody
    public Map<String,Object> getNowRelationship(@RequestBody(required = false)Map map){
        return relationshipService.getContant(map);
    }

    /**
     * 获得历史关系
     * @param map
     * @return
     */
    @RequestMapping(value = "/getHistory")
    @ResponseBody
    public Map<String,Object> getHistory(@RequestBody(required = false)Map map){
        return relationshipService.getHistory(map);
    }





}
