package com.itheima.mm.controller;

import com.itheima.framework.anno.Controller;
import com.itheima.framework.anno.RequestMapping;
import com.itheima.mm.constants.Constants;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author By--tgl
 * @time 2021/1/30$ 20:30$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
@Controller
public class UserController {

    private UserService userService = new UserService();

    @RequestMapping("/user/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //接受请求，封装到user对象中
            User user = JsonUtils.parseJSON2Object(request, User.class);
            //调用业务层的方法  进行登录校验
            User loginUser = userService.login(user);
            //将loginUser存储到session中
            request.getSession().setAttribute(Constants.LOGIN_USER, loginUser);
            //将result转换成json字符串输出到浏览器
            JsonUtils.printResult(response, new Result(true, "登录成功", loginUser));
        } catch (Exception e) {
            JsonUtils.printResult(response, new Result(false, "登录失败", null));
            e.printStackTrace();
        }
    }
    @RequestMapping("/user/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session
        request.getSession().invalidate();
        //向客户端相应数据
        JsonUtils.printResult(response,new Result(true,"退出成功",null));

    }
}
