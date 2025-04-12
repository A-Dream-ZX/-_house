package com.house.entity;

import java.time.LocalDateTime;


/**
 * 房源实体类
 * 
 * 用于存储和表示系统中的房屋信息
 * 包含房屋的基本属性、价格、位置和状态等信息
 */
public class House {
    /**
     * 房源ID，主键
     */
    private Integer id;
    
    /**
     * 房源标题，用于列表展示
     */
    private String title;
    
    /**
     * 房屋租金，每月价格（单位：元）
     */
    private Integer price;
    
    /**
     * 房屋面积，如"80平米"
     */
    private String area;
    
    /**
     * 房型，如"两室一厅"、"三室两厅"等
     */
    private String roomType;
    
    /**
     * 房屋地址，包括城市、区域和详细地址
     */
    private String location;
    
    /**
     * 房屋详细描述，包括设施、周边环境等
     */
    private String description;
    
    /**
     * 房屋状态，可选值：
     * "available"（可租）
     * "rented"（已租）
     * "unavailable"（不可租）
     */
    private String status;
    
    /**
     * 发布管理员ID，关联管理员表
     */
    private Integer adminId;
    
    /**
     * 创建时间，记录房源发布的时间点
     */
    private LocalDateTime createdAt;
    
    /**
     * 房屋主图URL，显示在列表页
     */
    private String imageUrl; // 主图
    
    /**
     * 多张房屋图片URL，以逗号分隔
     * 用于详情页展示房屋的多个角度
     */
    private String images; // 存储多张图片的URL，以逗号分隔
    
    /**
     * 价格范围查询 - 最低价格
     * 仅用于查询，不存储在数据库中
     */
    private Integer minPrice;
    
    /**
     * 价格范围查询 - 最高价格
     * 仅用于查询，不存储在数据库中
     */
    private Integer maxPrice;

    /**
     * 获取最低价格
     * @return 最低价格
     */
    public Integer getMinPrice() {
        return minPrice;
    }

    /**
     * 设置最低价格
     * @param minPrice 最低价格
     */
    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    /**
     * 获取最高价格
     * @return 最高价格
     */
    public Integer getMaxPrice() {
        return maxPrice;
    }

    /**
     * 设置最高价格
     * @param maxPrice 最高价格
     */
    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * 获取房源ID
     * @return 房源ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置房源ID
     * @param id 房源ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取房源标题
     * @return 房源标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置房源标题
     * @param title 房源标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取房屋租金
     * @return 房屋租金
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * 设置房屋租金
     * @param price 房屋租金
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * 获取房屋面积
     * @return 房屋面积
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置房屋面积
     * @param area 房屋面积
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取房型
     * @return 房型
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * 设置房型
     * @param roomType 房型
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    /**
     * 获取房屋地址
     * @return 房屋地址
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置房屋地址
     * @param location 房屋地址
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取房屋详细描述
     * @return 房屋详细描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置房屋详细描述
     * @param description 房屋详细描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取房屋状态
     * @return 房屋状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置房屋状态
     * @param status 房屋状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取发布管理员ID
     * @return 管理员ID
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置发布管理员ID
     * @param adminId 管理员ID
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
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

    /**
     * 获取房屋主图URL
     * @return 主图URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置房屋主图URL
     * @param imageUrl 主图URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取多张房屋图片URL
     * @return 多张图片URL（逗号分隔）
     */
    public String getImages() {
        return images;
    }

    /**
     * 设置多张房屋图片URL
     * @param images 多张图片URL（逗号分隔）
     */
    public void setImages(String images) {
        this.images = images;
    }
}
