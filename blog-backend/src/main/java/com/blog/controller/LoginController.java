package com.blog.controller;

import com.blog.common.Result;
import com.blog.dto.LoginDTO;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDTO dto) {

        // 1. 根据用户名查用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userService.getOne(wrapper);

        if (user == null) {
            return Result.fail("用户名不存在");
        }

        // 2. 明文密码对比
        if (!dto.getPassword().equals(user.getPassword())) {
            return Result.fail("密码错误");
        }

        // 3. 生成 JWT 令牌
        String token = jwtUtil.createToken(user.getId());

        return Result.success(token);
    }
}
