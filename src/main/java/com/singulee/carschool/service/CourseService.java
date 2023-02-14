package com.singulee.carschool.service;

import com.singulee.carschool.pojo.Course;
import com.singulee.carschool.pojo.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CourseService {
    final private CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public Map<String,Object> getCourse(){
        Map<String,Object> map=new HashMap<>();
        List<Course> courseList=courseMapper.getCourseList();
        map.put("code",200);
        map.put("data",courseList);
        return map;

    }


}
