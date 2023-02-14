package com.singulee.carschool.service;

import com.singulee.carschool.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RelationshipService {
    final private RelationshipMapper relationshipMapper;
    final private CartMapper cartMapper;
    final private TeacherMapper teacherMapper;

    @Autowired
    public RelationshipService(RelationshipMapper relationshipMapper, CartMapper cartMapper, TeacherMapper teacherMapper) {
        this.relationshipMapper = relationshipMapper;
        this.cartMapper = cartMapper;
        this.teacherMapper = teacherMapper;
    }

    /**
     * 添加关系
     *
     * @return
     */
    public Map<String, Object> addRelationship(Relationship relationship) {
        Map<String, Object> mapjson = new HashMap<>();
        //检查车辆状态
        Cart cart = cartMapper.selectById(relationship.getCartid().toString());
        Teacher teacher = teacherMapper.selectByTeaId(relationship.getTeacherid());
        if (!teacher.getTeadriver().equals(cart.getCardriverclass())) {
            mapjson.put("code", 201);
            mapjson.put("msg", "驾照类型不匹配");
            return mapjson;
        }
        if (!cart.getStaute().equals("备用")) {
            mapjson.put("code", 201);
            mapjson.put("msg", "车辆状态不可用");
            return mapjson;
        }
        relationship.setStarttime(new Timestamp(System.currentTimeMillis()));
        if (relationshipMapper.insert(relationship) > 0) {
            cart.setStaute("正常");
            cartMapper.update(cart);
            //修改课程关系



            mapjson.put("code", 200);
            mapjson.put("msg", "关联成功");
        }
        return mapjson;
    }

    /**
     * 修改关系 结束关系
     *
     * @return
     */
    public Map<String, Object> updateRelationship(Relationship relationship) {
        Map<String, Object> mapjson = new HashMap<>();
        relationship.setEndtime(new Timestamp(System.currentTimeMillis()));
        if (relationshipMapper.update(relationship) > 0) {
            mapjson.put("code", 200);
            mapjson.put("msg", "关联成功");
        }


        return mapjson;
    }


    /**
     * 获得空闲车辆
     *
     * @return
     */
    public Map<String, Object> getCart(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List<Cart> cartList = relationshipMapper.emptyCar(map);

        mapjson.put("code", 200);
        mapjson.put("data", cartList);


        return mapjson;
    }

    /**
     * 获得空闲教练
     *
     * @return
     */
    public Map<String, Object> getTeacher(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List<Teacher> teacherList = relationshipMapper.emptyTeacher(map);

        mapjson.put("code", 200);
        mapjson.put("data", teacherList);

        return mapjson;
    }

    /**
     * 获得关联历史
     *
     * @return
     */
    public Map<String, Object> getHistory(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List<Relationship> relationshipList = relationshipMapper.getHistory(map);
        if (relationshipList.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", relationshipList);
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "无数据");
        }

        return mapjson;
    }

    /**
     * 获得获得当前关系
     *
     * @return
     */
    public Map<String, Object> getContant(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List<Relationship> relationshipList = relationshipMapper.contant(map);

        mapjson.put("code", 200);
        mapjson.put("data", relationshipList);

        return mapjson;
    }


}
