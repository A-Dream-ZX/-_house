package com.house.entity;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 * 
 * 存储用户对房源的收藏记录
 * 作为用户和房源之间的多对多关联实体，记录用户的收藏行为
 */
public class Favorite {
    /**
     * 收藏记录ID，主键
     */
    private Integer id;
    
    /**
     * 用户ID，关联用户表
     * 表示进行收藏操作的用户
     */
    private Integer userId;
    
    /**
     * 房源ID，关联房源表
     * 表示被收藏的房源
     */
    private Integer houseId;
    
    /**
     * 收藏时间
     * 记录用户收藏该房源的时间点
     */
    private LocalDateTime createdAt;
    
    /**
     * 房源信息
     * 非数据库字段，用于前端展示收藏列表时包含房源详情
     */
    private House house;  // 添加房源信息

    /**
     * 获取房源信息
     * @return 关联的房源对象
     */
    public House getHouse() {
        return house;
    }

    /**
     * 设置房源信息
     * @param house 关联的房源对象
     */
    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * 获取收藏ID
     * @return 收藏ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置收藏ID
     * @param id 收藏ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取房源ID
     * @return 房源ID
     */
    public Integer getHouseId() {
        return houseId;
    }

    /**
     * 设置房源ID
     * @param houseId 房源ID
     */
    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     * @param createdAt 创建时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
