package com.itheima.mm.controller;

import com.itheima.framework.anno.Controller;
import com.itheima.framework.anno.RequestMapping;
import com.itheima.mm.entry.Result;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.service.QuestionService;
import com.itheima.mm.service.WxMemberService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.mm.controller
 *
 * @author Leevi
 * 日期2020-08-06  09:14
 */
@Controller
public class QuestionController {
    private WxMemberService wxMemberService = new WxMemberService();
    private QuestionService questionService = new QuestionService();
    @RequestMapping("/question/categorys")
    public void categorys(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取用户的id
            String authorization = request.getHeader("Authorization");
            Integer id = Integer.valueOf(authorization.substring(7));
            //2. 调用wxMemberService的方法，根据id获取用户信息
            WxMember wxMember = wxMemberService.findById(id);

            //3. 获取请求参数,categoryKind  1表示按照技术点查询 2表示按照企业查询 以及 categoryType 101表示刷题
            Map parameterMap = JsonUtils.parseJSON2Object(request, Map.class);
            parameterMap.put("memberId",wxMember.getId());
            parameterMap.put("cityId",wxMember.getCityId());
            parameterMap.put("courseId",wxMember.getCourseId());
            List<Map> categoryList = questionService.findCategorys(parameterMap);
            JsonUtils.printResult(response,new Result(true,"获取题库分类列表成功",categoryList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"获取题库分类列表失败"));
        }
    }
    @RequestMapping("/question/questions")
    public void questions(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //1. 获取用户的id
            String authorization = request.getHeader("Authorization");
            Integer id = Integer.valueOf(authorization.substring(7));

            //2. 获取客户端的请求参数: categoryID 如果是按照二级目录查询就是二级目录的id，如果是按照企业查询它就是企业的id
            // categoryKind和categoryType
            Map parameterMap = JsonUtils.parseJSON2Object(request, Map.class);
            parameterMap.put("memberId",id);

            Map resultMap = questionService.findQuestionList(parameterMap);

            JsonUtils.printResult(response,new Result(true,"获取题目列表成功",resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"获取题目列表失败"));
        }
    }

    @RequestMapping("/question/commit")
    public void CommitAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //1. 获取请求参数
            Map parameterMap = JsonUtils.parseJSON2Object(request, Map.class);
            //2. 获取用户的id
            String authorization = request.getHeader("Authorization");
            Integer memberId = Integer.valueOf(authorization.substring(7));
            //3. 将memberId存储到parameterMap中
            parameterMap.put("memberId",memberId);
            //4. 调用业务层的方法，保存用户做题的记录
            questionService.commitAnswer(parameterMap);
            JsonUtils.printResult(response,new Result(true,"保存做题记录成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"保存做题记录失败"));
        }
    }
}
