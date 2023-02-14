package com.singulee.carschool.controller;

import com.singulee.carschool.service.CourseService;
import com.singulee.carschool.service.DetailService;
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
@RequestMapping("/course")
public class CourseController {
    private CourseService courseService;
    private DetailService detailService;

    @Autowired
    public CourseController(CourseService courseService, DetailService detailService
                            ) {
        this.courseService = courseService;
        this.detailService=detailService;
    }

    /**
     * 获取课程表时间
     * @return
     */
    @RequestMapping(value = "/getCourses")
    @ResponseBody
    public Map<String,Object> getCourse(){
        return courseService.getCourse();
    }

    /**
     * 出传入日期 判断当前日期发布的课程
     * courseDate
     * @param map
     * @return
     */
    @RequestMapping(value = "/courseDetail")
    @ResponseBody
    public Map<String,Object> getDeatil(@RequestBody (required=false) Map<String, Object> map, HttpServletRequest request){
        return detailService.getDeatail(map,request);
    }

    /**
     * 添加课程,传入课程时间id 课程类型
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/addCourseDetail")
    @ResponseBody
    public Map<String,Object> addCOurseDetail(@RequestBody (required=false) Map map, HttpServletRequest request){

        return detailService.addDetail(map,request);
    }

    /**
     * 批量移除课程
     * @param map
     * @return
     */
    @RequestMapping(value = "/removeDetail")
    @ResponseBody
    public Map<String,Object> removeCOurseDetail(@RequestBody Map map){
        return detailService.removeDetail(map);
    }
    /**
     * 保存(发布)课程
     * @param map
     * @return
     */
    @RequestMapping(value = "/saveDetail")
    @ResponseBody
    public Map<String,Object> saveCOurseDetail(@RequestBody Map map){
        return detailService.saveDetail(map);
    }






}
