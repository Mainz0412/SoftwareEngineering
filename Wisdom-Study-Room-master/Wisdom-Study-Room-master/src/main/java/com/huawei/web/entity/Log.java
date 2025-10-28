package com.huawei.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Ruijie Zhao
 */
@TableName("log")
@Data
public class Log {
    @TableId(type = IdType.AUTO)
    private Integer logId;
    private String userAccount;
    private String logState;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp logTime;
}
