package com.singulee.carschool.pojo;

import java.util.List;
import java.util.Map;

public interface DetailMapper {


    List<Map<String,Object>> selectDetailList(Map map);

    int inserList(List<Detail> list);

    int updateList(Map map);

    /**
     * 批量修改关系列表
     * @param map
     * @return
     */
    int updateRelaList(Map map);

    int insert(Detail record);


    int insertSelective(Detail record);


    Detail selectById(Integer detailid);



    int updateById(Detail record);



    /**
     * 获得已发布的课程进行预约 courseDate courseClass teacherName carNumber stuid
     * @param map
     * @return
     */
    List<Map<String,Object>> getcourseList(Map map);
}