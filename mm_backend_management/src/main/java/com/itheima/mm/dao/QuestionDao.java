package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;

import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/31$ 16:18$
 * @Version: 1.0
 * @Description 描述:
 */
public interface QuestionDao {
    Long findCountByCourseId(Integer id);


    Long findTotalBasicCount(QueryPageBean queryPageBean);

    List<Question> findBasicPageList(QueryPageBean queryPageBean);
}
