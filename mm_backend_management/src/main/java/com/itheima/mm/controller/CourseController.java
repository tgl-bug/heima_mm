package com.itheima.mm.controller;

import com.itheima.framework.anno.Controller;
import com.itheima.framework.anno.RequestMapping;
import com.itheima.mm.constants.Constants;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.CourseService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author By--tgl
 * @time 2021/1/30$ 22:22$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
@Controller
public class CourseController {
    private CourseService courseService = new CourseService();
    @RequestMapping("/course/add")
    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //获取请求参数封装到Course中
            Course course = JsonUtils.parseJSON2Object(request, Course.class);
            //补全course缺少的数据
            course.setCreateDate(DateUtils.parseDate2String(new Date()));
            //设置userId
            User user=(User) request.getSession().getAttribute(Constants.LOGIN_USER);
            course.setId(user.getId());
            //设置olderNo
            course.setOrderNo(1);
            //调用service的方法保存course的信息
            courseService.add(course);
            JsonUtils.printResult(response,new Result(true,"添加成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"添加学科失败"));
        }
    }
    @RequestMapping("/course/findByPage")
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //获取请求参数封装到querypagebean对象
            QueryPageBean queryPageBean = JsonUtils.parseJSON2Object(request, QueryPageBean.class);
            //调用业务层的方法进行分页查询
            PageResult pageResult = courseService.findByPage(queryPageBean);
            //将相应结果进行封装病转换成json输出到客户端
            JsonUtils.printResult(response,new Result(true,"查询学科列表成功",pageResult));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"查询学科失败"));
        }
    }
    @RequestMapping("/course/update")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //请求参数封装到course中
            Course course = JsonUtils.parseJSON2Object(request, Course.class);
            //调用业务层的方法修改学科信息
            courseService.update(course);
            JsonUtils.printResult(response,new Result(true,"修改学科成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"修改学科失败"));
        }
    }
    @RequestMapping("/course/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //获取请求参数id
            Integer id = Integer.valueOf(request.getParameter("id"));
            //调用业务层的方法根据id删除学科
            courseService.deleteById(id);
            JsonUtils.printResult(response,new Result(true,"删除学科成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,e.getMessage()));

        }

    }
    @RequestMapping("/course/findAll")
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //获取请求参数status
            String status = request.getParameter("status");

            //调用业务层的方法查询学科信息
            List<Course> courseList = courseService.findAll(status);
            //封装相应结果
            JsonUtils.printResult(response,new Result(true,"获取所有学科成功",courseList));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"获取所有学科失败"));
        }

    }
}
