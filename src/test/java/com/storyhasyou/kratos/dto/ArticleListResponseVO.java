package com.storyhasyou.kratos.dto;

import lombok.Data;

/**
 * The type Article page response vo.
 *
 * @author fangxi created by 2021/4/16
 */
@Data
public class ArticleListResponseVO {

    /**
     * id
     */
    private Long id;

    /**
     * 发布时间
     */
    private Long publishTime;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 文章摘要
     */
    private String digest;

   
}
