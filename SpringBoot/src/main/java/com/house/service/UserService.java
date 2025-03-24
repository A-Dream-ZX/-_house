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

@Service
public class UserService {

    @Resource
    UserMapper userMapper;


    /**
     * 添加信息
     *
     * @param user
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
        user.setRole("user");
        user.setCreatedAt(LocalDateTime.now());  // 设置创建时间
        userMapper.insert(user);
    }

    /**
     * 修改信息
     *
     * @param user
     */
    public void updata(User user) {
        if (user.getId() == null) {
            throw new CustomerException("用户ID不能为空");
        }
        userMapper.updateById(user);
    }

    /**
     * 删除信息
     *
     * @param id
     */
    public void deleteByid(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @return
     */
    public void deleteBatch(List<User> userList) {
        for (User user : userList) {  //iter 遍历
            this.deleteByid(user.getId());
        }
    }

    public List<User> selectAll() {
        return userMapper.selectAll(null);
    }


    /**
     * 根据id查询 拦截器
     *
     * @param id
     * @return
     */
    public User selectById(String id) {
        return userMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     */
    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /**
     * 登录
     *
     * @param user
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
     * 注册
     *
     * @param user
     */
    public void register(User user) {
        this.add(user);
    }


    /**
     * 修改密码
     * }
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
        User user = userMapper.selectById(currentUser.getId().toString());
        user.setPassword(account.getNewPassword());
        userMapper.updateById(user);
    }

    /**
     * 获取用户注册趋势统计
     * @return 包含日期和对应新增用户数量的统计数据
     */
    public Object getUserStats() {
        return userMapper.getUserStats();
    }

    public long getTotalUsers() {
        return userMapper.getTotalCount();
    }

    public long getNewUsersThisMonth() {
        return userMapper.getNewUsersThisMonth();
    }

    public long getNewUsersThisWeek() {
        return userMapper.getNewUsersThisWeek();
    }
}
