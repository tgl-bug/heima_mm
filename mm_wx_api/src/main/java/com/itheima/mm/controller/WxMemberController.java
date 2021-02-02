package com.itheima.mm.controller;

import com.itheima.framework.anno.Controller;
import com.itheima.framework.anno.RequestMapping;
import com.itheima.mm.entry.Result;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.service.WxMemberService;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.itheima.mm.controller
 *
 * @author Leevi
 * 日期2020-08-05  14:43
 */
@Controller
public class WxMemberController {
    private WxMemberService wxMemberService = new WxMemberService();
    @RequestMapping("/wxMember/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取客户端的请求参数，封装到wxMember中
            WxMember wxMember = JsonUtils.parseJSON2Object(request, WxMember.class);

            //2. 判断你这个用户是否已经在我们面面服务器上注册过，根据昵称查询用户
            WxMember loginMember = wxMemberService.findByNickname(wxMember.getNickName());
            if (loginMember == null) {
                //说明之前没有在面面服务器注册过，那么我们就要将该用户的信息存储进面面数据库
                //我们先要设置好wxMember的createDate
                wxMember.setCreateTime(DateUtils.parseDate2String(new Date()));

                //调用业务层的方法，保存wxMember的信息
                wxMemberService.addWxMember(wxMember);
            }else {
                //如果已经注册过了，那么loginMember肯定有用户的id
                wxMember = loginMember;
            }

            //3. 响应给客户端数据
            Map resultMap = new HashMap<>();
            resultMap.put("token",wxMember.getId());
            resultMap.put("userInfo",wxMember);

            JsonUtils.printResult(response,new Result(true,"登录成功",resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"登录失败"));
        }
    }

    /**
     * 设置当前会员的城市和学科
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/wxMember/setCityCourse")
    public void setCityCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取请求参数
            Map parameterMap = JsonUtils.parseJSON2Object(request, Map.class);
            //2. 获取请求头Authorization的值
            String authorization = request.getHeader("Authorization");
            //3. 从Authorization请求头中获取用户id
            Integer id = Integer.valueOf(authorization.substring(7));
            //4. 调用业务层的方法，根据id获取用户信息
            WxMember wxMember = wxMemberService.findById(id);

            //5. 最终的目的是绑定城市和学科---->将城市id和学科id设置到wxMember中，然后存储到数据库
            Integer cityId = null;
            if (parameterMap.get("cityID") instanceof Integer) {
                cityId = (Integer) parameterMap.get("cityID");
            }else {
                cityId = Integer.valueOf((String) parameterMap.get("cityID"));
            }

            Integer courseId = null;
            if (parameterMap.get("subjectID") instanceof Integer) {
                courseId = (Integer) parameterMap.get("subjectID");
            }else {
                courseId = Integer.valueOf((String) parameterMap.get("subjectID"));
            }
            wxMember.setCityId(cityId);
            wxMember.setCourseId(courseId);
            //此时cityId和courseId就设置给了wxMember，调用业务层的方法，更新数据库中的wxMember的数据
            wxMemberService.update(wxMember);
            //设置成功
            JsonUtils.printResult(response,new Result(true,"设置城市和学科成功"));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"设置城市和学科失败"));
        }
    }
    @RequestMapping("/wxMember/center")
    public void center(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            //1. 获取当前用户的id,从Authorization请求头中获取用户id
            String authorization = request.getHeader("Authorization");
            Integer memberId = Integer.valueOf(authorization.substring(7));

            //2. 调用业务层的方法，根据用户的id查询到用户信息
            WxMember wxMember = wxMemberService.findById(memberId);
            //3. 调用业务层的方法，查询到响应数据
            Map resultMap = wxMemberService.findCenterInfo(wxMember);
            JsonUtils.printResult(response,new Result(true,"获取个人中心信息成功",resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            JsonUtils.printResult(response,new Result(false,"获取个人中心信息失败"));
        }
    }
}
