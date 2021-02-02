package com.itheima.mm.dao;

import com.itheima.mm.pojo.Question;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-08-06  09:46
 */
public interface QuestionDao {
    List<Map> findCategoryListByTag(Map parameterMap);

    List<Map> findCategoryListByCompany(Map parameterMap);

    Long findAllCount(Map parameterMap);

    List<Question> findQuestionListByCategoryId(Map parameterMap);

    Integer findLastQuestionId(Map parameterMap);

    Map findByMemberIdAndQuestionId(Map parameterMap);

    void updateMemberQuestion(Map parameterMap);

    void addMemberQuestion(Map parameterMap);
}
