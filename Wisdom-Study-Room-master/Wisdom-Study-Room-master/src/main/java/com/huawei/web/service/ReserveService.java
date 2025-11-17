package com.huawei.web.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huawei.web.entity.Reserve;
import com.huawei.web.mapper.ReserveMapper;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Ruijie Zhao
 */
@Service
public class ReserveService {
  @Resource ReserveMapper reserveMapper;

  /**
   * 插入预约信息
   *
   * @param reserve 预约信息
   */
  public void insertReserve(Reserve reserve) {
    reserveMapper.insert(reserve);
  }

  /**
   * 更新预约信息
   *
   * @param reserve 预约信息
   */
  public void updateReserve(Reserve reserve) {
    reserveMapper.update(
        reserve, Wrappers.<Reserve>lambdaQuery().eq(Reserve::getReserveId, reserve.getReserveId()));
  }

  /**
   * 删除预约信息
   *
   * @param reserve 预约信息
   */
  public void deleteReserve(Reserve reserve) {
    reserveMapper.delete(
        Wrappers.<Reserve>lambdaQuery().eq(Reserve::getReserveId, reserve.getReserveId()));
  }

  /**
   * @author Binhui Liu
   */
  /**
   * 查询用户预约信息
   *
   * @param userAccount 用户
   * @return 预约列表
   */
  public List<Reserve> selectList(String userAccount) {
    return reserveMapper.selectList(
        Wrappers.<Reserve>lambdaQuery().eq(Reserve::getReserveUserAccount, userAccount));
  }

  /**
   * @author Ruijie Zhao
   */
  /**
   * 查找所有预约信息
   *
   * @return 所有预约信息
   */
  public List<Reserve> selectAllList() {
    return reserveMapper.selectList(Wrappers.lambdaQuery());
  }
}
