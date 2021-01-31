package com.itheima.mm.service;

import com.itheima.mm.dao.UserDao;
import com.itheima.mm.pojo.User;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * @author By--tgl
 * @time 2021/1/30$ 20:36$
 * @Version: 1.0
 * @Description Java类作用描述:
 */
public class UserService {

    public User login(User user) throws IOException {
        //校验用户名，根据用户名到数据库查询用户
        SqlSession sqlSession = SqlSessionFactoryUtils.openSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User loginUser = userDao.findByUsername(user.getUsername());

        SqlSessionFactoryUtils.commitAndClose(sqlSession);

        //判断loginUser是否为null
        if (loginUser!=null) {
            if (user.getPassword().equals(loginUser.getPassword())) {
                return loginUser;
            }else {
//            用户名错误
                throw new RuntimeException("密码错误");
            }

        }else {
            throw new RuntimeException("用户名错误");
        }

    }

}
