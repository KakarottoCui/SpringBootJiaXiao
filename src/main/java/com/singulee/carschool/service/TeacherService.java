package com.singulee.carschool.service;

import com.singulee.carschool.pojo.StudentMapper;
import com.singulee.carschool.pojo.Teacher;
import com.singulee.carschool.pojo.TeacherMapper;
import com.singulee.carschool.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
public class TeacherService {
    final private TeacherMapper teacherMapper;
    final private StudentMapper studentMapper;
    final private UserService userService;

    @Autowired
    public TeacherService(TeacherMapper teacherMapper, StudentMapper studentMapper, UserService userService) {
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.userService = userService;
    }


    /**
     * 通过电话返回员工信息
     *
     * @param teacher
     * @return
     */
    public Teacher selectByPone(Teacher teacher) {

        return teacherMapper.selectByPhone(teacher);
    }

    /**
     * 修改密码
     *
     * @param phone
     * @param password
     * @param role
     * @return
     */
    public boolean updatePassword(String phone, String password, Integer role) {
        Teacher teacher = new Teacher();
        teacher.setTeaphone(phone);
        teacher.setTeapwd(password);
        teacher.setTearole(role);
        return teacherMapper.updatePassword(teacher) > 0;

    }

    /**
     * 教练添加
     *
     * @param teacher
     * @return
     */
    public Map<String, Object> addTeacher(Teacher teacher) {
        Map<String, Object> map = new HashMap<>();
        if (selectByPone(teacher) != null) {
            map.put("code", 201);
            map.put("msg", "手机号已存在");
            return map;

        }
        if (teacherMapper.insert(teacher) > 0) {
            User user = new User();
            user.setPassword(teacher.getTeapwd());
            user.setUserType(2);
            user.setUsername(teacher.getTeaphone());
            user.setStatus(1);
            userService.insert(user);
            map.put("code", 200);
            map.put("msg", "添加成功");
        } else {
            map.put("code", 201);
            map.put("msg", "添加失败");
        }
        return map;
    }

    /**
     * 返回教练列表
     * teaname:teacherName,driverclassId:driverClassId,staute:staute
     *
     * @param map
     * @return
     */
    public Map<String, Object> getTeacherList(Map<String, Object> map) {
        Map<String, Object> mapjson = new HashMap<>();
        List<Map<String, Object>> teacherList = teacherMapper.selectTeacherByCondiction(map);

        mapjson.put("code", 200);
        mapjson.put("data", teacherList);
        return mapjson;
    }

    /**
     * 通过id查找
     *
     * @param teacher
     * @return
     */
    public Map<String, Object> getTeacher(Teacher teacher) {
        Map<String, Object> mapjson = new HashMap<>();
        Teacher teacherjson = teacherMapper.selectByTeaId(teacher.getTeaid());
        mapjson.put("code", 200);
        mapjson.put("data", teacherjson);
        return mapjson;
    }


    /**
     * 修改教练信息
     *
     * @param teacher
     * @return
     */
    public Map<String, Object> updateTeacher(Teacher teacher) {
        Map<String, Object> mapjson = new HashMap<>();
        Teacher originTea = teacherMapper.selectByTeaId(teacher.getTeaid());
        String oldPhone = originTea.getTeaphone();
        User user = userService.findByUsername(oldPhone);
        user.setUsername(teacher.getTeaphone());
        user.setPassword(teacher.getTeapwd());
        boolean flag = userService.update(user);
        if (teacherMapper.update(teacher) > 0 && flag) {
            mapjson.put("code", 200);
            mapjson.put("msg", "修改成功");
        }
        return mapjson;
    }

    /**
     * 离职
     *
     * @param
     * @return
     */
    public Map<String, Object> getaway(Map map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();

        //获取总记录数
        map.put("studyLevel", 1);
        if (studentMapper.selectforTeacherCount(map) > 0) {
            mapjson.put("code", 201);
            mapjson.put("msg", "当前有关联的学员");

        } else {
            Teacher teacher = teacherMapper.selectByTeaId(Integer.parseInt(map.get("teacherId").toString()));
            teacher.setTeaenddate(new Date(System.currentTimeMillis()));
            //修改关系 修改车辆状态
            if (teacherMapper.updateByPrimaryKey(teacher) > 0) {

                mapjson.put("code", 200);
            }
        }
        return mapjson;
    }


}
