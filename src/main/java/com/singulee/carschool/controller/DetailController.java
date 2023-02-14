package com.singulee.carschool.controller;

import com.singulee.carschool.pojo.Detail;
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
@RequestMapping("/detail")
public class DetailController {
   private DetailService detailService;

   @Autowired
    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }



    /**
     * 学员获得教练发布的课程列表
     * courseDate courseClass teacherName carNumber stuid
     * @param map
     * @return
     */
    @RequestMapping("/getsaveCourse")
    @ResponseBody
    public Map<String, Object> getSaveDetail(@RequestBody Map map, HttpServletRequest request){

        return detailService.getSaveDetail(map,request);
    }

    /**
     * 预定课程
     *
     * @param request
     * @return
     */
    @RequestMapping("/orderCourse")
    @ResponseBody
    public Map<String, Object> orderCourse(@RequestBody Detail detail, HttpServletRequest request){

        return detailService.orderCourse(detail,request);
    }
}
