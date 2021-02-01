package com.itheima.mm.dao;

import com.itheima.mm.pojo.Tag;

import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/31$ 16:13$
 * @Version: 1.0
 * @Description 描述:
 */
public interface TagDao {
    Long findCountByCourseId(Integer id);
    List<Tag> findTagListByCourseId(Integer courseId);
}
