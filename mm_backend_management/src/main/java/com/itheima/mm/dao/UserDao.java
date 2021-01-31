package com.itheima.mm.dao;

import com.itheima.mm.pojo.User;

/**
 * @author By--tgl
 * @time 2021/1/30$ 20:40$
 * @Version: 1.0
 * @Description 描述:
 */
public interface UserDao {
    User findByUsername(String username);
}
