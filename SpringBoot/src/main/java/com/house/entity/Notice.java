package com.house.entity;


/**
 * 公告实体类
 *
 * 存储系统公告信息，由管理员发布，向用户展示重要通知
 * 包含公告的标题、内容和发布时间等基本信息
 */
public class Notice {
    /**
     * 公告ID，主键
     */
    private Integer id;
    
    /**
     * 公告标题
     * 简短描述公告的主题
     */
    private String title;
    
    /**
     * 公告内容
     * 公告的详细文本信息
     */
    private String content;
    
    /**
     * 发布时间
     * 公告的创建或发布时间，格式为字符串
     */
    private String time;

    /**
     * 获取公告ID
     * @return 公告ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置公告ID
     * @param id 公告ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取公告标题
     * @return 公告标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置公告标题
     * @param title 公告标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取公告内容
     * @return 公告内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置公告内容
     * @param content 公告内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取发布时间
     * @return 发布时间字符串
     */
    public String getTime() {
        return time;
    }

    /**
     * 设置发布时间
     * @param time 发布时间字符串
     */
    public void setTime(String time) {
        this.time = time;
    }
}
