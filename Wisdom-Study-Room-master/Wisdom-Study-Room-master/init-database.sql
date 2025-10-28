-- ===================================
-- 智慧自习室数据库初始化脚本
-- ===================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `wisdom_study_room` 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `wisdom_study_room`;

-- 删除已存在的表（按依赖顺序）
DROP TABLE IF EXISTS `log`;
DROP TABLE IF EXISTS `reserve`;
DROP TABLE IF EXISTS `seat_time`;
DROP TABLE IF EXISTS `seat`;
DROP TABLE IF EXISTS `room`;
DROP TABLE IF EXISTS `user`;

-- ----------------------------
-- 用户表
-- ----------------------------
CREATE TABLE `user` (
  `user_id` INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `user_account` VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
  `user_password` VARCHAR(255) NOT NULL COMMENT '密码',
  `user_name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `user_privilege` TINYINT(1) DEFAULT 0 COMMENT '权限等级 (0-普通用户 1-管理员)',
  `user_illegal` INT(11) DEFAULT 0 COMMENT '违规次数',
  `user_illegal_state` TINYINT(1) DEFAULT 0 COMMENT '违规状态 (0-正常 1-封禁)',
  `user_illegal_date` TIMESTAMP NULL COMMENT '最近违规时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 房间表
-- ----------------------------
CREATE TABLE `room` (
  `room_id` INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '房间ID',
  `room_name` VARCHAR(50) NOT NULL COMMENT '房间名称',
  INDEX `idx_room_name` (`room_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自习室信息表';

-- ----------------------------
-- 座位表
-- ----------------------------
CREATE TABLE `seat` (
  `seat_id` INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '座位ID',
  `room_id` INT(11) NOT NULL COMMENT '所属房间ID',
  `seat_number` VARCHAR(20) NOT NULL COMMENT '座位编号 (如A-101)',
  FOREIGN KEY (`room_id`) REFERENCES `room`(`room_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位信息表';

-- ----------------------------
-- 座位时段表
-- ----------------------------
CREATE TABLE `seat_time` (
  `seat_time_id` INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '时段ID',
  `seat_id` INT(11) NOT NULL COMMENT '座位ID',
  `begin_time` TIMESTAMP NOT NULL COMMENT '开始时间',
  `end_time` TIMESTAMP NOT NULL COMMENT '结束时间',
  `reserved` TINYINT(1) DEFAULT 0 COMMENT '是否被预约 (0-空闲 1-已预约)',
  FOREIGN KEY (`seat_id`) REFERENCES `seat`(`seat_id`) ON DELETE CASCADE,
  INDEX `idx_time_range` (`begin_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位时段表';

-- ----------------------------
-- 预约记录表
-- ----------------------------
CREATE TABLE `reserve` (
  `reserve_id` INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '预约ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `room_id` INT(11) NOT NULL COMMENT '房间ID',
  `seat_id` INT(11) NOT NULL COMMENT '座位ID',
  `reserve_time_begin` TIMESTAMP NOT NULL COMMENT '预约开始时间',
  `reserve_time_end` TIMESTAMP NOT NULL COMMENT '预约结束时间',
  `time_sign_in` TIMESTAMP NULL COMMENT '实际签到时间',
  `time_sign_out` TIMESTAMP NULL COMMENT '实际签退时间',
  `reserve_state` TINYINT(1) DEFAULT 0 COMMENT '状态 (0-未开始 1-进行中 2-已完成 3-已取消)',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
  FOREIGN KEY (`room_id`) REFERENCES `room`(`room_id`) ON DELETE CASCADE,
  FOREIGN KEY (`seat_id`) REFERENCES `seat`(`seat_id`) ON DELETE CASCADE,
  INDEX `idx_reserve_time` (`reserve_time_begin`, `reserve_time_end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约记录表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
CREATE TABLE `log` (
  `log_id` INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
  `user_id` INT(11) NOT NULL COMMENT '用户ID',
  `log_type` VARCHAR(20) NOT NULL COMMENT '操作类型 (登录/预约/取消)',
  `log_detail` TEXT COMMENT '操作详情',
  `log_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
  INDEX `idx_log_time` (`log_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

-- ----------------------------
-- 插入测试数据
-- ----------------------------

-- 插入管理员账号 (密码: admin123)
INSERT INTO `user` (`user_account`, `user_password`, `user_name`, `user_privilege`) 
VALUES ('admin', 'admin123', '系统管理员', 1);

-- 插入测试用户 (密码: user123)
INSERT INTO `user` (`user_account`, `user_password`, `user_name`, `user_privilege`) 
VALUES ('testuser', 'user123', '测试用户', 0);

-- 插入自习室
INSERT INTO `room` (`room_name`) VALUES 
  ('A栋自习室'), 
  ('B栋自习室'), 
  ('C栋自习室');

-- 插入座位 (A栋 10个座位)
INSERT INTO `seat` (`room_id`, `seat_number`) VALUES 
  (1, 'A-101'), (1, 'A-102'), (1, 'A-103'), (1, 'A-104'), (1, 'A-105'),
  (1, 'A-106'), (1, 'A-107'), (1, 'A-108'), (1, 'A-109'), (1, 'A-110');

-- 插入座位 (B栋 10个座位)
INSERT INTO `seat` (`room_id`, `seat_number`) VALUES 
  (2, 'B-201'), (2, 'B-202'), (2, 'B-203'), (2, 'B-204'), (2, 'B-205'),
  (2, 'B-206'), (2, 'B-207'), (2, 'B-208'), (2, 'B-209'), (2, 'B-210');

-- 插入座位 (C栋 10个座位)
INSERT INTO `seat` (`room_id`, `seat_number`) VALUES 
  (3, 'C-301'), (3, 'C-302'), (3, 'C-303'), (3, 'C-304'), (3, 'C-305'),
  (3, 'C-306'), (3, 'C-307'), (3, 'C-308'), (3, 'C-309'), (3, 'C-310');

SELECT '✅ 数据库初始化完成！' AS 'Status';
SELECT '📊 数据统计:' AS 'Info';
SELECT COUNT(*) AS '用户数' FROM `user`;
SELECT COUNT(*) AS '自习室数' FROM `room`;
SELECT COUNT(*) AS '座位数' FROM `seat`;
