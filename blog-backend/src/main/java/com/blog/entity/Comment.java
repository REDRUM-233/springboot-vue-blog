package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;

    private Long articleId;
    private Long userId;
    private String username;  // 评论人用户名

    private Long parentId;  // 父评论ID（回复用）

    private LocalDateTime createTime;
}
