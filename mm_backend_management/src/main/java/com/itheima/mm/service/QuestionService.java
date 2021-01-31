package com.itheima.mm.service;

import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.utils.JsonUtils;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/31$ 20:19$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
public class QuestionService {
    public PageResult findBasicByPage(QueryPageBean queryPageBean) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);

        //获取总条数
        Long total = questionDao.findTotalBasicCount(queryPageBean);
        //获取当前页数据集合
        List<Question> basicList = questionDao.findBasicPageList(queryPageBean);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);

        return new PageResult(total,basicList);
    }
}
