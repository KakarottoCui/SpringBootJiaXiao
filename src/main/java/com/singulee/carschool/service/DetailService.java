package com.singulee.carschool.service;

import com.singulee.carschool.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class DetailService {
    final private DetailMapper detailMapper;
    final private RelationshipMapper relationshipMapper;
    final private OrdersMapper ordersMapper;

    @Autowired
    public DetailService(DetailMapper detailMapper, RelationshipMapper relationshipMapper, OrdersMapper ordersMapper) {
        this.detailMapper = detailMapper;
        this.relationshipMapper = relationshipMapper;
        this.ordersMapper = ordersMapper;
    }




    /**
     * 查询当前教练发布的课程
     *
     * @param map
     * @param request
     * @return
     */
    public Map<String, Object> getDeatail(Map<String, Object> map, HttpServletRequest request) {
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        if (map == null) {
            map = new HashMap();
        }
        map.put("teacherId", teacher.getTeaid());

        Map<String, Object> mapjson = new HashMap<>();
        List<Map<String, Object>> data;
        data = detailMapper.selectDetailList(map);
        if (data.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", data);
        } else {
            mapjson.put("code", 201);
            mapjson.put("data", "无数据");
        }
        return mapjson;
    }

    /**
     * 课程的添加
     *
     * @param map
     * @param request
     * @return
     */
    public Map<String, Object> addDetail(Map map, HttpServletRequest request) {
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        Map<String, Object> mapjson = new HashMap<>();
        //判断当前是否有车
        Map<String, Object> maprela = new HashMap<>();
        maprela.put("teacherId", teacher.getTeaid());
        Relationship relationship = relationshipMapper.contant(maprela).get(0);

        if (relationship == null) {
            mapjson.put("code", 201);
            mapjson.put("msg", "当前无车辆,请联系管理员分配车辆");
            return mapjson;
        }

        if (map == null) {
            map = new HashMap();
        }
        List<Detail> detailList = new ArrayList<>();
        Date courseDate = null;
//        课程时间转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            courseDate = new Date(sdf.parse(map.get("courseDate").toString()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List courseList = (List) map.get("courseList");
        Detail detail;
        for (int i = 0; i < courseList.size(); i++) {
            detail = new Detail();
            detail.setCourseid(Integer.parseInt(courseList.get(i).toString()));
            detail.setCourseDate(courseDate);
            detail.setRelationshipid(relationship.getRelationshipid());
            detail.setStusta("0");
            detail.setExtend1(map.get("courseType").toString());
            detail.setExtend2("0");
            detailList.add(detail);
        }
//        对象组装完成

        System.out.println(detailMapper.inserList(detailList));
        mapjson.put("code", 200);

        return mapjson;
    }


    /**
     * 批量修改
     *
     * @param map
     * @return
     */
    public Map<String, Object> removeDetail(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List detailId = (List) map.get("detailId");
        System.out.println(map);
        Integer []courseid=new Integer[detailId.size()];
        for (int i = 0; i < detailId.size(); i++) {
            Detail detail=detailMapper.selectById(Integer.parseInt(detailId.get(i).toString()));
            if ("1".equals(detail.getStusta())) {
                mapjson.put("code", 201);
                mapjson.put("msg", "已有课程预约,不能修改,请刷新");
                return mapjson;
            }
            courseid[i]=Integer.parseInt(detailId.get(i).toString());
        }
        Map<String,Object> courseId=new HashMap<>();
        courseId.put("staute","2");
        courseId.put("detailList",courseid);
        if(detailMapper.updateList(courseId)>0){
            mapjson.put("code", 200);

        }
        return mapjson;

    }

    /**
     * 课程发布(保存)
     * @param map
     * @return
     */
    public Map<String, Object> saveDetail(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List detailId = (List) map.get("detailId");
        System.out.println(map);
        Integer []courseid=new Integer[detailId.size()];
        for (int i = 0; i < detailId.size(); i++) {
            courseid[i]=Integer.parseInt(detailId.get(i).toString());
        }
        Map<String,Object> courseId=new HashMap<>();
        courseId.put("staute","1");
        courseId.put("detailList",courseid);
        if(detailMapper.updateList(courseId)>0){
            mapjson.put("code", 200);
            mapjson.put("msg","课程发布成功");
        }
        return mapjson;

    }


    /**
     * 学员获得教练发布的课程列表
     * courseDate courseClass teacherName carNumber stuid
     * @param map
     * @return
     */
    public Map<String, Object> getSaveDetail(Map map,HttpServletRequest request){
        Map<String, Object> mapjson = new HashMap<>();
        Student student= (Student) request.getSession().getAttribute("user");

        if(map==null){
            map=new HashMap();
        }
        map.put("teacherId",student.getTeaid());

        List<Map<String,Object>> list=detailMapper.getcourseList(map);
        if(list.size()>0){
            mapjson.put("code",200);
            mapjson.put("data",list);
        }
        else {
            mapjson.put("code",201);
            mapjson.put("msg","没有空余的课程");
        }
        return mapjson;
    }

    /**
     * 课程预定
     * @param
     * @param request
     * @return
     */
    public Map<String, Object> orderCourse(Detail detail,HttpServletRequest request){
        Map<String, Object> mapjson = new HashMap<>();
//        先检查课程是否被预定
        Detail detailTmp=detailMapper.selectById(detail.getDetailid());
        Student student= (Student) request.getSession().getAttribute("user");

        Map map=new HashMap();
        map.put("studentId",student.getStuid());
        map.put("courseDate",detailTmp.getCourseDate().toString());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+map);
        if(ordersMapper.getMyOrderDate(map).size()>0){
            mapjson.put("code",201);
            mapjson.put("msg","当前日期已有预约请选择其他时间");
            return mapjson;
        }


        if(detailTmp.getStusta().equals("1")){
            mapjson.put("code",201);
            mapjson.put("msg","此课程已被预约");
            return mapjson;
        }
//        检查预定课程时间有没有预定的课程

        detailTmp.setStusta("1");
        if(detailMapper.updateById(detailTmp)>0){
            //添加数据

           Orders orders=new Orders();
           orders.setStuid(student.getStuid());
           orders.setDetailid(detailTmp.getDetailid());
           orders.setStates(0);
           orders.setExtend1(new Timestamp(System.currentTimeMillis()));
            ordersMapper.insertByNull(orders);
            mapjson.put("code",200);
            mapjson.put("msg","添加成功");
        }

        return mapjson;
    }
}
