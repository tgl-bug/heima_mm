package com.itheima.mm.dao;

import com.itheima.mm.pojo.Course;

import java.util.List;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-08-05  12:11
 */
public interface CourseDao {

    public List<Course> findAll();
}
