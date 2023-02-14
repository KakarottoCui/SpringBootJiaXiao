package com.singulee.carschool.controller;

import com.singulee.carschool.pojo.Teacher;
import com.singulee.carschool.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
    private TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * 添加教练
     * @param teacher
     * @return
     */
    @RequestMapping(value = "/addTeacher")
    @ResponseBody
    public Map<String,Object> addTeacher(@RequestBody Teacher teacher){
        return  teacherService.addTeacher(teacher);

    }

    @RequestMapping(value = "/updateTeacher")
    @ResponseBody
    public Map<String,Object> updateTeacher(@RequestBody Teacher teacher){
        return  teacherService.updateTeacher(teacher);

    }



    /**
     * 多条件查询
     * teaname:teacherName,driverclassId:driverClassId,staute:staute
     * @param map
     * @return
     */
    @RequestMapping(value = "/getTeacherList")
    @ResponseBody
    public Map<String,Object> getTeacherList(@RequestBody(required = false)Map<String,Object> map){
        return  teacherService.getTeacherList(map);
    }

    /**
     * 通过id查看
     * @param teacher
     * @return
     */
    @RequestMapping(value = "/getTea")
    @ResponseBody
    public Map<String,Object> getTeacher(@RequestBody Teacher teacher){
        return teacherService.getTeacher(teacher);
    }


    /**
     * 添加学员时返回学员列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getTeacher")
    @ResponseBody
    public Map<String,Object> getTeacherIndate(){
        Map<String,Object> map=new HashMap<>();
        map.put("staute","在职");
        return  teacherService.getTeacherList(map);
    }

    @RequestMapping(value = "/getaway")
    @ResponseBody
    public Map<String,Object> getaway(@RequestBody Map map,HttpServletRequest request){

        return  teacherService.getaway(map,request);
    }




}
