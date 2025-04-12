package com.house.service;

import com.house.entity.Favorite;
import com.house.mapper.FavoriteMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏服务类
 * 
 * 提供用户收藏相关的业务逻辑处理，包括添加收藏、取消收藏、查询收藏列表等功能
 * 主要为控制层提供服务，处理用户收藏房源的各种业务操作
 */
@Service
public class FavoriteService {

    @Resource
    FavoriteMapper favoriteMapper; // 注入收藏数据访问层接口


    /**
     * 添加收藏
     * 
     * 创建新的收藏记录，将用户与房源关联起来
     *
     * @param favorite 待添加的收藏信息对象，必须包含用户ID和房源ID
     */
    public void add(Favorite favorite) {
        favoriteMapper.insert(favorite);
    }

    /**
     * 修改收藏信息
     * 
     * 更新已有收藏的信息，根据收藏ID进行更新
     * 注：在实际应用中，收藏记录通常只有添加和删除操作，很少需要修改
     *
     * @param favorite 包含更新内容的收藏对象，必须包含ID
     */
    public void update(Favorite favorite) {
        favoriteMapper.updateById(favorite);
    }

    /**
     * 删除收藏
     * 
     * 根据ID删除指定收藏记录，即用户取消收藏某个房源
     *
     * @param id 要删除的收藏ID
     */
    public void deleteByid(Integer id) {
        favoriteMapper.deleteById(id);
    }

    /**
     * 批量删除收藏
     * 
     * 批量删除多个收藏记录，逐个调用单个删除方法
     *
     * @param favoriteList 要删除的收藏对象列表
     */
    public void deleteBatch(List<Favorite> favoriteList) {
        for (Favorite favorite : favoriteList) {  //iter 遍历
            this.deleteByid(favorite.getId());
        }
    }


    /**
     * 查询所有收藏
     * 
     * 获取系统中的所有收藏列表，不分页
     * 主要用于管理员查看所有用户的收藏情况
     *
     * @return 所有收藏的列表
     */
    public List<Favorite> selectAll() {
        return favoriteMapper.selectAll(null);
    }

    /**
     * 分页查询收藏
     * 
     * 根据条件分页查询收藏信息，支持按用户ID和房源ID筛选
     *
     * @param pageNum 当前页码，从1开始
     * @param pageSize 每页显示的记录数
     * @param favorite 查询条件，可包含用户ID、房源ID等属性
     * @return 分页结果，包含当前页数据和分页信息
     */
    public PageInfo<Favorite> selectPage(Integer pageNum, Integer pageSize, Favorite favorite) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能，设置页码和每页大小
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Favorite> list = favoriteMapper.selectAll(favorite);
        // 将查询结果封装为PageInfo对象返回
        return PageInfo.of(list);
    }

    /**
     * 查询用户的收藏列表
     * 
     * 获取指定用户ID的所有收藏记录，包含收藏的房源详情
     * 用于前端展示用户的收藏页面
     *
     * @param userId 用户ID
     * @return 该用户的所有收藏列表，包含房源详情
     */
    public List<Favorite> listByUserId(Integer userId) {
        return favoriteMapper.selectByUserId(userId);
    }

    /**
     * 获取系统收藏总数
     * 
     * 统计系统中所有收藏记录的数量
     * 用于管理员查看系统运营数据
     *
     * @return 收藏记录的总数量
     */
    public long getTotalFavorites() {
        return favoriteMapper.getTotalCount();
    }
}
