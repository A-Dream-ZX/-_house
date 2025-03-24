package com.house.service;

import com.house.common.Result;
import com.house.entity.House;
import com.house.mapper.HouseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    @Resource
    HouseMapper houseMapper;


    /**
     * 添加信息
     *
     * @param house
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
     * 修改信息
     *
     * @param house
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
     * 删除信息
     *
     * @param id
     */
    public void deleteByid(Integer id) {
        houseMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @return
     */
    public void deleteBatch(List<House> houseList) {
        for (House house : houseList) {  //iter 遍历
            this.deleteByid(house.getId());
        }
    }



    public List<House> selectAll(House house) {
        return houseMapper.selectAll(house);
    }

    /**
     * 根据ID查询房源详情
     */
    public House selectById(Integer id) {
        return houseMapper.selectById(id);
    }

    /**
     * 根据价格区间查询房源
     */


    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     */
    public PageInfo<House> selectPage(Integer pageNum, Integer pageSize, House house) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<House> list = houseMapper.selectAll(house);
        return PageInfo.of(list);
    }



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
     * @param id
     * @param status
     */
    public void updateStatus(Integer id, String status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseMapper.updateById(house);
    }

    public long getTotalHouses() {
        return houseMapper.getTotalCount();
    }
}
