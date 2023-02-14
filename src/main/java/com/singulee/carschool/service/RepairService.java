package com.singulee.carschool.service;

import com.singulee.carschool.pojo.*;
import com.singulee.carschool.util.StaticInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Service
public class RepairService {
    final private RepairMapper repairMapper;
    final private CartMapper cartMapper;
    final private DetailMapper detailMapper;
    final private RelationshipMapper relationshipMapper;

    @Autowired
    public RepairService(RepairMapper repairMapper, CartMapper cartMapper, DetailMapper detailMapper, RelationshipMapper relationshipMapper) {
        this.repairMapper = repairMapper;
        this.cartMapper = cartMapper;
        this.detailMapper = detailMapper;
        this.relationshipMapper = relationshipMapper;
    }

    /**
     * 查询当前关联的车辆
     *
     * @param request
     * @return
     */
    public Map<String, Object> getloaclCar(HttpServletRequest request) {

        Map<String, Object> mapjson = new HashMap<>();
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        Map<String, Object> map = new HashMap<>();
        map.put("teaId", teacher.getTeaid());
        List<Map<String, Object>> mapList = repairMapper.getlocalCart(map);
        if (mapList.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", mapList);
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "当前无关联车辆");
        }
        return mapjson;
    }

    /**
     * 查看维修记录
     *
     * @param map
     * @param request
     * @return
     */
    public Map<String, Object> getHistory(Map map, HttpServletRequest request) {
        int page=1;
        int count=0;
        Map mapjson = new HashMap();

        if (map == null) {
            map = new HashMap();
        }
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        //教练
        if (teacher.getTearole().equals("0")) {
            map.put("teaId", teacher.getTeaid());
        }
        page=Integer.parseInt(map.get("page").toString());
        count=repairMapper.getHistoryCount(map);
        map.put("pageStart",(page-1)* StaticInfo.PAGE_SIZE);
        map.put("pageEnd",page* StaticInfo.PAGE_SIZE);
        List<Map<String,Object>> historyList=repairMapper.getHistory(map);

        mapjson.put("code",200);
        mapjson.put("data",historyList);
        mapjson.put("page",page);
        mapjson.put("count",count);
        mapjson.put("pageCount",count-1/page);
//        判断用户角色
//        教练 添加userid  传入时间
//        管理员  传入  name   车牌   状态


        return mapjson;
    }

    /**
     * 获得空闲车辆
     *
     * @param request
     * @return
     */
    public Map<String, Object> getNullCart(HttpServletRequest request) {
        Map map = new HashMap();
        Map mapjson = new HashMap();

        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        map.put("carDriverClass", teacher.getTeadriver());
        List<Cart> carts = repairMapper.getNullCar(map);
        if (carts.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", carts);
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "当前无备用车辆,请联系管理员");
        }
        return mapjson;
    }

    /**
     * 获得空闲车辆
     *
     * @param request
     * @return
     */
    public Map<String, Object> addReparir(Map map, HttpServletRequest request) {
        Map mapjson = new HashMap();
        //获得修改前的课程信息
        Map mapRelationship = new HashMap();
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        mapRelationship.put("teacherId", teacher.getTeaid());
        List<Map<String, Object>> detailList = repairMapper.getDetail(mapRelationship);

        //1,取消当前关联记录
        Relationship relationship = relationshipMapper.contant(mapRelationship).get(0);
        relationship.setEndtime(new Timestamp(System.currentTimeMillis()));
        relationshipMapper.update(relationship);
        //添加新关系
        Relationship newrelationship = new Relationship();
        newrelationship.setStarttime(new Timestamp(System.currentTimeMillis()));
        newrelationship.setCartid(Integer.parseInt(map.get("newCarId").toString()));
        newrelationship.setTeacherid(teacher.getTeaid());
        relationshipMapper.insert(newrelationship);
        //判断开始时间加课程与当前时间比较进行添加
        // 循环修改detail的值

        Map<String, Object> tmpMap = null;
        System.out.println(detailList);
        Iterator iterator = detailList.iterator();
        SimpleDateFormat timeForm = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> mapRelation = new HashMap<>();
        List<String> detailIdlist = new ArrayList();
        while (iterator.hasNext()) {
            tmpMap = (Map<String, Object>) iterator.next();
            try {
                if (timeForm.parse(tmpMap.get("courseBegin").toString()).getTime() + dateForm.parse(tmpMap.get("courseDate").toString()).getTime() > System.currentTimeMillis()) {
                    detailIdlist.add(tmpMap.get("detailId").toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        mapRelation.put("relationshipId", newrelationship.getRelationshipid());
        mapRelation.put("detailIdlist", detailIdlist);
        System.out.println(detailIdlist);
        if(detailIdlist.size()>0){
            detailMapper.updateRelaList(mapRelation);
        }
        //添加维修表
        Repair repair = new Repair();
        repair.setCartid(Integer.parseInt(map.get("carId").toString()));
        repair.setCause(map.get("cause").toString());
        repair.setTeacherid(teacher.getTeaid());
        repair.setExtend1("维修中");
        repair.setRepairtime(new Date(System.currentTimeMillis()));
        repairMapper.insert(repair);
        //修改两个车的状态
        Cart cartold = new Cart();
        cartold.setStaute("维修");
        cartold.setCarid(Integer.parseInt(map.get("carId").toString()));
        cartMapper.updateStute(cartold);

        Cart cartNew = new Cart();
        cartNew.setStaute("正常");
        cartNew.setCarid(Integer.parseInt(map.get("newCarId").toString()));
        cartMapper.updateStute(cartNew);

        mapjson.put("code",200);
        //修改
        return mapjson;
    }

    public Map<String, Object> repairFinish(Map map) {
        Map mapjson = new HashMap();
        Repair repair=repairMapper.selectByPrimaryKey(Integer.parseInt(map.get("repairId").toString()));
        repair.setComebacktime(new Timestamp(System.currentTimeMillis()));
        repair.setExtend1("已完成");
        repairMapper.updateByPrimaryKey(repair);
        Cart cart=cartMapper.selectById(repair.getCartid().toString());
        cart.setStaute("备用");
        if(cartMapper.update(cart)>0){
            mapjson.put("code",200);
        }
        return mapjson;
    }
}
