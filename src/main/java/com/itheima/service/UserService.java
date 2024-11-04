package com.itheima.service;

import com.itheima.pojo.User;

public interface UserService {
    //根据用户名查询用户信息
    public User findByUsername(String username);

    //注册
    public void register(String username, String password);

    void update(User user);

    void updateAvatar(String avatar);

    void updatePwd(String newPwd);
}
