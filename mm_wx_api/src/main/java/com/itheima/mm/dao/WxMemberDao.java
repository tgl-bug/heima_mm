package com.itheima.mm.dao;

import com.itheima.mm.pojo.WxMember;

import java.util.Map;

/**
 * 包名:com.itheima.mm.dao
 *
 * @author Leevi
 * 日期2020-08-05  15:20
 */
public interface WxMemberDao {
    WxMember findByNickname(String nickName);

    void addWxMember(WxMember wxMember);

    WxMember findById(Integer id);

    void update(WxMember wxMember);

    String findCityName(Integer cityId);

    Integer findAnswerCount(int id);

    Map findLastAnswer(int id);

    String findCategoryTitle(Map lastAnswer);
}
