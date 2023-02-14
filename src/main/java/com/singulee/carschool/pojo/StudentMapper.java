package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

    /**
     * 通过电话查询
     *
     * @param phone
     * @return
     */
    Student selectByPhone(String phone);

    /**
     * 修改密码
     *
     * @param student
     * @return
     */
    Integer updatePassword(Student student);

    /**
     * 插入学员
     *
     * @param student
     * @return
     */
    Integer insert(Student student);

    /**
     * 多条件查询
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectStudent(Map map);

    /**
     * 通过id查看
     *
     * @param studentId
     * @return
     */
    Student selectById(Integer studentId);

    /**
     * 修改
     *
     * @param student
     * @return
     */
    int update(Student student);

    /**
     * 教练查看当前学员
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectforTeacher(Map map);

    /**
     * 求条件下的数量
     * @param map
     * @return
     */
    Integer selectforTeacherCount(Map map);
}