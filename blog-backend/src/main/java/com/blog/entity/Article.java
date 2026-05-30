package com.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String content;
    private String summary;     // 摘要

    private Long categoryId;
    private Long userId;

    private String status;      // draft 草稿 / published 已发布
    private Integer viewCount;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
