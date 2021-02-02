package com.itheima.mm.service;

import com.alibaba.fastjson.JSONArray;
import com.itheima.mm.constant.Constants;
import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.WxMemberDao;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.utils.IntegerUtils;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.service
 *
 * @author Leevi
 * 日期2020-08-06  09:19
 */
public class QuestionService {
    public List<Map> findCategorys(Map parameterMap) throws Exception {
        Integer categoryType = IntegerUtils.parseInteger(parameterMap.get("categoryType"));
        Integer categoryKind = IntegerUtils.parseInteger(parameterMap.get("categoryKind"));
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
        List<Map> categoryList = null;
        if (categoryType == Constants.VIEW_QUESTION) {
            //刷题
            //2. 判断你是按照技术点刷？按照企业刷?
            if (categoryKind == Constants.TAG) {
                //按照技术点
                //调用dao层的方法进行查询题目分类列表
                categoryList = questionDao.findCategoryListByTag(parameterMap);
            }else if(categoryKind == Constants.COMPANY){
                //按照企业
                categoryList = questionDao.findCategoryListByCompany(parameterMap);
            }else {
                //按照行业方向
            }
        }
        return categoryList;
    }

    public Map findQuestionList(Map parameterMap) throws Exception {
        //1. 获取客户端传入的categoryKind、categoryType
        Integer categoryType = IntegerUtils.parseInteger(parameterMap.get("categoryType"));
        Integer categoryKind = IntegerUtils.parseInteger(parameterMap.get("categoryKind"));
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
        Map resultMap = new HashMap();
        if (categoryType == Constants.VIEW_QUESTION) {
            //刷题
            //1.查询题目总数
            Long allCount = questionDao.findAllCount(parameterMap);
            //按照技术点查询
            resultMap.put("allCount",allCount);
            //2. 查询当前二级目录的所有题
            List<Question> questionList = questionDao.findQuestionListByCategoryId(parameterMap);
            resultMap.put("items",questionList);

            //3. 查询最后一道题的id
            Integer lastID = questionDao.findLastQuestionId(parameterMap);
            resultMap.put("lastID",lastID);
        }
        return resultMap;
    }

    public void commitAnswer(Map parameterMap) {
        SqlSession sqlSession = null;
        try {
            //1. 往tr_member_question表保存用户的做题记录: memberId、questionId、isFavorite、answerResult、tag(只有tag不在parameterMap中)
            //对于tag的分析:如果是选择题，true就是0、false就是1；如果是简单题，true就是2、false就是3
            //怎么判断是选择题，还是简答题? 看answerResult是否为空
            JSONArray jsonArray = (JSONArray) parameterMap.get("answerResult");
            Boolean answerIsRight = (Boolean) parameterMap.get("answerIsRight");
            // 第一步: 判断当前这道题是简答题还是选择题
            if (jsonArray == null) {
                //则判断answerIsRight为true或者false，设置tag的值为2或者3
                if (answerIsRight) {
                    parameterMap.put("tag", 2);
                } else {
                    parameterMap.put("tag", 3);
                }
            } else {
                //选择题，则判断answerIsRight为true或者false，设置tag的值为0或者1
                if (answerIsRight) {
                    parameterMap.put("tag", 0);
                } else {
                    parameterMap.put("tag", 1);
                }
                //parameterMap中的answerResult是一个字符串数组
                //将answerResult转换成字符串
                Object[] objects = jsonArray.toArray();
                String answerResult = Arrays.toString(objects);
                //替换map中原本的answerResult
                parameterMap.put("answerResult", answerResult);
            }
            //转换isFavorite
            Boolean isFavorite = (Boolean) parameterMap.get("isFavorite");
            if (isFavorite) {
                parameterMap.put("isFavorite", 1);
            } else {
                parameterMap.put("isFavorite", 0);
            }

            //第二步: 判断用户是否已经做过这道题了:根据memberId和questionId到tr_member_question表查询数据
            sqlSession = SqlSessionFactoryUtils.openSqlSession();
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);

            Map resultMap = questionDao.findByMemberIdAndQuestionId(parameterMap);

            if (resultMap != null) {
                //修改数据
                questionDao.updateMemberQuestion(parameterMap);
            } else {
                //如果没有做过这道题，则新增数据
                questionDao.addMemberQuestion(parameterMap);
            }

            //第三步: 保存用户的lastxxx的信息
            WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
            //根据id获取微信用户信息
            WxMember wxMember = wxMemberDao.findById((Integer) parameterMap.get("memberId"));
            //设置lastQuestionId
            wxMember.setLastQuestionId((Integer) parameterMap.get("id"));

            Integer categoryId = IntegerUtils.parseInteger(parameterMap.get("categoryID"));
            Integer categoryType = IntegerUtils.parseInteger(parameterMap.get("categoryType"));
            Integer categoryKind = IntegerUtils.parseInteger(parameterMap.get("categoryKind"));
            wxMember.setLastCategoryId(categoryId);
            wxMember.setLastCategoryKind(categoryKind);
            wxMember.setLastCategoryType(categoryType);
            wxMemberDao.update(wxMember);

            SqlSessionFactoryUtils.commitAndClose(sqlSession);
        } catch (Exception e) {
            e.printStackTrace();
            SqlSessionFactoryUtils.rollbackAndClose(sqlSession);
            throw new RuntimeException(e.getMessage());
        }
    }
}
