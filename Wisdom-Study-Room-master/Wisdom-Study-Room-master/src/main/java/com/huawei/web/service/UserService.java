package com.huawei.web.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huawei.web.entity.User;
import com.huawei.web.mapper.UserMapper;
import com.huawei.web.util.constant.Constant;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Ruijie Zhao
 */
@Service
public class UserService {
  @Resource UserMapper userMapper;

  /**
   * 根据用户名查询
   *
   * @param user 用户
   * @return 用户
   */
  public User selectUserName(User user) {
    return userMapper.selectOne(
        Wrappers.<User>lambdaQuery().eq(User::getUserName, user.getUserName()));
  }

  /**
   * 根据用户名查询(排除自己)
   *
   * @param user 用户
   * @return 用户
   */
  public User selectUserNameExclude(User user) {
    return userMapper.selectOne(
        Wrappers.<User>lambdaQuery()
            .eq(User::getUserName, user.getUserName())
            .ne(User::getUserId, user.getUserId()));
  }

  /**
   * 插入用户
   *
   * @param user 用户
   */
  public void insertUser(User user) {
    userMapper.insert(user);
  }

  /**
   * 用户登录
   *
   * @param user 用户
   * @return 用户
   */
  public User login(User user) {
    return userMapper.selectOne(
        Wrappers.<User>lambdaQuery()
            .eq(User::getUserAccount, user.getUserAccount())
            .eq(User::getUserPassword, user.getUserPassword()));
  }

  /**
   * 更新用户信息
   *
   * @param user 用户信息
   */
  public void updateUser(User user) {
    userMapper.update(user, Wrappers.<User>lambdaQuery().eq(User::getUserId, user.getUserId()));
  }

  /**
   * 删除用户
   *
   * @param user 用户
   */
  public void deleteUser(User user) {
    userMapper.delete(Wrappers.<User>lambdaQuery().eq(User::getUserId, user.getUserId()));
  }

  /**
   * 查询用户信息
   *
   * @param userAccount 用户名
   * @return 用户列表
   */
  public List<User> selectUserList(String userAccount) {
    return userMapper.selectList(
        Wrappers.<User>lambdaQuery().eq(User::getUserAccount, userAccount));
  }

  /**
   * 查找所有用户信息
   *
   * @return 所有用户信息
   */
  public List<User> selectAllList() {
    return userMapper.selectList(Wrappers.lambdaQuery());
  }

  /**
   * 查找Account
   *
   * @param userAccount 用户账户
   */
  public User selectUserAccount(String userAccount) {
    return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserAccount, userAccount));
  }

  /**
   * 违规状态添加
   *
   * @param userAccount 用户账号
   */
  public void addIllegal(String userAccount) {
    User user = selectUserAccount(userAccount);
    user.setUserIllegal(user.getUserIllegal() + 1);
    if (user.getUserIllegal() >= Constant.ILLEGAL_LIMIT) {
      user.setUserIllegalState(Constant.BAN);
      Timestamp time =
          Timestamp.valueOf(LocalDateTime.now().plusDays(Constant.BAN_DAY).plusMinutes(480));
      user.setUserIllegalDate(time);
    }
    updateUser(user);
  }

  /**
   * 找封禁时间已到的用户
   *
   * @param time 时间
   * @return User列表
   */
  public List<User> selectUnblock(Timestamp time) {
    return userMapper.selectList(Wrappers.<User>lambdaQuery().le(User::getUserIllegalDate, time));
  }
}
