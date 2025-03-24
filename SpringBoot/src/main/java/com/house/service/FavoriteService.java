package com.house.service;

import com.house.entity.Favorite;
import com.house.mapper.FavoriteMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Resource
    FavoriteMapper favoriteMapper;


    /**
     * 添加信息
     *
     * @param favorite
     */
    public void add(Favorite favorite) {

        favoriteMapper.insert(favorite);
    }

    /**
     * 修改信息
     *
     * @param favorite
     */
    public void update(Favorite favorite) {
        favoriteMapper.updateById(favorite);
    }

    /**
     * 删除信息
     *
     * @param id
     */
    public void deleteByid(Integer id) {
        favoriteMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @return
     */
    public void deleteBatch(List<Favorite> favoriteList) {
        for (Favorite favorite : favoriteList) {  //iter 遍历
            this.deleteByid(favorite.getId());
        }
    }



    public List<Favorite> selectAll() {
        return favoriteMapper.selectAll(null);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     */
    public PageInfo<Favorite> selectPage(Integer pageNum, Integer pageSize, Favorite favorite) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<Favorite> list = favoriteMapper.selectAll(favorite);
        return PageInfo.of(list);
    }


    public List<Favorite> listByUserId(Integer userId) {
        return favoriteMapper.selectByUserId(userId);
    }

    public long getTotalFavorites() {
        return favoriteMapper.getTotalCount();
    }
}
