/*
 Navicat Premium Dump SQL

 Source Server         : dev
 Source Server Type    : MySQL
 Source Server Version : 80025 (8.0.25)
 Source Host           : 127.0.0.1:3306
 Source Schema         : cutejava

 Target Server Type    : MySQL
 Target Server Version : 80025 (8.0.25)
 File Encoding         : 65001

 Date: 20/06/2025 18:30:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `blob_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  KEY `sched_name` (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`,`calendar_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cron_expression` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fired_time` bigint NOT NULL,
  `sched_time` bigint NOT NULL,
  `priority` int NOT NULL,
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`entry_id`) USING BTREE,
  KEY `idx_qrtz_ft_trig_inst_name` (`sched_name`,`instance_name`) USING BTREE,
  KEY `idx_qrtz_ft_inst_job_req_rcvry` (`sched_name`,`instance_name`,`requests_recovery`) USING BTREE,
  KEY `idx_qrtz_ft_j_g` (`sched_name`,`job_name`,`job_group`) USING BTREE,
  KEY `idx_qrtz_ft_jg` (`sched_name`,`job_group`) USING BTREE,
  KEY `idx_qrtz_ft_t_g` (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  KEY `idx_qrtz_ft_tg` (`sched_name`,`trigger_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`) USING BTREE,
  KEY `idx_qrtz_j_req_recovery` (`sched_name`,`requests_recovery`) USING BTREE,
  KEY `idx_qrtz_j_grp` (`sched_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`sched_name`,`lock_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_locks` (`sched_name`, `lock_name`) VALUES ('ClusteredScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` (`sched_name`, `lock_name`) VALUES ('ClusteredScheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_checkin_time` bigint NOT NULL,
  `checkin_interval` bigint NOT NULL,
  PRIMARY KEY (`sched_name`,`instance_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_scheduler_state` (`sched_name`, `instance_name`, `last_checkin_time`, `checkin_interval`) VALUES ('ClusteredScheduler', 'DESKTOP-FM44BLS1742558233429', 1742558255052, 20000);
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `repeat_count` bigint NOT NULL,
  `repeat_interval` bigint NOT NULL,
  `times_triggered` bigint NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `int_prop_1` int DEFAULT NULL,
  `int_prop_2` int DEFAULT NULL,
  `long_prop_1` bigint DEFAULT NULL,
  `long_prop_2` bigint DEFAULT NULL,
  `dec_prop_1` decimal(13,4) DEFAULT NULL,
  `dec_prop_2` decimal(13,4) DEFAULT NULL,
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `next_fire_time` bigint DEFAULT NULL,
  `prev_fire_time` bigint DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_time` bigint NOT NULL,
  `end_time` bigint DEFAULT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `misfire_instr` smallint DEFAULT NULL,
  `job_data` blob,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  KEY `idx_qrtz_t_j` (`sched_name`,`job_name`,`job_group`) USING BTREE,
  KEY `idx_qrtz_t_jg` (`sched_name`,`job_group`) USING BTREE,
  KEY `idx_qrtz_t_c` (`sched_name`,`calendar_name`) USING BTREE,
  KEY `idx_qrtz_t_g` (`sched_name`,`trigger_group`) USING BTREE,
  KEY `idx_qrtz_t_state` (`sched_name`,`trigger_state`) USING BTREE,
  KEY `idx_qrtz_t_n_state` (`sched_name`,`trigger_name`,`trigger_group`,`trigger_state`) USING BTREE,
  KEY `idx_qrtz_t_n_g_state` (`sched_name`,`trigger_group`,`trigger_state`) USING BTREE,
  KEY `idx_qrtz_t_next_fire_time` (`sched_name`,`next_fire_time`) USING BTREE,
  KEY `idx_qrtz_t_nft_st` (`sched_name`,`trigger_state`,`next_fire_time`) USING BTREE,
  KEY `idx_qrtz_t_nft_misfire` (`sched_name`,`misfire_instr`,`next_fire_time`) USING BTREE,
  KEY `idx_qrtz_t_nft_st_misfire` (`sched_name`,`misfire_instr`,`next_fire_time`,`trigger_state`) USING BTREE,
  KEY `idx_qrtz_t_nft_st_misfire_grp` (`sched_name`,`misfire_instr`,`next_fire_time`,`trigger_group`,`trigger_state`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint DEFAULT NULL COMMENT '上级部门',
  `sub_count` int DEFAULT '0' COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `dept_sort` int DEFAULT '999' COMMENT '排序',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  KEY `idx_pid` (`pid`) USING BTREE,
  KEY `idx_enabled` (`enabled`) USING BTREE,
  KEY `idx_sys_dept_dept_id` (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门';

-- ----------------------------
-- Records of system_dept
-- ----------------------------
BEGIN;
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, 7, 1, '研发部', 3, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (5, 7, 0, '运维部', 4, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (6, 8, 0, '测试部', 6, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (7, NULL, 2, '华南分部', 0, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (8, NULL, 2, '华北分部', 1, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (15, 8, 0, 'UI部门', 7, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept` (`dept_id`, `pid`, `sub_count`, `name`, `dept_sort`, `enabled`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (17, 2, 0, '研发一组', 999, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='数据字典';

-- ----------------------------
-- Records of system_dict
-- ----------------------------
BEGIN;
INSERT INTO `system_dict` (`id`, `name`, `description`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, 'user_status', '用户状态', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict` (`id`, `name`, `description`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (4, 'dept_status', '部门状态', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict` (`id`, `name`, `description`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (5, 'job_status', '岗位状态', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_detail`;
CREATE TABLE `system_dict_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
  `dict_sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_dict_id` (`dict_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='数据字典详情';

-- ----------------------------
-- Records of system_dict_detail
-- ----------------------------
BEGIN;
INSERT INTO `system_dict_detail` (`id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, 1, '激活', 'true', 1, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail` (`id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, 1, '禁用', 'false', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail` (`id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (3, 4, '启用', 'true', 1, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail` (`id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (4, 4, '停用', 'false', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail` (`id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (5, 5, '启用', 'true', 1, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail` (`id`, `dict_id`, `label`, `value`, `dict_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (6, 5, '停用', 'false', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_email_config
-- ----------------------------
DROP TABLE IF EXISTS `system_email_config`;
CREATE TABLE `system_email_config` (
  `config_id` bigint NOT NULL COMMENT 'ID',
  `from_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收件人',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '端口',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='邮箱配置';

-- ----------------------------
-- Records of system_email_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `job_sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`) USING BTREE,
  KEY `idx_enabled` (`enabled`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='岗位';

-- ----------------------------
-- Records of system_job
-- ----------------------------
BEGIN;
INSERT INTO `system_job` (`id`, `name`, `enabled`, `job_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (8, '人事专员', b'1', 3, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_job` (`id`, `name`, `enabled`, `job_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (10, '产品经理', b'1', 4, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_job` (`id`, `name`, `enabled`, `job_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (11, '全栈开发', b'1', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_job` (`id`, `name`, `enabled`, `job_sort`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (12, '软件测试', b'1', 5, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_local_storage
-- ----------------------------
DROP TABLE IF EXISTS `system_local_storage`;
CREATE TABLE `system_local_storage` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路径',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类型',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '大小',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='本地存储';

-- ----------------------------
-- Records of system_local_storage
-- ----------------------------
BEGIN;
INSERT INTO `system_local_storage` (`id`, `real_name`, `name`, `suffix`, `path`, `type`, `size`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (11, 'bg-202504100131576.jpg', 'bg', 'jpg', 'C:\\cutejava\\file\\image\\bg-202504100131576.jpg', 'image', '20.20MB   ', 'admin', 'admin', '2025-04-10 13:32:00', '2025-04-10 13:32:00');
COMMIT;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint DEFAULT NULL COMMENT '上级菜单ID',
  `sub_count` int DEFAULT '0' COMMENT '子菜单数目',
  `type` int DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件',
  `menu_sort` int DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) DEFAULT NULL COMMENT '是否外链',
  `cache` bit(1) DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`) USING BTREE,
  UNIQUE KEY `uniq_title` (`title`) USING BTREE,
  KEY `idx_pid` (`pid`) USING BTREE,
  KEY `idx_sys_menu_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统菜单';

-- ----------------------------
-- Records of system_menu
-- ----------------------------
BEGIN;
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, NULL, 7, 0, '系统管理', NULL, NULL, 1, 'system', 'system', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (6, NULL, 3, 0, '系统监控', NULL, NULL, 10, 'monitor', 'monitor', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 18, 'sqlMonitor', 'druid', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (10, NULL, 12, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 35, 'email', 'email', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/base/Editor', 52, 'fwb', 'tinymce', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 34, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (21, NULL, 2, 0, '多级菜单', NULL, '', 900, 'menu', 'nested', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/index', 999, 'timing', 'timing', b'0', b'0', b'0', 'timing:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/base/MarkDown', 53, 'markdown', 'markdown', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/base/YamlEdit', 54, 'dev', 'yaml', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (36, NULL, 2, 0, '系统工具', NULL, '', 30, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'user:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'user:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'user:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', b'0', b'0', b'0', 'roles:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', b'0', b'0', b'0', 'roles:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'roles:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'menu:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'menu:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'menu:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dept:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dept:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dept:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'job:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'job:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'job:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dict:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dict:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dict:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'timing:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'timing:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'timing:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', b'0', b'0', b'0', 'storage:add', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'storage:edit', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'storage:del', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 14, 'codeConsole', 'server', b'0', b'0', b'0', 'monitor:list', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/base/Echarts', 50, 'chart', 'echarts', b'0', b'1', b'0', '', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (117, 10, 0, 1, '一键复制', 'clipboardDemo', 'components/base/ClipboardDemo', 999, 'menu', 'clipboardDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (118, 10, 0, 1, 'Java代码编辑器', 'javaCodemirror', 'components/base/CodemirrorDemo', 999, 'menu', 'javaCodemirror', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (119, 10, 0, 1, '数字滚动', 'countToDemo', 'components/base/CountToDemo', 999, 'menu', 'countToDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (120, 10, 0, 1, '拖拽表格', 'dragTableDemo', 'components/base/DragTableDemo', 999, 'menu', 'dragTableDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (121, 10, 0, 1, '分割面板', 'splitPaneDemo', 'components/base/SplitPaneDemo', 999, 'menu', 'splitPaneDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (122, 10, 0, 1, 'WebSocket', 'webSocketDemo', 'components/base/WebSocketDemo', 999, 'menu', 'webSocketDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_menu` (`menu_id`, `pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (125, 10, 0, 1, '业务组件Demo', 'DemoShow', 'components/business/DemoShow', 999, 'menu', 'bussinessDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_qiniu_config
-- ----------------------------
DROP TABLE IF EXISTS `system_qiniu_config`;
CREATE TABLE `system_qiniu_config` (
  `config_id` bigint NOT NULL COMMENT 'ID',
  `access_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'accessKey',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Bucket 识别符',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '外链域名',
  `secret_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'secretKey',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '空间类型',
  `zone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '机房',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='七牛云配置';

-- ----------------------------
-- Records of system_qiniu_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_qiniu_content
-- ----------------------------
DROP TABLE IF EXISTS `system_qiniu_content`;
CREATE TABLE `system_qiniu_content` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Bucket 识别符',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件名称',
  `size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件大小',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件类型：私有或公开',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件url',
  `suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件后缀',
  `update_time` datetime DEFAULT NULL COMMENT '上传或同步的时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='七牛云文件存储';

-- ----------------------------
-- Records of system_qiniu_content
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `system_quartz_job`;
CREATE TABLE `system_quartz_job` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'cron 表达式',
  `is_pause` bit(1) DEFAULT NULL COMMENT '状态：1暂停、0启用',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `person_in_charge` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '负责人',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '报警邮箱',
  `sub_task` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '子任务ID',
  `pause_after_failure` bit(1) DEFAULT NULL COMMENT '任务失败后是否暂停',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_is_pause` (`is_pause`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务';

-- ----------------------------
-- Records of system_quartz_job
-- ----------------------------
BEGIN;
INSERT INTO `system_quartz_job` (`id`, `bean_name`, `cron_expression`, `is_pause`, `job_name`, `method_name`, `params`, `description`, `person_in_charge`, `email`, `sub_task`, `pause_after_failure`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, 'testTask', '0/5 * * * * ?', b'1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL, NULL, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_quartz_job` (`id`, `bean_name`, `cron_expression`, `is_pause`, `job_name`, `method_name`, `params`, `description`, `person_in_charge`, `email`, `sub_task`, `pause_after_failure`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (3, 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '6', b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `system_quartz_log`;
CREATE TABLE `system_quartz_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Bean名称',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'cron 表达式',
  `is_success` bit(1) DEFAULT NULL COMMENT '是否执行成功',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参数',
  `time` bigint DEFAULT NULL COMMENT '执行耗时',
  `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '异常详情',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务日志';

-- ----------------------------
-- Records of system_quartz_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `level` int DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '数据权限',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`) USING BTREE,
  KEY `idx_level` (`level`) USING BTREE,
  KEY `idx_sys_role_level` (`level`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of system_role
-- ----------------------------
BEGIN;
INSERT INTO `system_role` (`role_id`, `name`, `level`, `description`, `data_scope`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (1, '超级管理员', 1, '666', '全部', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_role` (`role_id`, `name`, `level`, `description`, `data_scope`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (2, '普通用户', 2, '-', '本级', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_role` (`role_id`, `name`, `level`, `description`, `data_scope`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (7, 'test', 3, 'testtesttesttesttesttesttesttesttest', '全部', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_depts`;
CREATE TABLE `system_roles_depts` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE,
  KEY `idx_sys_roles_depts_role_dept` (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色部门关联';

-- ----------------------------
-- Records of system_roles_depts
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_menus`;
CREATE TABLE `system_roles_menus` (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`,`role_id`) USING BTREE,
  KEY `idx_menu_id` (`menu_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE,
  KEY `idx_sys_roles_menus_role_menu` (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联';

-- ----------------------------
-- Records of system_roles_menus
-- ----------------------------
BEGIN;
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (1, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (2, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (3, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (5, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (6, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (9, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (10, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (10, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (11, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (11, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (14, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (14, 7);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (15, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (15, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (18, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (18, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (18, 7);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (21, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (21, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (22, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (22, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (23, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (23, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (24, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (24, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (27, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (27, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (28, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (33, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (33, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (34, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (34, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (35, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (36, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (36, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (36, 7);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (37, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (39, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (41, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (44, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (45, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (46, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (48, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (49, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (50, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (52, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (53, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (54, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (56, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (57, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (58, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (60, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (61, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (62, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (64, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (65, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (66, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (73, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (74, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (75, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (77, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (77, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (77, 7);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (78, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (78, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (78, 7);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (79, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (79, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (79, 7);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (80, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (80, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (83, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (83, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (117, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (117, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (118, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (118, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (119, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (119, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (120, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (120, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (121, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (121, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (122, 1);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (122, 2);
INSERT INTO `system_roles_menus` (`menu_id`, `role_id`) VALUES (125, 1);
COMMIT;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门名称',
  `username` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `is_admin` bit(1) DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bit(1) DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新者',
  `pwd_reset_time` datetime DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `uniq_email` (`email`) USING BTREE,
  UNIQUE KEY `uniq_username` (`username`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE,
  KEY `idx_enabled` (`enabled`) USING BTREE,
  KEY `uniq_phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户';

-- ----------------------------
-- Records of system_user
-- ----------------------------
BEGIN;
INSERT INTO `system_user` (`user_id`, `dept_id`, `username`, `nick_name`, `gender`, `phone`, `email`, `avatar_name`, `avatar_path`, `password`, `is_admin`, `enabled`, `create_by`, `update_by`, `pwd_reset_time`, `create_time`, `update_time`) VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '1943815081@qq.com', '', '', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', b'1', 'admin', 'admin', '2020-05-03 16:38:31', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_user` (`user_id`, `dept_id`, `username`, `nick_name`, `gender`, `phone`, `email`, `avatar_name`, `avatar_path`, `password`, `is_admin`, `enabled`, `create_by`, `update_by`, `pwd_reset_time`, `create_time`, `update_time`) VALUES (3, 2, '测试', '测试', '男', '18888888881', '188888@qq.com', NULL, NULL, '$2a$10$DpAGWp3CYtYySPqJe.CnL.c/OCR0kkPLLAwAyID0yFHLamtYdYsvy', b'0', b'1', 'admin', 'admin', NULL, '2025-01-05 00:00:00', '2025-01-05 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for system_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS `system_users_jobs`;
CREATE TABLE `system_users_jobs` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `job_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`job_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_job_id` (`job_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of system_users_jobs
-- ----------------------------
BEGIN;
INSERT INTO `system_users_jobs` (`user_id`, `job_id`) VALUES (1, 11);
INSERT INTO `system_users_jobs` (`user_id`, `job_id`) VALUES (3, 11);
COMMIT;

-- ----------------------------
-- Table structure for system_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `system_users_roles`;
CREATE TABLE `system_users_roles` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色关联';

-- ----------------------------
-- Records of system_users_roles
-- ----------------------------
BEGIN;
INSERT INTO `system_users_roles` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `system_users_roles` (`user_id`, `role_id`) VALUES (3, 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
