package com.house.service;

import cn.hutool.core.util.StrUtil;
import com.house.entity.Account;
import com.house.entity.Admin;
import com.house.exception.CustomerException;
import com.house.mapper.AdminMapper;
import com.house.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;


    /**
     * 添加信息
     *
     * @param admin
     */
    public void add(Admin admin) {
        // 判断用户名是否已存在
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            throw new CustomerException("账号已存在");
        }
        // 密码为空时，设置默认密码为 admin
        if (StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("admin");
        }
        admin.setRole("admin");
        adminMapper.insert(admin);
    }

    /**
     * 修改信息
     *
     * @param admin
     */
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    /**
     * 删除信息
     *
     * @param id
     */
    public void deleteByid(Integer id) {
        adminMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @return
     */
    public void deleteBatch(List<Admin> adminList) {
        for (Admin admin : adminList) {  //iter 遍历
            this.deleteByid(admin.getId());
        }
    }

    /**
     * 根据id查询 拦截器
     *
     * @param id
     */
    public Admin selectById(String id) {
        return adminMapper.selectById(id);
    }

    public List<Admin> selectAll() {
        return adminMapper.selectAll(null);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     */
    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    /**
     * 登录
     *
     * @param admin
     */

    public Admin login(Account admin) {
        // 验证账号
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (dbAdmin == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码
        if (!dbAdmin.getPassword().equals(admin.getPassword())) {
            throw new CustomerException("密码错误");
        }
        // 创建Token 并返回
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + "admin", dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }


    /**
     * 修改密码
     *
     * @param account
     */
    public void updatePassword(Account account) {
        //判断
        if (!account.getNewPassword().equals(account.getNew2Password())) {
            throw new CustomerException("500", "两次密码不一致");
        }
        //校验旧密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if (!currentUser.getPassword().equals(account.getPassword())) {
            throw new CustomerException("500", "旧密码错误");
        }
        //更新密码
        Admin admin = adminMapper.selectById(currentUser.getId().toString());
        admin.setPassword(account.getNewPassword());
        adminMapper.updateById(admin);



    }
}
