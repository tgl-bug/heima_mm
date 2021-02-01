package com.itheima.mm.service;

import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.QuestionItemDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.QuestionItem;
import com.itheima.mm.pojo.Tag;
import com.itheima.mm.utils.JsonUtils;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void add(Question question){
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtils.openSqlSession();
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            //将t_question表相关的数据
            questionDao.add(question);
            //判断是否是选择题   如果是选择题将题目的选项信息   存储到t_question_item表
            QuestionItemDao questionItemDao = sqlSession.getMapper(QuestionItemDao.class);
            List<QuestionItem> questionItemList = question.getQuestionItemList();
            if (questionItemList != null && questionItemList.size() >0) {
                for (QuestionItem questionItem : questionItemList) {
                    //手动设置改选项所属的question的id
                    questionItem.setQuestionId(question.getId());
                    //调用dao层的方法存储每一个题目的选项信息
                    questionItemDao.add(questionItem);
                }
            }
            //将题目和标签的关联信息  存储到t_question_tag表中
            //获取题目的所有标签
            List<Tag> tagList = question.getTagList();
            //获取改题目的所有标签
            if (tagList != null && tagList.size()>0) {
                for (Tag tag : tagList) {
                    Map paramMap = new HashMap();
                    paramMap.put("questionId",question.getId());
                    paramMap.put("tagId",tag.getId());
                    //进行关联
                    questionDao.addQuestionTag(paramMap);
                }
            }
            SqlSessionFactoryUtils.commitAndClose(sqlSession);
        } catch (IOException e) {
            e.printStackTrace();
            SqlSessionFactoryUtils.rollbackAndClose(sqlSession);
        }


    }
}
