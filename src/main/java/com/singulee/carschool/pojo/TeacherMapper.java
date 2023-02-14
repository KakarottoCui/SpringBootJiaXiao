package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface TeacherMapper {

    /**
     * 通过手机号查找用户信息
     * @param teacher
     * @return
     */
    Teacher selectByPhone(Teacher teacher);

    /**
     * 修改用户密码
     * @param teacher
     * @return
     */
    Integer updatePassword(Teacher teacher);

    /**插入对象
     *
     * @param record
     * @return
     */
    int insert(Teacher record);

    List<Map<String,Object>> selectTeacherByCondiction(Map map);

    int insertSelective(Teacher record);


    /**
     * 通过id 查找
     *
     * @param teaid
     * @return
     */
    Teacher selectByTeaId(Integer teaid);

    /**
     * 修改教练信息
     * @param record
     * @return
     */
    int update(Teacher record);


    int updateByPrimaryKey(Teacher record);
}