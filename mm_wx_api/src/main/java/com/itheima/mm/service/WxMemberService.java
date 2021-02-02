package com.itheima.mm.service;

import com.itheima.mm.dao.WxMemberDao;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.itheima.mm.service
 *
 * @author Leevi
 * 日期2020-08-05  15:10
 */
public class WxMemberService {
    public WxMember findByNickname(String nickName) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
        WxMember loginMember = wxMemberDao.findByNickname(nickName);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return loginMember;
    }

    public void addWxMember(WxMember wxMember) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
        wxMemberDao.addWxMember(wxMember);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
    }

    public WxMember findById(Integer id) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);

        WxMember wxMember = wxMemberDao.findById(id);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
        return wxMember;
    }

    public void update(WxMember wxMember) throws IOException {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);

        wxMemberDao.update(wxMember);

        SqlSessionFactoryUtils.commitAndClose(sqlSession);
    }

    public Map findCenterInfo(WxMember wxMember) throws Exception {
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
        //1. 调用dao层的方法，根据cityID查询出cityName(当前用户选择的城市名)
        String cityName = wxMemberDao.findCityName(wxMember.getCityId());
        //2. 调用dao层的方法，查询当前用户累计答题数answerCount
        Integer answerCount = wxMemberDao.findAnswerCount(wxMember.getId());
        //3. 调用dao层的方法，查询出当前用户的lastAnswer的信息
        //缺少categoryTile
        Map lastAnswer = wxMemberDao.findLastAnswer(wxMember.getId());
        String categoryTitle = wxMemberDao.findCategoryTitle(lastAnswer);
        lastAnswer.put("categoryTitle",categoryTitle);

        Map category = new HashMap();
        category.put("cityID",wxMember.getCityId());
        category.put("cityName",cityName);
        category.put("subjectID",wxMember.getCourseId());

        Map resultMap = new HashMap();
        resultMap.put("answerCount",answerCount);
        resultMap.put("lastAnswer",lastAnswer);
        resultMap.put("category",category);
        return resultMap;
    }
}
