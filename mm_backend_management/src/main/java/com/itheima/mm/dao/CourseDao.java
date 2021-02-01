package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/30$ 22:40$
 * @Version: 1.0
 * @Description 描述:
 */
public interface CourseDao {
    void add(Course course);

    Long findTotalCount(QueryPageBean queryPageBean);

    List<Course> findPageList(QueryPageBean queryPageBean);

    void update(Course course);

    void deleteById(Integer id);

    List<Course> findAll(@Param("status") String status);
}
