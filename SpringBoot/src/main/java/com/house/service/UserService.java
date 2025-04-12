package com.house.service;

import java.time.LocalDateTime;  // 添加这行导入
import cn.hutool.core.util.StrUtil;
import com.house.entity.Account;
import com.house.entity.User;
import com.house.exception.CustomerException;
import com.house.mapper.UserMapper;
import com.house.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务类
 * 
 * 提供用户相关的业务逻辑实现，包括用户的CRUD操作、认证和授权
 * 处理普通用户的注册、登录、信息管理和密码修改等功能
 */
@Service
public class UserService {

    @Resource
    UserMapper userMapper; // 用户数据访问层接口


    /**
     * 添加用户信息
     * 创建新的用户记录，包含用户名查重和默认值设置
     *
     * @param user 包含用户详细信息的实体对象
     * @throws CustomerException 当用户名已存在时抛出异常
     */
    public void add(User user) {
        // 判断用户名是否已存在
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomerException("账号已存在");
        }
        // 密码为空时，设置默认密码为 123456
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("123456");
        }
        // 姓名为空时，设置默认姓名为 用户名
        if (StrUtil.isBlank(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole("user"); // 设置为普通用户角色
        user.setCreatedAt(LocalDateTime.now());  // 设置创建时间
        userMapper.insert(user);
    }

    /**
     * 修改用户信息
     * 更新现有用户的基本信息
     *
     * @param user 包含更新后用户信息的实体对象
     * @throws CustomerException 当用户ID为空时抛出异常
     */
    public void updata(User user) {
        if (user.getId() == null) {
            throw new CustomerException("用户ID不能为空");
        }
        userMapper.updateById(user);
    }

    /**
     * 删除用户信息
     * 根据ID删除指定用户
     *
     * @param id 要删除的用户ID
     */
    public void deleteByid(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 批量删除用户
     * 循环删除多个用户记录
     *
     * @param userList 包含多个用户实体的列表
     */
    public void deleteBatch(List<User> userList) {
        for (User user : userList) {  //iter 遍历
            this.deleteByid(user.getId());
        }
    }

    /**
     * 查询所有用户
     * 获取系统中所有用户的列表
     * 
     * @return 所有用户的列表
     */
    public List<User> selectAll() {
        return userMapper.selectAll(null);
    }


    /**
     * 根据ID查询用户
     * 获取指定用户的完整信息，主要用于JWT拦截器验证
     *
     * @param id 用户ID
     * @return 返回指定用户的实体对象，不存在则返回null
     */
    public User selectById(String id) {
        return userMapper.selectById(id);
    }

    /**
     * 分页查询用户
     * 支持分页和条件查询，用于管理界面展示用户列表
     *
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param user 查询条件封装对象
     * @return 返回包含分页信息和用户数据的PageInfo对象
     */
    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /**
     * 用户登录
     * 验证用户名和密码，生成JWT令牌
     *
     * @param user 包含用户名和密码的账户对象
     * @return 登录成功返回带有token的用户信息
     * @throws CustomerException 当账号不存在或密码错误时抛出异常
     */
    public User login(Account user) {
        // 验证账号
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码
        if (!dbUser.getPassword().equals(user.getPassword())) {
            throw new CustomerException("密码错误");
        }
        // 创建Token 并返回
        String token = TokenUtils.createToken(dbUser.getId() + "-" + "user", dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    /**
     * 用户注册
     * 创建新的普通用户账号
     *
     * @param user 包含注册信息的用户对象
     */
    public void register(User user) {
        this.add(user);
    }


    /**
     * 修改用户密码
     * 验证旧密码，更新为新密码
     *
     * @param account 包含旧密码和新密码的账户对象
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
        User user = userMapper.selectById(currentUser.getId().toString());
        user.setPassword(account.getNewPassword());
        userMapper.updateById(user);
    }

    /**
     * 获取用户注册趋势统计
     * 用于管理员仪表盘展示用户增长趋势
     * 
     * @return 包含日期和对应新增用户数量的统计数据
     */
    public Object getUserStats() {
        return userMapper.getUserStats();
    }

    /**
     * 获取系统总用户数
     * 用于统计和展示在仪表盘
     * 
     * @return 系统中的总用户数量
     */
    public long getTotalUsers() {
        return userMapper.getTotalCount();
    }

    /**
     * 获取本月新增用户数
     * 用于统计和展示在仪表盘
     * 
     * @return 本月新注册的用户数量
     */
    public long getNewUsersThisMonth() {
        return userMapper.getNewUsersThisMonth();
    }

    /**
     * 获取本周新增用户数
     * 用于统计和展示在仪表盘
     * 
     * @return 本周新注册的用户数量
     */
    public long getNewUsersThisWeek() {
        return userMapper.getNewUsersThisWeek();
    }
}
