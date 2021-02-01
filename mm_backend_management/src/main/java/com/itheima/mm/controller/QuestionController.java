package com.itheima.mm.controller;

import com.itheima.framework.anno.Controller;
import com.itheima.framework.anno.RequestMapping;
import com.itheima.mm.constants.Constants;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.QuestionService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author By--tgl
 * @time 2021/1/31$ 20:15$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
@Controller
public class QuestionController {
    private QuestionService questionService = new QuestionService();
    @RequestMapping("/question/findBasicByPage")
    public void findBasicByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //1. 获取请求参数，封装到QueryPageBean对象中
            QueryPageBean queryPageBean = JsonUtils.parseJSON2Object(request, QueryPageBean.class);
            //2. 调用业务层的方法，进行分页查询
            PageResult pageResult = questionService.findBasicByPage(queryPageBean);

            JsonUtils.printResult(response,new Result(true,"获取基础题库成功",pageResult));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"获取基础题库失败"));
        }
    }
    @RequestMapping("/question/add")
    public void addQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //获取请求参数封装到question中
            Question question = JsonUtils.parseJSON2Object(request, Question.class);
            //手动设置缺失的数据: status,reviewStatus,createDate,userId
            question.setStatus(Constants.QUESTION_PRE_PUBLISH);
            question.setReviewStatus(Constants.QUESTION_PRE_REVIEW);
            question.setCreateDate(DateUtils.parseDate2String(new Date()));
            User user = (User) request.getSession().getAttribute(Constants.LOGIN_USER);
            question.setUserId(user.getId());
            //调用业务层的方法  添加题目信息
            questionService.add(question);
            JsonUtils.printResult(response,new Result(true,"添加基础试题成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"添加基础试题失败"));
        }
    }
}
