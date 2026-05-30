package com.blog.controller;

import com.blog.common.Result;
import com.blog.entity.User;
import com.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin // 加上这一行：允许跨域
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        return Result.success(user);
    }

    @PostMapping
    public Result<?> save(@Valid @RequestBody User user) {
        boolean save = userService.save(user);
        return save ? Result.success(null) : Result.fail("新增失败");
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean b = userService.updateById(user);
        return b ? Result.success(null) : Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        boolean b = userService.removeById(id);
        return b ? Result.success(null) : Result.fail("删除失败");
    }
}
