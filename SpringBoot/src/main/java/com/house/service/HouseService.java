package com.house.service;

import com.house.common.Result;
import com.house.entity.House;
import com.house.mapper.HouseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房源服务类
 * 
 * 提供房源相关的业务逻辑实现，包括CRUD操作和特定业务功能
 * 如房源的添加、修改、查询和预订等操作
 */
@Service
public class HouseService {

    @Resource
    HouseMapper houseMapper; // 房源数据访问层接口


    /**
     * 添加房源信息
     * 创建新的房源记录，并处理图片URL的存储
     *
     * @param house 包含房源详细信息的实体对象
     */
    public void add(House house) {
        if (house.getImages() != null && !house.getImages().isEmpty()) {
            // 将图片 URL 列表转换为逗号分隔的字符串
            String images = String.join(",", house.getImages());
            
            // 处理图片字符串长度过长的问题
            if (images.length() > 1000) {
                String[] imageArray = images.split(",");
                if (imageArray.length > 5) {  // 假设最多保存5张图片
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb.append(imageArray[i]);
                    }
                    images = sb.toString();
                }
            }
            
            house.setImages(images);
        }

        houseMapper.insert(house);
    }

    /**
     * 修改房源信息
     * 更新现有房源记录，并处理图片字段可能过长的问题
     *
     * @param house 包含更新后房源信息的实体对象
     */
    public void update(House house) {
        // 处理images字段，如果过长则进行处理
        if (house.getImages() != null && house.getImages().length() > 1000) {
            // 可以选择截断字符串
            // house.setImages(house.getImages().substring(0, 1000));
            
            // 或者将多个图片分开存储，只保留前几个图片
            String[] imageArray = house.getImages().split(",");
            if (imageArray.length > 5) {  // 假设最多保存5张图片
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(imageArray[i]);
                }
                house.setImages(sb.toString());
            }
        }
        
        houseMapper.updateById(house);
    }

    /**
     * 删除房源信息
     * 根据ID删除指定房源记录
     *
     * @param id 要删除的房源ID
     */
    public void deleteByid(Integer id) {
        houseMapper.deleteById(id);
    }

    /**
     * 批量删除房源
     * 循环删除多个房源记录
     *
     * @param houseList 包含多个房源实体的列表
     */
    public void deleteBatch(List<House> houseList) {
        for (House house : houseList) {  //iter 遍历
            this.deleteByid(house.getId());
        }
    }


    /**
     * 查询房源列表
     * 支持条件查询，可根据传入的房源对象属性进行筛选
     * 
     * @param house 查询条件封装对象，属性为null则不作为筛选条件
     * @return 返回符合条件的房源列表
     */
    public List<House> selectAll(House house) {
        return houseMapper.selectAll(house);
    }

    /**
     * 根据ID查询房源详情
     * 获取指定房源的完整信息
     * 
     * @param id 房源ID
     * @return 返回指定房源的实体对象，不存在则返回null
     */
    public House selectById(Integer id) {
        return houseMapper.selectById(id);
    }

    /**
     * 分页查询房源
     * 支持分页和条件查询，用于前端展示房源列表
     *
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param house 查询条件封装对象
     * @return 返回包含分页信息和房源数据的PageInfo对象
     */
    public PageInfo<House> selectPage(Integer pageNum, Integer pageSize, House house) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<House> list = houseMapper.selectAll(house);
        return PageInfo.of(list);
    }


    /**
     * 预订房源
     * 将房源状态更改为"已预订"
     * 
     * @param houseId 要预订的房源ID
     * @return 预订成功返回成功消息，失败返回错误信息
     */
    public Result bookHouse(Integer houseId) {
        House house = houseMapper.selectById(houseId);
        if (house == null) {
            return Result.error("房源不存在");
        }
        
        if ("已预订".equals(house.getStatus())) {
            return Result.error("该房源已被预订");
        }
        
        house.setStatus("已预订");
        houseMapper.updateById(house);  // 使用统一的updateById方法
        
        return Result.success();
    }

    /**
     * 更新房源状态
     * 修改指定房源的状态值
     * 
     * @param id 房源ID
     * @param status 新的状态值，如"available"、"rented"等
     */
    public void updateStatus(Integer id, String status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseMapper.updateById(house);
    }

    /**
     * 获取房源总数
     * 用于统计和展示在仪表盘
     * 
     * @return 系统中的总房源数量
     */
    public long getTotalHouses() {
        return houseMapper.getTotalCount();
    }
}
