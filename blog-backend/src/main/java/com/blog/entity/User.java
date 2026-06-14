package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    private String username;

    // 只允许写入，不允许返回给前端
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

