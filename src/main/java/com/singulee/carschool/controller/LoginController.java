package com.singulee.carschool.controller;

import com.singulee.carschool.pojo.Student;
import com.singulee.carschool.pojo.Teacher;
import com.singulee.carschool.pojo.User;
import com.singulee.carschool.service.StudentService;
import com.singulee.carschool.service.TeacherService;
import com.singulee.carschool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 
 * Date: 2023/02/19
 * Description:
 * Version: V1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    final private StudentService studentService;
    final private TeacherService teacherService;
    final private UserService userService;

    @Autowired
    public LoginController(StudentService studentService, TeacherService teacherService, UserService userService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    /**
     * 登录  返回格式
     *
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> map, HttpServletRequest request) {

        boolean flag = false;
        String phone = map.get("phone");
        String password = map.get("password");
        User user = userService.findByUsername(phone);
        if (null != user && user.getPassword().equals(password)) {
            Integer userType = user.getUserType();
            if (userType == 1) {
                Student student = studentService.getByPhone(phone);
                if (student != null) {
                    flag = true;
                    student.setStupwd(user.getPassword());
                    request.getSession().setAttribute("user", student);
                }
            } else {
                Teacher teacher = new Teacher();
                teacher.setTeaphone(phone);
                Integer roleTmp = user.getUserType() == 3 ? 1 : 0;
                teacher.setTearole(roleTmp);
                Teacher teacherRe = teacherService.selectByPone(teacher);
                if (teacherRe != null) {
                    flag = true;
                    teacher.setTeapwd(user.getPassword());
                    request.getSession().setAttribute("user", teacherRe);
                }
            }
        }
        Map<String, Object> mapjson = new HashMap<>();
        if (flag) {
            mapjson.put("code", 200);
            mapjson.put("role", user.getUserType());
        } else {
            mapjson.put("code", 201);
        }
        return mapjson;

    }

    /**
     * 教练 管理员的修改用户信息
     *
     * @param request
     * @return
     */

    @RequestMapping(value = "/teacherInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> teacherInfo(HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        teacher.setTeapwd("");
        mapjson.put("code", 200);
        mapjson.put("data", teacher);
        return mapjson;
    }

    /**
     * 注销登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public String loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "/login.html";
    }

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/getStudentInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getStudentInfo(HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        Student student = (Student) request.getSession().getAttribute("user");
        student.setStupwd("");
        mapjson.put("code", 200);
        mapjson.put("data", student);
        return mapjson;
    }

    /**
     * 获得验证码
     *
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getCode(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String phone = map.get("phone");
        Map<String, Object> mapCode = new HashMap<>();
        Map<String, Object> mapjson = new HashMap<>();
        int mobileCode = (int) ((Math.random() * 9 + 1) * 100000);
        mapCode.put("number", mobileCode + "");
        mapCode.put("phone", phone);
        boolean flag = false;
        User user = userService.findByUsername(phone);
        if (user != null) {
            flag = true;
        }
//        if (flag&& Send.sendCode(mobileCode,phone)){
        if (flag) {
            //发送手机号到手机
            mapjson.put("code", 200);
            mapjson.put("number", mobileCode);
            mapCode.put("time", System.currentTimeMillis());
            System.out.println(mobileCode);
            request.getSession().setAttribute("smscode", mapCode);
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "手机号不存在,请查证");
        }
        return mapjson;
    }


    /**
     * 获得验证码 公用
     *
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getPhoneCode(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String phone = map.get("phone");
        Teacher teacher = new Teacher();
        teacher.setTeaphone(phone);
        Map<String, Object> mapCode = new HashMap<>();
        Map<String, Object> mapjson = new HashMap<>();
        int mobileCode = (int) ((Math.random() * 9 + 1) * 100000);
        mapCode.put("number", mobileCode + "");
        mapCode.put("phone", phone);
        boolean flag = false;
//        if (flag&& Send.sendCode(mobileCode,phone)){
        mapCode.put("time", System.currentTimeMillis() + "");
        mapjson.put("code", 200);
        mapjson.put("number", mobileCode + "");
        request.getSession().setAttribute("smscode", mapCode);
//    }
        return mapjson;
    }


    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updatePhone(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String phone = map.get("phone");
        String number = map.get("number");

        Map<String, Object> mapjson = new HashMap<>();

        Object object = request.getSession().getAttribute("user");
        //判断验证码手机是否合法

        Map<String, Object> mapSession = (Map<String, Object>) request.getSession().getAttribute("smscode");
        System.out.println(mapSession);
        System.out.println(map);
        if (mapSession.get("phone").toString().equals(phone) && mapSession.get("number").toString().equals(number) &&
                System.currentTimeMillis() - Long.parseLong(mapSession.get("time").toString()) < 60 * 1000) {
            if (object instanceof Student) {
                Student student = (Student) object;
                Student studenttmp = studentService.getByPhone(student.getStuphone());
                studenttmp.setStuphone(phone);
                request.getSession().setAttribute("user", studenttmp);
                return studentService.updateStudent(studenttmp);

            } else {
                Teacher teacher = (Teacher) object;
                Teacher teachertmp = teacherService.selectByPone(teacher);
                teachertmp.setTeaphone(phone);
                request.getSession().setAttribute("user", teachertmp);
                return teacherService.updateTeacher(teachertmp);

            }

        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "时间超时重新发送");

        }

        return mapjson;
    }

    /**
     * 获得用户的信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updatePhone(HttpServletRequest request) {


        Map<String, Object> mapjson = new HashMap<>();

        Object object = request.getSession().getAttribute("user");
        //判断验证码手机是否合法
        if (object instanceof Student) {
            Student student = (Student) object;
            mapjson.put("img", student.getStuimg());
            mapjson.put("name", student.getStuname());
        } else {
            Teacher teacher = (Teacher) object;
            mapjson.put("img", teacher.getTeaimg());
            mapjson.put("name", teacher.getTeaname());

        }


        return mapjson;
    }


    /**
     * 验证手机号和验证码
     *
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkCode(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        String phone = map.get("phone");
        String number = map.get("number");
        String password = map.get("password");
        Map<String, Object> mapSession = (Map<String, Object>) request.getSession().getAttribute("smscode");
        if (mapSession.get("phone").equals(phone) && mapSession.get("number").equals(number) &&
                System.currentTimeMillis() - Long.parseLong(mapSession.get("time").toString()) < 60 * 1000) {
            boolean action;
            action = userService.updatePassword(phone, password);
            if (action) {
                mapjson.put("code", 200);
            } else {
                mapjson.put("code", 202);
            }
        } else {
            request.getSession().removeAttribute("smscode");
            mapjson.put("code", 201);
        }
        return mapjson;
    }


    @RequestMapping(value = "/updatePasswordTeacher", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updatePasswordTeacher(@RequestBody Map map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        //判断原密码是否正确
        User user = userService.findByUsername(teacher.getTeaphone());
        System.out.println(user.getPassword());
        if (map.get("oldpassword").toString().equals(user.getPassword())) {
            //修改session的的密码
            teacher.setTeapwd(map.get("newpassword").toString());
            request.getSession().setAttribute("user", teacher);
            //修改数据库中的
            boolean flag = userService.updatePassword(user.getUsername(), teacher.getTeapwd());
            if (flag) {
                mapjson.put("code", 200);
                mapjson.put("msg", "修改成功！");
                return mapjson;
            } else {
                mapjson.put("code", 201);
                mapjson.put("msg", "修改失败！");
                return mapjson;
            }
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "原密码错误");
            return mapjson;
        }


    }

    /**
     * 修改密码 学生
     *
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePasswordStudent", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updatePasswordStudent(@RequestBody Map map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        Student student = (Student) request.getSession().getAttribute("user");
        //判断原密码是否正确
        User user = userService.findByUsername(student.getStuphone());
        System.out.println(user.getPassword());
        if (map.get("oldpassword").toString().equals(user.getPassword())) {
            //修改session的的密码
            student.setStupwd(map.get("newpassword").toString());
            request.getSession().setAttribute("user", student);
            //修改数据库中的
            //修改数据库中的
            boolean flag = userService.updatePassword(user.getUsername(), student.getStupwd());
            if (flag) {
                mapjson.put("code", 200);
                mapjson.put("msg", "修改成功！");
                return mapjson;
            } else {
                mapjson.put("code", 201);
                mapjson.put("msg", "修改失败！");
                return mapjson;
            }
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "原密码错误");
            return mapjson;
        }


    }


    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();

//        String path=request.getSession().getServletContext().getRealPath("/");
        String path = request.getSession().getServletContext().getRealPath("/img");
        System.out.println(path);
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        System.out.println(fileName);
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        // 保存
        try {
            file.transferTo(targetFile);
            map.put("code", 200);
            map.put("data", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 201);

        }
        return map;
    }
}
