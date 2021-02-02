package com.itheima.mm.service;

import com.itheima.mm.dao.CityDao;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.utils.LocationUtil;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.service
 *
 * @author Leevi
 * 日期2020-08-05  10:19
 */
public class CityService {
    /**
     * 获取城市信息
     * @param parameterMap
     * @return
     */
    public Map findCityList(Map parameterMap) throws Exception {
        //1. 获取客户端传入的经纬度
        String location = (String) parameterMap.get("location");
        //2. 调用高德地图的API根据经纬度，获取当前城市
        String cityName = LocationUtil.parseLocation(location);
        //3. 调用dao层的方法，根据城市名，查询到当前城市的信息(id，title)
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        CityDao cityDao = sqlSession.getMapper(CityDao.class);
        //当前城市信息，包含id和title
        Dict currentCity = cityDao.findCityByName(cityName);

        //4. 根据fs查询城市列表
        String fs = null;
        if (parameterMap.get("fs") instanceof Integer) {
            fs = (Integer) parameterMap.get("fs") + "";
        }else {
            fs = (String) parameterMap.get("fs");
        };

        List<Dict> dictList = cityDao.findCityListByFs(fs);
        Map resultMap = new HashMap();
        resultMap.put("location",currentCity);
        resultMap.put("citys",dictList);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return resultMap;
    }
}
