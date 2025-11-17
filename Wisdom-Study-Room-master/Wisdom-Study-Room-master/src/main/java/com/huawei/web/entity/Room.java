package com.huawei.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Ruijie Zhao
 */
@TableName("room")
@Data
public class Room {
  @TableId(type = IdType.AUTO)
  private Integer roomId;

  private String roomName;
}
