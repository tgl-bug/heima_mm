package com.itheima.mm.dao;

import com.itheima.mm.pojo.Dict;

import java.util.List;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-08-05  11:05
 */
public interface CityDao {
    Dict findCityByName(String cityName);

    List<Dict> findCityListByFs(String fs);
}
