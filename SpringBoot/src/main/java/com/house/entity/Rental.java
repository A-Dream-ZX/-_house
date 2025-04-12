package com.house.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 租约实体类
 * 
 * 存储房屋租赁合同相关信息，包括租期、价格和状态等
 * 作为用户和房源之间的关联实体，记录租赁交易的完整过程
 */
public class Rental {
    /**
     * 租约ID，主键
     */
    private Integer id;
    
    /**
     * 租户ID，关联用户表
     * 表示签订租约的用户
     */
    private Integer userId;
    
    /**
     * 房源ID，关联房源表
     * 表示被租赁的房屋
     */
    private Integer houseId;
    
    /**
     * 租期开始日期
     * 租约生效的时间点
     */
    private LocalDateTime startDate;
    
    /**
     * 租期结束日期
     * 租约到期的时间点
     */
    private LocalDateTime endDate;
    
    /**
     * 租约总价
     * 整个租期的租金总额
     */
    private BigDecimal totalPrice;
    
    /**
     * 租约状态
     * 可选值：
     * "pending"（待确认）：用户提交租约申请，等待房东确认
     * "confirmed"（已确认）：房东已确认租约，租约生效
     * "rejected"（已拒绝）：房东拒绝租约申请
     * "completed"（已完成）：租期结束，租约履行完毕
     * "cancelled"（已取消）：租约被取消
     */
    private String status;
    
    /**
     * 备注信息
     * 租约的额外说明或特殊要求
     */
    private String remark;
    
    /**
     * 创建时间
     * 租约申请提交的时间点
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     * 租约状态最后一次更新的时间点
     */
    private LocalDateTime updatedAt;
    
    /**
     * 获取租约ID
     * @return 租约ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置租约ID
     * @param id 租约ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取租户ID
     * @return 租户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置租户ID
     * @param userId 租户ID
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
     * 获取租期开始日期
     * @return 开始日期
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * 设置租期开始日期
     * @param startDate 开始日期
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取租期结束日期
     * @return 结束日期
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * 设置租期结束日期
     * @param endDate 结束日期
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取租约总价
     * @return 总价金额
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置租约总价
     * @param totalPrice 总价金额
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取租约状态
     * @return 状态字符串
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置租约状态
     * @param status 状态字符串
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取备注信息
     * @return 备注字符串
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息
     * @param remark 备注字符串
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * 获取更新时间
     * @return 更新时间
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置更新时间
     * @param updatedAt 更新时间
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}