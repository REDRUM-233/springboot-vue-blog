package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // ====================== 新增用户（明文密码） ======================
    @Override
    public boolean save(User user) {
        return super.save(user);
    }

    // ====================== 更新用户（空密码不修改） ======================
    @Override
    public boolean updateById(User user) {
        if (!StringUtils.hasText(user.getPassword())) {
            // 前端没传密码，保持原有密码不变
            user.setPassword(null); // MyBatis-Plus 默认忽略 null 字段
        }
        return super.updateById(user);
    }
}
