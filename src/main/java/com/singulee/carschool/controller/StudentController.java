package com.singulee.carschool.controller;

import com.singulee.carschool.pojo.Student;
import com.singulee.carschool.service.StudentService;
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
@RequestMapping("/student")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 添加学员
     * @param student
     * @return
     */
    @RequestMapping(value = "/addStudent")
    @ResponseBody
    public Map<String,Object> addStudent(@RequestBody Student student){
        return studentService.insert(student);
    }

    /**
     * 信息修改
     * @param student
     * @return
     */
    @RequestMapping(value = "/updateStudent")
    @ResponseBody
    public Map<String,Object> updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }

    /**
     * var json={name:name,driverclassId:driverclass,studentStute:studyStute,finshStau:finsh}
     * @param map
     * @return
     */
    @RequestMapping(value = "/getStuList")
    @ResponseBody
    public Map<String,Object> getStuList(@RequestBody(required = false) Map map){
        return studentService.selectByCondiction(map);
    }

    /**
     * 通过id查看
     * @param student
     * @return
     */
    @RequestMapping(value = "/getStudent")
    @ResponseBody
    public Map<String,Object> getStu(@RequestBody Student student){
    return studentService.selectStudent(student);
    }

    /**
     * 查看我的学生 分页
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getMystudent")
    @ResponseBody
    public Map<String,Object> getMyStu(@RequestBody Map map, HttpServletRequest  request){
    return studentService.getMyStudent(map,request);
    }
    /**
     *修改学生状态 {studentId:$("#studentId").val(),courseType:$("#courseType").val()}
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateStaute")
    @ResponseBody
    public Map<String,Object> updateStaute(@RequestBody Map map){
    return studentService.updateStudentStaute(map);
    }

    @RequestMapping(value = "/end")
    @ResponseBody
    public Map<String,Object> studentEnd(@RequestBody Map map){
    return studentService.endStudent(map);
    }





}
