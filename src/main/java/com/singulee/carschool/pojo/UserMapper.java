package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    User findOneByUsername(String username);

    /**
     * 修改用户密码
     * @param
     * @return
     */
    Integer updatePassword(User user);

    /**插入对象
     *
     */
    int insert(User user);

    List<Map<String,Object>> selectUserByCondiction(Map map);

    int insertSelective(User user);


    /**
     * 通过id 查找
     *
     * @param
     * @return
     */
    Teacher selectByUserId(Integer userId);

    /**
     * 修改教练信息
     * @param record
     * @return
     */
    int update(User record);


    int updateByPrimaryKey(Teacher record);
}