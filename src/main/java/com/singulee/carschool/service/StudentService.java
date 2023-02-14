package com.singulee.carschool.service;

import com.singulee.carschool.pojo.Student;
import com.singulee.carschool.pojo.StudentMapper;
import com.singulee.carschool.pojo.Teacher;
import com.singulee.carschool.pojo.User;
import com.singulee.carschool.util.StaticInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
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
public class StudentService {
    final private StudentMapper studentMapper;
    final private UserService userService;

    @Autowired
    public StudentService(StudentMapper studentMapper, UserService userService) {
        this.studentMapper = studentMapper;
        this.userService = userService;
    }

    /**
     * 通过手机号查询学员信息
     *
     * @param phone
     * @return
     */
    public Student getByPhone(String phone) {
        return studentMapper.selectByPhone(phone);
    }

    /**
     * 修改密码
     *
     * @param phone
     * @param password
     * @return
     */
    public boolean updatePassword(String phone, String password) {
        Student student = new Student();
        student.setStuphone(phone);
        student.setStupwd(password);
        return studentMapper.updatePassword(student) > 0;
    }

    /**
     * 添加
     *
     * @param student
     * @return
     */
    public Map<String, Object> insert(Student student) {
        Map<String, Object> map = new HashMap<>();
        if (getByPhone(student.getStuphone()) != null) {
            map.put("code", 201);
            map.put("msg", "手机号已存在");
            return map;
        }
        if (studentMapper.insert(student) > 0) {
            User user = new User();
            user.setStatus(1);
            user.setUsername(student.getStuphone());
            user.setPassword(student.getStupwd());
            user.setUserType(1);
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
     * var json={name:name,driverclassId:driverclass,studentStute:studyStute,finshStau:finsh}
     *
     * @param map
     * @return
     */
    public Map<String, Object> selectByCondiction(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        List<Map<String, Object>> studentList = studentMapper.selectStudent(map);
        mapjson.put("code", 200);
        mapjson.put("data", studentList);
        return mapjson;
    }

    /**
     * 通过id查看
     *
     * @param student
     * @return
     */
    public Map<String, Object> selectStudent(Student student) {
        Map<String, Object> mapjson = new HashMap<>();
        Student studenjson = studentMapper.selectById(student.getStuid());
        mapjson.put("code", 200);
        mapjson.put("data", studenjson);
        return mapjson;
    }

    /**
     * 修改学生信息
     *
     * @param student
     * @return
     */
    @Transactional
    public Map<String, Object> updateStudent(Student student) {
        Integer stuid = student.getStuid();
        Student originStu = studentMapper.selectById(stuid);
        String oldPhone = originStu.getStuphone();
        User user = userService.findByUsername(oldPhone);
        user.setUsername(student.getStuphone());
        user.setPassword(student.getStupwd());
        Map<String, Object> mapjson = new HashMap<>();
        int count = studentMapper.update(student);
        boolean update = userService.update(user);
        if (count > 0 && update) {
            mapjson.put("code", 200);
            mapjson.put("msg", "修改成功");
        } else {
            mapjson.put("code", 201);
            mapjson.put("msg", "没有修改成功");
        }
        return mapjson;
    }

    /**
     * studentname:$("#studentname").val().trim(),
     * courseType:$("#courseTypetitle").val(),studyLevel:$("#studyLevel").val()
     * 获得当前学习的学院
     * 当前页:page
     * 总页数: count
     *
     * @param map
     * @param request
     * @return
     */
    public Map<String, Object> getMyStudent(Map map, HttpServletRequest request) {
        Map<String, Object> mapjson = new HashMap<>();
        int count = 0;
        int total = 0;
        int page = Integer.parseInt(map.get("page").toString());

        Teacher teacher = (Teacher) request.getSession().getAttribute("user");
        map.put("teacherId", teacher.getTeaid());

        //获取总记录数
        count = studentMapper.selectforTeacherCount(map);

        map.put("from", (page - 1) * StaticInfo.PAGE_SIZE);
        map.put("to", (page) * StaticInfo.PAGE_SIZE);
        total = (count - 1) / StaticInfo.PAGE_SIZE + 1;
        //查询页码
        List<Map<String, Object>> students = studentMapper.selectforTeacher(map);
        if (students.size() > 0) {
            mapjson.put("code", 200);
            mapjson.put("data", students);
            mapjson.put("page", page);
            mapjson.put("count", count);
            mapjson.put("total", total);
        }

        return mapjson;
    }

    /**
     * 修改学生学生信息
     *
     * @param map
     * @return
     */
    public Map<String, Object> updateStudentStaute(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        Student student = studentMapper.selectById(Integer.parseInt(map.get("studentId").toString()));
        student.setStustust(map.get("courseType").toString());
        if (studentMapper.update(student) > 0) {
            mapjson.put("code", 200);
        }
        return mapjson;
    }

    /**
     * 学生毕业
     *
     * @param map
     * @return
     */
    public Map<String, Object> endStudent(Map map) {
        Map<String, Object> mapjson = new HashMap<>();
        Student student = studentMapper.selectById(Integer.parseInt(map.get("studentId").toString()));
        if (!student.getStustust().equals("科目三")) {
            mapjson.put("code", 201);
            mapjson.put("msg", "当前学习状态不是科目三,不能毕业");
        } else {
            student.setStuenddate(new Date(System.currentTimeMillis()));
            if (studentMapper.update(student) > 0) {
                mapjson.put("code", 200);
            }
        }

        return mapjson;
    }


}
