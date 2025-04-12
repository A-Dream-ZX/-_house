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

/**
 * 管理员服务类
 * 
 * 提供管理员相关的业务逻辑处理，包括管理员的CRUD操作、登录验证、密码修改等功能
 * 主要为控制层提供服务，处理管理员账户的各种业务操作
 */
@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper; // 注入管理员数据访问层接口


    /**
     * 添加管理员
     * 
     * 创建新的管理员账户，会进行用户名重复检查
     * 若密码为空，将设置默认密码为"admin"
     *
     * @param admin 待添加的管理员信息对象
     * @throws CustomerException 当用户名已存在时抛出异常
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
     * 修改管理员信息
     * 
     * 更新已有管理员的信息，根据管理员ID进行更新
     *
     * @param admin 包含更新内容的管理员对象，必须包含ID
     */
    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    /**
     * 删除管理员
     * 
     * 根据ID删除指定管理员账户
     *
     * @param id 要删除的管理员ID
     */
    public void deleteByid(Integer id) {
        adminMapper.deleteById(id);
    }

    /**
     * 批量删除管理员
     * 
     * 批量删除多个管理员账户，逐个调用单个删除方法
     *
     * @param adminList 要删除的管理员对象列表
     */
    public void deleteBatch(List<Admin> adminList) {
        for (Admin admin : adminList) {  //iter 遍历
            this.deleteByid(admin.getId());
        }
    }

    /**
     * 根据ID查询管理员
     * 
     * 获取指定ID的管理员详细信息
     *
     * @param id 管理员ID
     * @return 管理员对象，不存在则返回null
     */
    public Admin selectById(String id) {
        return adminMapper.selectById(id);
    }

    /**
     * 查询所有管理员
     * 
     * 获取系统中的所有管理员列表，不分页
     *
     * @return 所有管理员的列表
     */
    public List<Admin> selectAll() {
        return adminMapper.selectAll(null);
    }

    /**
     * 分页查询管理员
     * 
     * 根据条件分页查询管理员信息，支持按用户名、姓名等条件筛选
     *
     * @param pageNum 当前页码，从1开始
     * @param pageSize 每页显示的记录数
     * @param admin 查询条件，可包含用户名、姓名等属性
     * @return 分页结果，包含当前页数据和分页信息
     */
    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize, Admin admin) {
        // 测试是否拿到当前用户信息
        //Account currentUser = TokenUtils.getCurrentUser();

        // 开启分页功能，设置页码和每页大小
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询
        List<Admin> list = adminMapper.selectAll(admin);
        // 将查询结果封装为PageInfo对象返回
        return PageInfo.of(list);
    }

    /**
     * 管理员登录
     * 
     * 验证管理员账号和密码，成功则生成JWT令牌
     *
     * @param admin 包含登录信息的账号对象，必须包含用户名和密码
     * @return 登录成功的管理员信息，包含生成的JWT令牌
     * @throws CustomerException 当账号不存在或密码错误时抛出异常
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
     * 修改管理员密码
     * 
     * 验证旧密码正确性，检查两次新密码是否一致，然后更新密码
     *
     * @param account 包含密码修改信息的账号对象，包含旧密码、新密码和确认密码
     * @throws CustomerException 当两次密码不一致或旧密码错误时抛出异常
     */
    public void updatePassword(Account account) {
        //判断两次新密码是否一致
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
