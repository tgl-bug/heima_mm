package com.itheima.mm.dao;

import com.itheima.mm.pojo.Catalog;

import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/31$ 16:08$
 * @Version: 1.0
 * @Description 描述:
 */
public interface CatalogDao {
    Long findCountByCourseId(Integer id);
    List<Catalog> findCatalogListByCourseId(Integer courseId);
}
