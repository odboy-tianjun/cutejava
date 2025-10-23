/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80025 (8.0.25)
 Source Host           : 192.168.100.128:23306
 Source Schema         : cutejava

 Target Server Type    : MySQL
 Target Server Version : 80025 (8.0.25)
 File Encoding         : 65001
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_time_table_copy1
-- ----------------------------
DROP TABLE IF EXISTS `demo_time_table_copy1`;
CREATE TABLE `demo_time_table_copy1`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_time_table_copy1
-- ----------------------------

-- ----------------------------
-- Table structure for demo_user_time_logic_table
-- ----------------------------
DROP TABLE IF EXISTS `demo_user_time_logic_table`;
CREATE TABLE `demo_user_time_logic_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `available` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据有效性',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_user_time_logic_table
-- ----------------------------

-- ----------------------------
-- Table structure for demo_user_time_table
-- ----------------------------
DROP TABLE IF EXISTS `demo_user_time_table`;
CREATE TABLE `demo_user_time_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of demo_user_time_table
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `blob_data` blob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name` ASC, `trigger_name` ASC, `trigger_group` ASC) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `calendar` blob NOT NULL,
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cron_expression` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fired_time` bigint NOT NULL,
  `sched_time` bigint NOT NULL,
  `priority` int NOT NULL,
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE,
  INDEX `idx_qrtz_ft_trig_inst_name`(`sched_name` ASC, `instance_name` ASC) USING BTREE,
  INDEX `idx_qrtz_ft_inst_job_req_rcvry`(`sched_name` ASC, `instance_name` ASC, `requests_recovery` ASC) USING BTREE,
  INDEX `idx_qrtz_ft_j_g`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
  INDEX `idx_qrtz_ft_jg`(`sched_name` ASC, `job_group` ASC) USING BTREE,
  INDEX `idx_qrtz_ft_t_g`(`sched_name` ASC, `trigger_name` ASC, `trigger_group` ASC) USING BTREE,
  INDEX `idx_qrtz_ft_tg`(`sched_name` ASC, `trigger_group` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_data` blob NULL,
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE,
  INDEX `idx_qrtz_j_req_recovery`(`sched_name` ASC, `requests_recovery` ASC) USING BTREE,
  INDEX `idx_qrtz_j_grp`(`sched_name` ASC, `job_group` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('ClusteredScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('ClusteredScheduler', 'TRIGGER_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('cutejava', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('cutejava', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_checkin_time` bigint NOT NULL,
  `checkin_interval` bigint NOT NULL,
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('ClusteredScheduler', 'DESKTOP-FM44BLS1742558233429', 1742558255052, 20000);
INSERT INTO `qrtz_scheduler_state` VALUES ('cutejava', 'auto', 1761211053605, 10000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `repeat_count` bigint NOT NULL,
  `repeat_interval` bigint NOT NULL,
  `times_triggered` bigint NOT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `int_prop_1` int NULL DEFAULT NULL,
  `int_prop_2` int NULL DEFAULT NULL,
  `long_prop_1` bigint NULL DEFAULT NULL,
  `long_prop_2` bigint NULL DEFAULT NULL,
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL,
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL,
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `next_fire_time` bigint NULL DEFAULT NULL,
  `prev_fire_time` bigint NULL DEFAULT NULL,
  `priority` int NULL DEFAULT NULL,
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_time` bigint NOT NULL,
  `end_time` bigint NULL DEFAULT NULL,
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `misfire_instr` smallint NULL DEFAULT NULL,
  `job_data` blob NULL,
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `idx_qrtz_t_j`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
  INDEX `idx_qrtz_t_jg`(`sched_name` ASC, `job_group` ASC) USING BTREE,
  INDEX `idx_qrtz_t_c`(`sched_name` ASC, `calendar_name` ASC) USING BTREE,
  INDEX `idx_qrtz_t_g`(`sched_name` ASC, `trigger_group` ASC) USING BTREE,
  INDEX `idx_qrtz_t_state`(`sched_name` ASC, `trigger_state` ASC) USING BTREE,
  INDEX `idx_qrtz_t_n_state`(`sched_name` ASC, `trigger_name` ASC, `trigger_group` ASC, `trigger_state` ASC) USING BTREE,
  INDEX `idx_qrtz_t_n_g_state`(`sched_name` ASC, `trigger_group` ASC, `trigger_state` ASC) USING BTREE,
  INDEX `idx_qrtz_t_next_fire_time`(`sched_name` ASC, `next_fire_time` ASC) USING BTREE,
  INDEX `idx_qrtz_t_nft_st`(`sched_name` ASC, `trigger_state` ASC, `next_fire_time` ASC) USING BTREE,
  INDEX `idx_qrtz_t_nft_misfire`(`sched_name` ASC, `misfire_instr` ASC, `next_fire_time` ASC) USING BTREE,
  INDEX `idx_qrtz_t_nft_st_misfire`(`sched_name` ASC, `misfire_instr` ASC, `next_fire_time` ASC, `trigger_state` ASC) USING BTREE,
  INDEX `idx_qrtz_t_nft_st_misfire_grp`(`sched_name` ASC, `misfire_instr` ASC, `next_fire_time` ASC, `trigger_group` ASC, `trigger_state` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint NULL DEFAULT NULL COMMENT '上级部门',
  `sub_count` int NULL DEFAULT 0 COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `dept_sort` int NULL DEFAULT 999 COMMENT '排序',
  `enabled` bit(1) NOT NULL COMMENT '状态',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  INDEX `idx_pid`(`pid` ASC) USING BTREE,
  INDEX `idx_enabled`(`enabled` ASC) USING BTREE,
  INDEX `idx_sys_dept_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dept
-- ----------------------------
INSERT INTO `system_dept` VALUES (2, 7, 1, '研发部', 3, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dept` VALUES (5, 7, 0, '运维部', 4, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dept` VALUES (6, 8, 0, '测试部', 6, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dept` VALUES (7, NULL, 2, '华南分部', 0, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dept` VALUES (8, NULL, 2, '华北分部', 1, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dept` VALUES (15, 8, 0, 'UI部门', 7, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dept` VALUES (17, 2, 0, '研发一组', 999, b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict
-- ----------------------------
INSERT INTO `system_dict` VALUES (1, 'user_status', '用户状态', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict` VALUES (4, 'dept_status', '部门状态', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict` VALUES (5, 'job_status', '岗位状态', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict` VALUES (8, 'test_status', '测试状态', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_detail`;
CREATE TABLE `system_dict_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` bigint NULL DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
  `dict_sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_id`(`dict_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict_detail
-- ----------------------------
INSERT INTO `system_dict_detail` VALUES (1, 1, '激活', 'true', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (2, 1, '禁用', 'false', 2, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (3, 4, '启用', 'true', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (4, 4, '停用', 'false', 2, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (5, 5, '启用', 'true', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (6, 5, '停用', 'false', 2, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (7, 8, '打瞌睡01', 'ks01', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (8, 8, '打瞌睡2', 'ks02', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (9, 8, '打瞌睡3', 'ks03', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (10, 8, '打瞌睡4', 'ks04', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (11, 8, '打瞌睡5', 'ks05', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (12, 8, '打瞌睡6', 'ks06', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (13, 8, '打瞌睡7', 'ks07', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (14, 8, '打瞌睡8', 'ks08', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (15, 8, '打瞌睡9', 'ks09', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (16, 8, '打瞌睡10', 'ks10', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (17, 8, '打瞌睡11', 'ks11', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (18, 8, '打瞌睡12', 'ks12', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (19, 8, '打瞌睡13', 'ks13', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (20, 8, '打瞌睡14', 'ks14', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (21, 8, '打瞌睡15', 'ks15', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (22, 8, '打瞌睡16', 'ks16', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (23, 8, '打瞌睡17', 'ks17', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (24, 8, '打瞌睡18', 'ks18', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (25, 8, '打瞌睡19', 'ks19', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (26, 8, '打瞌睡20', 'ks20', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (27, 8, '打瞌睡21', 'ks21', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_dict_detail` VALUES (28, 8, '打瞌睡22', 'ks22', 1, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_email_config
-- ----------------------------
DROP TABLE IF EXISTS `system_email_config`;
CREATE TABLE `system_email_config`  (
  `config_id` bigint NOT NULL COMMENT 'ID',
  `from_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收件人',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮件服务器SMTP地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '端口',
  `user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发件者用户名',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '邮箱配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_email_config
-- ----------------------------

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `enabled` bit(1) NOT NULL COMMENT '岗位状态',
  `job_sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
  INDEX `idx_enabled`(`enabled` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_job
-- ----------------------------
INSERT INTO `system_job` VALUES (8, '人事专员', b'1', 3, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_job` VALUES (10, '产品经理', b'1', 4, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_job` VALUES (11, '全栈开发', b'1', 2, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_job` VALUES (12, '软件测试', b'1', 5, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_local_storage
-- ----------------------------
DROP TABLE IF EXISTS `system_local_storage`;
CREATE TABLE `system_local_storage`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大小',
  `date_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日期分组',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '本地存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_local_storage
-- ----------------------------

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint NULL DEFAULT NULL COMMENT '上级菜单ID',
  `sub_count` int NULL DEFAULT 0 COMMENT '子菜单数目',
  `type` int NULL DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件',
  `menu_sort` int NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接地址',
  `i_frame` bit(1) NULL DEFAULT NULL COMMENT '是否外链',
  `cache` bit(1) NULL DEFAULT b'0' COMMENT '缓存',
  `hidden` bit(1) NULL DEFAULT b'0' COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
  UNIQUE INDEX `uniq_title`(`title` ASC) USING BTREE,
  INDEX `idx_pid`(`pid` ASC) USING BTREE,
  INDEX `idx_sys_menu_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES (1, NULL, 9, 0, '系统管理', NULL, NULL, 998, 'system', 'system', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (6, 1, 3, 0, '系统监控', NULL, NULL, 11, 'monitor', 'monitor', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (9, 6, 0, 1, 'SQL监控', 'DruidSqlConsole', 'system/druidSql/index', 18, 'sqlMonitor', 'druid-console', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (10, NULL, 16, 0, '组件管理', NULL, NULL, 999, 'zujian', 'components', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'system/email/index', 35, 'email', 'email', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'system/storage/index', 34, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (21, NULL, 2, 0, '演示:多级菜单', NULL, '', 999, 'menu', 'nested', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (28, 1, 3, 1, '任务调度', 'QuartzJob', 'system/quartzJob/index', 9, 'timing', 'quartz-job', b'0', b'0', b'0', 'quartzJob:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (34, 10, 0, 1, '演示:Yaml编辑器', 'CuteYamlEditorDemo', 'componentsDemo/CuteYamlEditorDemo', 999, 'menu', 'cuteYamlEditorDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (36, 1, 2, 0, '系统工具', NULL, '', 10, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'system/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'user:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'user:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'user:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', b'0', b'0', b'0', 'roles:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', b'0', b'0', b'0', 'roles:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'roles:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'menu:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'menu:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'menu:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dept:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dept:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dept:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'job:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'job:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'job:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dict:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dict:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dict:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'quartzJob:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'quartzJob:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'quartzJob:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', b'0', b'0', b'0', 'storage:add', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'storage:edit', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'storage:del', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'system/server/index', 14, 'codeConsole', 'server', b'0', b'0', b'0', 'monitor:list', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/base/Echarts', 50, 'chart', 'echarts', b'0', b'1', b'0', '', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (117, 10, 0, 1, '演示:一键复制组件', 'CuteOneKeyCopy', 'componentsDemo/CuteOneKeyCopyDemo', 999, 'menu', 'cuteOneKeyCopyDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (118, 10, 0, 1, '演示:Java代码编辑器', 'CuteJavaEditorDemo', 'componentsDemo/CuteJavaEditorDemo', 999, 'menu', 'cuteJavaEditorDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (119, 10, 0, 1, '数字滚动', 'countToDemo', 'components/base/CountToDemo', 56, 'menu', 'countToDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (121, 10, 0, 1, '分割面板', 'splitPaneDemo', 'components/base/SplitPaneDemo', 57, 'menu', 'splitPaneDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (122, 10, 0, 1, 'WebSocket', 'webSocketDemo', 'components/base/WebSocketDemo', 58, 'menu', 'webSocketDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (125, 10, 0, 1, '演示:业务组件', 'CuteBusinessDemo', 'componentsDemo/CuteBusinessDemo', 999, 'menu', 'cuteBusinessDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (126, 10, 2, 1, '演示:简单表格组件', 'CuteSimpleTableDemo', 'componentsDemo/CuteSimpleTableDemo', 999, 'menu', 'cuteSimpleTableDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (127, 10, 2, 1, '演示:拖拽表格组件', 'CuteDragTableDemo', 'componentsDemo/CuteDragTableDemo', 999, 'menu', 'cuteDragTableDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (128, 10, 0, 1, '演示:Markdown编辑器', 'CuteMarkdownDemo', 'componentsDemo/CuteMarkdownDemo', 999, 'menu', 'cuteMarkdownDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (129, 10, 0, 1, '演示:富文本编辑器', 'CuteRichTextEditorDemo', 'componentsDemo/CuteRichTextEditorDemo', 999, 'menu', 'cuteRichTextEditorDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (130, 10, 0, 1, '演示:文件上传', 'CuteFileUploadDemo', 'componentsDemo/CuteFileUploadDemo', 999, 'menu', 'cuteFileUploadDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (132, 10, 0, 1, '演示:文件拖拽上传', 'CuteFileDragUploadDemo', 'componentsDemo/CuteFileDragUploadDemo', 999, 'menu', 'cuteFileDragUploadDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_menu` VALUES (150, 10, 0, 1, '演示:其他基础组件', 'CuteOtherDemo', 'componentsDemo/CuteOtherDemo', 999, 'menu', 'cuteOtherDemo', b'0', b'0', b'0', NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `system_operation_log`;
CREATE TABLE `system_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `biz_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `request_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `execute_time` bigint NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `browser_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '审计日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for system_oss_storage
-- ----------------------------
DROP TABLE IF EXISTS `system_oss_storage`;
CREATE TABLE `system_oss_storage`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `service_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型，比如minio',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务地址',
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存储桶名称',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '完整文件名称',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小, 单位：字节',
  `file_mime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '短文件名',
  `file_suffix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `file_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件md5',
  `file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件链接',
  `file_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件编码',
  `object_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对象路径',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `udx_filemd5`(`file_md5` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OSS存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_oss_storage
-- ----------------------------

-- ----------------------------
-- Table structure for system_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `system_quartz_job`;
CREATE TABLE `system_quartz_job`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Spring Bean名称',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
  `is_pause` bit(1) NULL DEFAULT NULL COMMENT '状态：1暂停、0启用',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `person_in_charge` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报警邮箱',
  `sub_task` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子任务ID',
  `pause_after_failure` bit(1) NULL DEFAULT NULL COMMENT '任务失败后是否暂停',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_is_pause`(`is_pause` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_quartz_job
-- ----------------------------
INSERT INTO `system_quartz_job` VALUES (2, 'testTask', '0/5 * * * * ?', b'1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL, NULL, 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_quartz_job` VALUES (3, 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '6', b'1', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `system_quartz_log`;
CREATE TABLE `system_quartz_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Bean名称',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
  `is_success` bit(1) NULL DEFAULT NULL COMMENT '是否执行成功',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名称',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
  `time` bigint NULL DEFAULT NULL COMMENT '执行耗时',
  `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常详情',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_quartz_log
-- ----------------------------

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `level` int NULL DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据权限',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
  INDEX `idx_level`(`level` ASC) USING BTREE,
  INDEX `idx_sys_role_level`(`level` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES (1, '超级管理员', 1, '6666', '全部', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_role` VALUES (2, '普通用户', 2, '-', '本级', 'admin', 'admin', '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_depts`;
CREATE TABLE `system_roles_depts`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_sys_roles_depts_role_dept`(`role_id` ASC, `dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色部门关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_roles_depts
-- ----------------------------

-- ----------------------------
-- Table structure for system_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_menus`;
CREATE TABLE `system_roles_menus`  (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_sys_roles_menus_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_roles_menus
-- ----------------------------
INSERT INTO `system_roles_menus` VALUES (1, 1);
INSERT INTO `system_roles_menus` VALUES (2, 1);
INSERT INTO `system_roles_menus` VALUES (3, 1);
INSERT INTO `system_roles_menus` VALUES (5, 1);
INSERT INTO `system_roles_menus` VALUES (6, 1);
INSERT INTO `system_roles_menus` VALUES (9, 1);
INSERT INTO `system_roles_menus` VALUES (10, 1);
INSERT INTO `system_roles_menus` VALUES (10, 2);
INSERT INTO `system_roles_menus` VALUES (11, 1);
INSERT INTO `system_roles_menus` VALUES (11, 2);
INSERT INTO `system_roles_menus` VALUES (14, 1);
INSERT INTO `system_roles_menus` VALUES (18, 1);
INSERT INTO `system_roles_menus` VALUES (18, 2);
INSERT INTO `system_roles_menus` VALUES (21, 1);
INSERT INTO `system_roles_menus` VALUES (21, 2);
INSERT INTO `system_roles_menus` VALUES (22, 1);
INSERT INTO `system_roles_menus` VALUES (22, 2);
INSERT INTO `system_roles_menus` VALUES (23, 1);
INSERT INTO `system_roles_menus` VALUES (23, 2);
INSERT INTO `system_roles_menus` VALUES (24, 1);
INSERT INTO `system_roles_menus` VALUES (24, 2);
INSERT INTO `system_roles_menus` VALUES (27, 1);
INSERT INTO `system_roles_menus` VALUES (27, 2);
INSERT INTO `system_roles_menus` VALUES (28, 1);
INSERT INTO `system_roles_menus` VALUES (34, 1);
INSERT INTO `system_roles_menus` VALUES (34, 2);
INSERT INTO `system_roles_menus` VALUES (35, 1);
INSERT INTO `system_roles_menus` VALUES (36, 1);
INSERT INTO `system_roles_menus` VALUES (36, 2);
INSERT INTO `system_roles_menus` VALUES (37, 1);
INSERT INTO `system_roles_menus` VALUES (39, 1);
INSERT INTO `system_roles_menus` VALUES (41, 1);
INSERT INTO `system_roles_menus` VALUES (44, 1);
INSERT INTO `system_roles_menus` VALUES (45, 1);
INSERT INTO `system_roles_menus` VALUES (46, 1);
INSERT INTO `system_roles_menus` VALUES (48, 1);
INSERT INTO `system_roles_menus` VALUES (49, 1);
INSERT INTO `system_roles_menus` VALUES (50, 1);
INSERT INTO `system_roles_menus` VALUES (52, 1);
INSERT INTO `system_roles_menus` VALUES (53, 1);
INSERT INTO `system_roles_menus` VALUES (54, 1);
INSERT INTO `system_roles_menus` VALUES (56, 1);
INSERT INTO `system_roles_menus` VALUES (57, 1);
INSERT INTO `system_roles_menus` VALUES (58, 1);
INSERT INTO `system_roles_menus` VALUES (60, 1);
INSERT INTO `system_roles_menus` VALUES (61, 1);
INSERT INTO `system_roles_menus` VALUES (62, 1);
INSERT INTO `system_roles_menus` VALUES (64, 1);
INSERT INTO `system_roles_menus` VALUES (65, 1);
INSERT INTO `system_roles_menus` VALUES (66, 1);
INSERT INTO `system_roles_menus` VALUES (73, 1);
INSERT INTO `system_roles_menus` VALUES (74, 1);
INSERT INTO `system_roles_menus` VALUES (75, 1);
INSERT INTO `system_roles_menus` VALUES (77, 1);
INSERT INTO `system_roles_menus` VALUES (77, 2);
INSERT INTO `system_roles_menus` VALUES (78, 1);
INSERT INTO `system_roles_menus` VALUES (78, 2);
INSERT INTO `system_roles_menus` VALUES (79, 1);
INSERT INTO `system_roles_menus` VALUES (79, 2);
INSERT INTO `system_roles_menus` VALUES (80, 1);
INSERT INTO `system_roles_menus` VALUES (80, 2);
INSERT INTO `system_roles_menus` VALUES (83, 1);
INSERT INTO `system_roles_menus` VALUES (83, 2);
INSERT INTO `system_roles_menus` VALUES (117, 1);
INSERT INTO `system_roles_menus` VALUES (117, 2);
INSERT INTO `system_roles_menus` VALUES (118, 1);
INSERT INTO `system_roles_menus` VALUES (118, 2);
INSERT INTO `system_roles_menus` VALUES (119, 1);
INSERT INTO `system_roles_menus` VALUES (119, 2);
INSERT INTO `system_roles_menus` VALUES (121, 1);
INSERT INTO `system_roles_menus` VALUES (121, 2);
INSERT INTO `system_roles_menus` VALUES (122, 1);
INSERT INTO `system_roles_menus` VALUES (122, 2);
INSERT INTO `system_roles_menus` VALUES (125, 1);
INSERT INTO `system_roles_menus` VALUES (125, 2);
INSERT INTO `system_roles_menus` VALUES (126, 1);
INSERT INTO `system_roles_menus` VALUES (126, 2);
INSERT INTO `system_roles_menus` VALUES (127, 1);
INSERT INTO `system_roles_menus` VALUES (127, 2);
INSERT INTO `system_roles_menus` VALUES (128, 1);
INSERT INTO `system_roles_menus` VALUES (128, 2);
INSERT INTO `system_roles_menus` VALUES (129, 1);
INSERT INTO `system_roles_menus` VALUES (129, 2);
INSERT INTO `system_roles_menus` VALUES (130, 1);
INSERT INTO `system_roles_menus` VALUES (130, 2);
INSERT INTO `system_roles_menus` VALUES (132, 1);
INSERT INTO `system_roles_menus` VALUES (132, 2);
INSERT INTO `system_roles_menus` VALUES (150, 1);

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门名称',
  `username` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `email` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `is_admin` bit(1) NULL DEFAULT b'0' COMMENT '是否为admin账号',
  `enabled` bit(1) NULL DEFAULT NULL COMMENT '状态：1启用、0禁用',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
  `pwd_reset_time` datetime NULL DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uniq_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username` ASC) USING BTREE,
  INDEX `idx_dept_id`(`dept_id` ASC) USING BTREE,
  INDEX `idx_enabled`(`enabled` ASC) USING BTREE,
  INDEX `uniq_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '1943815081@qq.com', '', '', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', b'1', 'admin', 'admin', '2020-05-03 16:38:31', '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (4, 2, 'odboy100', 'odboy100测试用户', '男', '18797874100', '1943815100@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (5, 2, 'odboy101', 'odboy101测试用户', '男', '18797874101', '1943815101@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (6, 2, 'odboy102', 'odboy102测试用户', '男', '18797874102', '1943815102@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (7, 2, 'odboy103', 'odboy103测试用户', '男', '18797874103', '1943815103@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (8, 2, 'odboy104', 'odboy104测试用户', '男', '18797874104', '1943815104@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (9, 2, 'odboy105', 'odboy105测试用户', '男', '18797874105', '1943815105@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (10, 2, 'odboy106', 'odboy106测试用户', '男', '18797874106', '1943815106@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (11, 2, 'odboy107', 'odboy107测试用户', '男', '18797874107', '1943815107@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (12, 2, 'odboy108', 'odboy108测试用户', '男', '18797874108', '1943815108@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (13, 2, 'odboy109', 'odboy109测试用户', '男', '18797874109', '1943815109@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (14, 2, 'odboy110', 'odboy110测试用户', '男', '18797874110', '1943815110@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (15, 2, 'odboy111', 'odboy111测试用户', '男', '18797874111', '1943815111@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (16, 2, 'odboy112', 'odboy112测试用户', '男', '18797874112', '1943815112@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (17, 2, 'odboy113', 'odboy113测试用户', '男', '18797874113', '1943815113@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (18, 2, 'odboy114', 'odboy114测试用户', '男', '18797874114', '1943815114@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (19, 2, 'odboy115', 'odboy115测试用户', '男', '18797874115', '1943815115@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (20, 2, 'odboy116', 'odboy116测试用户', '男', '18797874116', '1943815116@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (21, 2, 'odboy117', 'odboy117测试用户', '男', '18797874117', '1943815117@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (22, 2, 'odboy118', 'odboy118测试用户', '男', '18797874118', '1943815118@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (23, 2, 'odboy119', 'odboy119测试用户', '男', '18797874119', '1943815119@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (24, 2, 'odboy120', 'odboy120测试用户', '男', '18797874120', '1943815120@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (25, 2, 'odboy121', 'odboy121测试用户', '男', '18797874121', '1943815121@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (26, 2, 'odboy122', 'odboy122测试用户', '男', '18797874122', '1943815122@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (27, 2, 'odboy123', 'odboy123测试用户', '男', '18797874123', '1943815123@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (28, 2, 'odboy124', 'odboy124测试用户', '男', '18797874124', '1943815124@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (29, 2, 'odboy125', 'odboy125测试用户', '男', '18797874125', '1943815125@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (30, 2, 'odboy126', 'odboy126测试用户', '男', '18797874126', '1943815126@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (31, 2, 'odboy127', 'odboy127测试用户', '男', '18797874127', '1943815127@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (32, 2, 'odboy128', 'odboy128测试用户', '男', '18797874128', '1943815128@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (33, 2, 'odboy129', 'odboy129测试用户', '男', '18797874129', '1943815129@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (34, 2, 'odboy130', 'odboy130测试用户', '男', '18797874130', '1943815130@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (35, 2, 'odboy131', 'odboy131测试用户', '男', '18797874131', '1943815131@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (36, 2, 'odboy132', 'odboy132测试用户', '男', '18797874132', '1943815132@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (37, 2, 'odboy133', 'odboy133测试用户', '男', '18797874133', '1943815133@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (38, 2, 'odboy134', 'odboy134测试用户', '男', '18797874134', '1943815134@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (39, 2, 'odboy135', 'odboy135测试用户', '男', '18797874135', '1943815135@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (40, 2, 'odboy136', 'odboy136测试用户', '男', '18797874136', '1943815136@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (41, 2, 'odboy137', 'odboy137测试用户', '男', '18797874137', '1943815137@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (42, 2, 'odboy138', 'odboy138测试用户', '男', '18797874138', '1943815138@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (43, 2, 'odboy139', 'odboy139测试用户', '男', '18797874139', '1943815139@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (44, 2, 'odboy140', 'odboy140测试用户', '男', '18797874140', '1943815140@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (45, 2, 'odboy141', 'odboy141测试用户', '男', '18797874141', '1943815141@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (46, 2, 'odboy142', 'odboy142测试用户', '男', '18797874142', '1943815142@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (47, 2, 'odboy143', 'odboy143测试用户', '男', '18797874143', '1943815143@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (48, 2, 'odboy144', 'odboy144测试用户', '男', '18797874144', '1943815144@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (49, 2, 'odboy145', 'odboy145测试用户', '男', '18797874145', '1943815145@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (50, 2, 'odboy146', 'odboy146测试用户', '男', '18797874146', '1943815146@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (51, 2, 'odboy147', 'odboy147测试用户', '男', '18797874147', '1943815147@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (52, 2, 'odboy148', 'odboy148测试用户', '男', '18797874148', '1943815148@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (53, 2, 'odboy149', 'odboy149测试用户', '男', '18797874149', '1943815149@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (54, 2, 'odboy150', 'odboy150测试用户', '男', '18797874150', '1943815150@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (55, 2, 'odboy151', 'odboy151测试用户', '男', '18797874151', '1943815151@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (56, 2, 'odboy152', 'odboy152测试用户', '男', '18797874152', '1943815152@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (57, 2, 'odboy153', 'odboy153测试用户', '男', '18797874153', '1943815153@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (58, 2, 'odboy154', 'odboy154测试用户', '男', '18797874154', '1943815154@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (59, 2, 'odboy155', 'odboy155测试用户', '男', '18797874155', '1943815155@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (60, 2, 'odboy156', 'odboy156测试用户', '男', '18797874156', '1943815156@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (61, 2, 'odboy157', 'odboy157测试用户', '男', '18797874157', '1943815157@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (62, 2, 'odboy158', 'odboy158测试用户', '男', '18797874158', '1943815158@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (63, 2, 'odboy159', 'odboy159测试用户', '男', '18797874159', '1943815159@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (64, 2, 'odboy160', 'odboy160测试用户', '男', '18797874160', '1943815160@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (65, 2, 'odboy161', 'odboy161测试用户', '男', '18797874161', '1943815161@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (66, 2, 'odboy162', 'odboy162测试用户', '男', '18797874162', '1943815162@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (67, 2, 'odboy163', 'odboy163测试用户', '男', '18797874163', '1943815163@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (68, 2, 'odboy164', 'odboy164测试用户', '男', '18797874164', '1943815164@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (69, 2, 'odboy165', 'odboy165测试用户', '男', '18797874165', '1943815165@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (70, 2, 'odboy166', 'odboy166测试用户', '男', '18797874166', '1943815166@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (71, 2, 'odboy167', 'odboy167测试用户', '男', '18797874167', '1943815167@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (72, 2, 'odboy168', 'odboy168测试用户', '男', '18797874168', '1943815168@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (73, 2, 'odboy169', 'odboy169测试用户', '男', '18797874169', '1943815169@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (74, 2, 'odboy170', 'odboy170测试用户', '男', '18797874170', '1943815170@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (75, 2, 'odboy171', 'odboy171测试用户', '男', '18797874171', '1943815171@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (76, 2, 'odboy172', 'odboy172测试用户', '男', '18797874172', '1943815172@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (77, 2, 'odboy173', 'odboy173测试用户', '男', '18797874173', '1943815173@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (78, 2, 'odboy174', 'odboy174测试用户', '男', '18797874174', '1943815174@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (79, 2, 'odboy175', 'odboy175测试用户', '男', '18797874175', '1943815175@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (80, 2, 'odboy176', 'odboy176测试用户', '男', '18797874176', '1943815176@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (81, 2, 'odboy177', 'odboy177测试用户', '男', '18797874177', '1943815177@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (82, 2, 'odboy178', 'odboy178测试用户', '男', '18797874178', '1943815178@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (83, 2, 'odboy179', 'odboy179测试用户', '男', '18797874179', '1943815179@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (84, 2, 'odboy180', 'odboy180测试用户', '男', '18797874180', '1943815180@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (85, 2, 'odboy181', 'odboy181测试用户', '男', '18797874181', '1943815181@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (86, 2, 'odboy182', 'odboy182测试用户', '男', '18797874182', '1943815182@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (87, 2, 'odboy183', 'odboy183测试用户', '男', '18797874183', '1943815183@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (88, 2, 'odboy184', 'odboy184测试用户', '男', '18797874184', '1943815184@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (89, 2, 'odboy185', 'odboy185测试用户', '男', '18797874185', '1943815185@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (90, 2, 'odboy186', 'odboy186测试用户', '男', '18797874186', '1943815186@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (91, 2, 'odboy187', 'odboy187测试用户', '男', '18797874187', '1943815187@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (92, 2, 'odboy188', 'odboy188测试用户', '男', '18797874188', '1943815188@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (93, 2, 'odboy189', 'odboy189测试用户', '男', '18797874189', '1943815189@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (94, 2, 'odboy190', 'odboy190测试用户', '男', '18797874190', '1943815190@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (95, 2, 'odboy191', 'odboy191测试用户', '男', '18797874191', '1943815191@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (96, 2, 'odboy192', 'odboy192测试用户', '男', '18797874192', '1943815192@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (97, 2, 'odboy193', 'odboy193测试用户', '男', '18797874193', '1943815193@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (98, 2, 'odboy194', 'odboy194测试用户', '男', '18797874194', '1943815194@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (99, 2, 'odboy195', 'odboy195测试用户', '男', '18797874195', '1943815195@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (100, 2, 'odboy196', 'odboy196测试用户', '男', '18797874196', '1943815196@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (101, 2, 'odboy197', 'odboy197测试用户', '男', '18797874197', '1943815197@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (102, 2, 'odboy198', 'odboy198测试用户', '男', '18797874198', '1943815198@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');
INSERT INTO `system_user` VALUES (103, 2, 'odboy199', 'odboy199测试用户', '男', '18797874199', '1943815199@qq.com', NULL, NULL, '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'0', b'1', 'admin', 'admin', NULL, '2021-03-20 22:04:13', '2021-03-20 22:04:13');

-- ----------------------------
-- Table structure for system_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS `system_users_jobs`;
CREATE TABLE `system_users_jobs`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `job_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `job_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_job_id`(`job_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_users_jobs
-- ----------------------------
INSERT INTO `system_users_jobs` VALUES (1, 11);
INSERT INTO `system_users_jobs` VALUES (3, 11);
INSERT INTO `system_users_jobs` VALUES (4, 11);
INSERT INTO `system_users_jobs` VALUES (5, 11);
INSERT INTO `system_users_jobs` VALUES (6, 11);
INSERT INTO `system_users_jobs` VALUES (7, 11);
INSERT INTO `system_users_jobs` VALUES (8, 11);
INSERT INTO `system_users_jobs` VALUES (9, 11);
INSERT INTO `system_users_jobs` VALUES (10, 11);
INSERT INTO `system_users_jobs` VALUES (11, 11);
INSERT INTO `system_users_jobs` VALUES (12, 11);
INSERT INTO `system_users_jobs` VALUES (13, 11);
INSERT INTO `system_users_jobs` VALUES (14, 11);
INSERT INTO `system_users_jobs` VALUES (15, 11);
INSERT INTO `system_users_jobs` VALUES (16, 11);
INSERT INTO `system_users_jobs` VALUES (17, 11);
INSERT INTO `system_users_jobs` VALUES (18, 11);
INSERT INTO `system_users_jobs` VALUES (19, 11);
INSERT INTO `system_users_jobs` VALUES (20, 11);
INSERT INTO `system_users_jobs` VALUES (21, 11);
INSERT INTO `system_users_jobs` VALUES (22, 11);
INSERT INTO `system_users_jobs` VALUES (23, 11);
INSERT INTO `system_users_jobs` VALUES (24, 11);
INSERT INTO `system_users_jobs` VALUES (25, 11);
INSERT INTO `system_users_jobs` VALUES (26, 11);
INSERT INTO `system_users_jobs` VALUES (27, 11);
INSERT INTO `system_users_jobs` VALUES (28, 11);
INSERT INTO `system_users_jobs` VALUES (29, 11);
INSERT INTO `system_users_jobs` VALUES (30, 11);
INSERT INTO `system_users_jobs` VALUES (31, 11);
INSERT INTO `system_users_jobs` VALUES (32, 11);
INSERT INTO `system_users_jobs` VALUES (33, 11);
INSERT INTO `system_users_jobs` VALUES (34, 11);
INSERT INTO `system_users_jobs` VALUES (35, 11);
INSERT INTO `system_users_jobs` VALUES (36, 11);
INSERT INTO `system_users_jobs` VALUES (37, 11);
INSERT INTO `system_users_jobs` VALUES (38, 11);
INSERT INTO `system_users_jobs` VALUES (39, 11);
INSERT INTO `system_users_jobs` VALUES (40, 11);
INSERT INTO `system_users_jobs` VALUES (41, 11);
INSERT INTO `system_users_jobs` VALUES (42, 11);
INSERT INTO `system_users_jobs` VALUES (43, 11);
INSERT INTO `system_users_jobs` VALUES (44, 11);
INSERT INTO `system_users_jobs` VALUES (45, 11);
INSERT INTO `system_users_jobs` VALUES (46, 11);
INSERT INTO `system_users_jobs` VALUES (47, 11);
INSERT INTO `system_users_jobs` VALUES (48, 11);
INSERT INTO `system_users_jobs` VALUES (49, 11);
INSERT INTO `system_users_jobs` VALUES (50, 11);
INSERT INTO `system_users_jobs` VALUES (51, 11);
INSERT INTO `system_users_jobs` VALUES (52, 11);
INSERT INTO `system_users_jobs` VALUES (53, 11);
INSERT INTO `system_users_jobs` VALUES (54, 11);
INSERT INTO `system_users_jobs` VALUES (55, 11);
INSERT INTO `system_users_jobs` VALUES (56, 11);
INSERT INTO `system_users_jobs` VALUES (57, 11);
INSERT INTO `system_users_jobs` VALUES (58, 11);
INSERT INTO `system_users_jobs` VALUES (59, 11);
INSERT INTO `system_users_jobs` VALUES (60, 11);
INSERT INTO `system_users_jobs` VALUES (61, 11);
INSERT INTO `system_users_jobs` VALUES (62, 11);
INSERT INTO `system_users_jobs` VALUES (63, 11);
INSERT INTO `system_users_jobs` VALUES (64, 11);
INSERT INTO `system_users_jobs` VALUES (65, 11);
INSERT INTO `system_users_jobs` VALUES (66, 11);
INSERT INTO `system_users_jobs` VALUES (67, 11);
INSERT INTO `system_users_jobs` VALUES (68, 11);
INSERT INTO `system_users_jobs` VALUES (69, 11);
INSERT INTO `system_users_jobs` VALUES (70, 11);
INSERT INTO `system_users_jobs` VALUES (71, 11);
INSERT INTO `system_users_jobs` VALUES (72, 11);
INSERT INTO `system_users_jobs` VALUES (73, 11);
INSERT INTO `system_users_jobs` VALUES (74, 11);
INSERT INTO `system_users_jobs` VALUES (75, 11);
INSERT INTO `system_users_jobs` VALUES (76, 11);
INSERT INTO `system_users_jobs` VALUES (77, 11);
INSERT INTO `system_users_jobs` VALUES (78, 11);
INSERT INTO `system_users_jobs` VALUES (79, 11);
INSERT INTO `system_users_jobs` VALUES (80, 11);
INSERT INTO `system_users_jobs` VALUES (81, 11);
INSERT INTO `system_users_jobs` VALUES (82, 11);
INSERT INTO `system_users_jobs` VALUES (83, 11);
INSERT INTO `system_users_jobs` VALUES (84, 11);
INSERT INTO `system_users_jobs` VALUES (85, 11);
INSERT INTO `system_users_jobs` VALUES (86, 11);
INSERT INTO `system_users_jobs` VALUES (87, 11);
INSERT INTO `system_users_jobs` VALUES (88, 11);
INSERT INTO `system_users_jobs` VALUES (89, 11);
INSERT INTO `system_users_jobs` VALUES (90, 11);
INSERT INTO `system_users_jobs` VALUES (91, 11);
INSERT INTO `system_users_jobs` VALUES (92, 11);
INSERT INTO `system_users_jobs` VALUES (93, 11);
INSERT INTO `system_users_jobs` VALUES (94, 11);
INSERT INTO `system_users_jobs` VALUES (95, 11);
INSERT INTO `system_users_jobs` VALUES (96, 11);
INSERT INTO `system_users_jobs` VALUES (97, 11);
INSERT INTO `system_users_jobs` VALUES (98, 11);
INSERT INTO `system_users_jobs` VALUES (99, 11);
INSERT INTO `system_users_jobs` VALUES (100, 11);
INSERT INTO `system_users_jobs` VALUES (101, 11);
INSERT INTO `system_users_jobs` VALUES (102, 11);
INSERT INTO `system_users_jobs` VALUES (103, 11);

-- ----------------------------
-- Table structure for system_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `system_users_roles`;
CREATE TABLE `system_users_roles`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_users_roles
-- ----------------------------
INSERT INTO `system_users_roles` VALUES (1, 1);
INSERT INTO `system_users_roles` VALUES (3, 2);
INSERT INTO `system_users_roles` VALUES (4, 2);
INSERT INTO `system_users_roles` VALUES (5, 2);
INSERT INTO `system_users_roles` VALUES (6, 2);
INSERT INTO `system_users_roles` VALUES (7, 2);
INSERT INTO `system_users_roles` VALUES (8, 2);
INSERT INTO `system_users_roles` VALUES (9, 2);
INSERT INTO `system_users_roles` VALUES (10, 2);
INSERT INTO `system_users_roles` VALUES (11, 2);
INSERT INTO `system_users_roles` VALUES (12, 2);
INSERT INTO `system_users_roles` VALUES (13, 2);
INSERT INTO `system_users_roles` VALUES (14, 2);
INSERT INTO `system_users_roles` VALUES (15, 2);
INSERT INTO `system_users_roles` VALUES (16, 2);
INSERT INTO `system_users_roles` VALUES (17, 2);
INSERT INTO `system_users_roles` VALUES (18, 2);
INSERT INTO `system_users_roles` VALUES (19, 2);
INSERT INTO `system_users_roles` VALUES (20, 2);
INSERT INTO `system_users_roles` VALUES (21, 2);
INSERT INTO `system_users_roles` VALUES (22, 2);
INSERT INTO `system_users_roles` VALUES (23, 2);
INSERT INTO `system_users_roles` VALUES (24, 2);
INSERT INTO `system_users_roles` VALUES (25, 2);
INSERT INTO `system_users_roles` VALUES (26, 2);
INSERT INTO `system_users_roles` VALUES (27, 2);
INSERT INTO `system_users_roles` VALUES (28, 2);
INSERT INTO `system_users_roles` VALUES (29, 2);
INSERT INTO `system_users_roles` VALUES (30, 2);
INSERT INTO `system_users_roles` VALUES (31, 2);
INSERT INTO `system_users_roles` VALUES (32, 2);
INSERT INTO `system_users_roles` VALUES (33, 2);
INSERT INTO `system_users_roles` VALUES (34, 2);
INSERT INTO `system_users_roles` VALUES (35, 2);
INSERT INTO `system_users_roles` VALUES (36, 2);
INSERT INTO `system_users_roles` VALUES (37, 2);
INSERT INTO `system_users_roles` VALUES (38, 2);
INSERT INTO `system_users_roles` VALUES (39, 2);
INSERT INTO `system_users_roles` VALUES (40, 2);
INSERT INTO `system_users_roles` VALUES (41, 2);
INSERT INTO `system_users_roles` VALUES (42, 2);
INSERT INTO `system_users_roles` VALUES (43, 2);
INSERT INTO `system_users_roles` VALUES (44, 2);
INSERT INTO `system_users_roles` VALUES (45, 2);
INSERT INTO `system_users_roles` VALUES (46, 2);
INSERT INTO `system_users_roles` VALUES (47, 2);
INSERT INTO `system_users_roles` VALUES (48, 2);
INSERT INTO `system_users_roles` VALUES (49, 2);
INSERT INTO `system_users_roles` VALUES (50, 2);
INSERT INTO `system_users_roles` VALUES (51, 2);
INSERT INTO `system_users_roles` VALUES (52, 2);
INSERT INTO `system_users_roles` VALUES (53, 2);
INSERT INTO `system_users_roles` VALUES (54, 2);
INSERT INTO `system_users_roles` VALUES (55, 2);
INSERT INTO `system_users_roles` VALUES (56, 2);
INSERT INTO `system_users_roles` VALUES (57, 2);
INSERT INTO `system_users_roles` VALUES (58, 2);
INSERT INTO `system_users_roles` VALUES (59, 2);
INSERT INTO `system_users_roles` VALUES (60, 2);
INSERT INTO `system_users_roles` VALUES (61, 2);
INSERT INTO `system_users_roles` VALUES (62, 2);
INSERT INTO `system_users_roles` VALUES (63, 2);
INSERT INTO `system_users_roles` VALUES (64, 2);
INSERT INTO `system_users_roles` VALUES (65, 2);
INSERT INTO `system_users_roles` VALUES (66, 2);
INSERT INTO `system_users_roles` VALUES (67, 2);
INSERT INTO `system_users_roles` VALUES (68, 2);
INSERT INTO `system_users_roles` VALUES (69, 2);
INSERT INTO `system_users_roles` VALUES (70, 2);
INSERT INTO `system_users_roles` VALUES (71, 2);
INSERT INTO `system_users_roles` VALUES (72, 2);
INSERT INTO `system_users_roles` VALUES (73, 2);
INSERT INTO `system_users_roles` VALUES (74, 2);
INSERT INTO `system_users_roles` VALUES (75, 2);
INSERT INTO `system_users_roles` VALUES (76, 2);
INSERT INTO `system_users_roles` VALUES (77, 2);
INSERT INTO `system_users_roles` VALUES (78, 2);
INSERT INTO `system_users_roles` VALUES (79, 2);
INSERT INTO `system_users_roles` VALUES (80, 2);
INSERT INTO `system_users_roles` VALUES (81, 2);
INSERT INTO `system_users_roles` VALUES (82, 2);
INSERT INTO `system_users_roles` VALUES (83, 2);
INSERT INTO `system_users_roles` VALUES (84, 2);
INSERT INTO `system_users_roles` VALUES (85, 2);
INSERT INTO `system_users_roles` VALUES (86, 2);
INSERT INTO `system_users_roles` VALUES (87, 2);
INSERT INTO `system_users_roles` VALUES (88, 2);
INSERT INTO `system_users_roles` VALUES (89, 2);
INSERT INTO `system_users_roles` VALUES (90, 2);
INSERT INTO `system_users_roles` VALUES (91, 2);
INSERT INTO `system_users_roles` VALUES (92, 2);
INSERT INTO `system_users_roles` VALUES (93, 2);
INSERT INTO `system_users_roles` VALUES (94, 2);
INSERT INTO `system_users_roles` VALUES (95, 2);
INSERT INTO `system_users_roles` VALUES (96, 2);
INSERT INTO `system_users_roles` VALUES (97, 2);
INSERT INTO `system_users_roles` VALUES (98, 2);
INSERT INTO `system_users_roles` VALUES (99, 2);
INSERT INTO `system_users_roles` VALUES (100, 2);
INSERT INTO `system_users_roles` VALUES (101, 2);
INSERT INTO `system_users_roles` VALUES (102, 2);
INSERT INTO `system_users_roles` VALUES (103, 2);

-- ----------------------------
-- Table structure for task_instance_detail
-- ----------------------------
DROP TABLE IF EXISTS `task_instance_detail`;
CREATE TABLE `task_instance_detail`  (
  `id` bigint NOT NULL COMMENT 'id',
  `instance_id` bigint NOT NULL COMMENT '任务实例id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `biz_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务编码',
  `biz_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务名称(步骤)',
  `execute_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '执行参数',
  `execute_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行信息',
  `execute_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行状态(running进行中 success成功 fail失败)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务实例明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_instance_detail
-- ----------------------------

-- ----------------------------
-- Table structure for task_instance_info
-- ----------------------------
DROP TABLE IF EXISTS `task_instance_info`;
CREATE TABLE `task_instance_info`  (
  `id` bigint NOT NULL COMMENT 'id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `context_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言',
  `change_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变更类型',
  `env_alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '环境别名',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态(running进行中 success成功 fail失败)',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '来源',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变更原因',
  `template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '任务模板',
  `job_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'QuartzJob参数',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务实例' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_instance_info
-- ----------------------------

-- ----------------------------
-- Table structure for task_instance_step_detail
-- ----------------------------
DROP TABLE IF EXISTS `task_instance_step_detail`;
CREATE TABLE `task_instance_step_detail`  (
  `id` bigint NOT NULL COMMENT 'id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `instance_detail_id` bigint NOT NULL COMMENT 'task_instance_detail表id',
  `step_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '步骤描述',
  `step_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态(success成功 fail失败)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_instance_step_detail
-- ----------------------------

-- ----------------------------
-- Table structure for task_pipeline_config
-- ----------------------------
DROP TABLE IF EXISTS `task_pipeline_config`;
CREATE TABLE `task_pipeline_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `template_info_id` bigint NOT NULL COMMENT 'task_template_info表id',
  `context_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上下文名称',
  `template_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务流水线配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_pipeline_config
-- ----------------------------
INSERT INTO `task_pipeline_config` VALUES (1, 'admin', '2021-03-20 22:04:13', 4, 'cutejava', '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\",\"click\":\"true\",\"retry\":\"false\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"tags\":\"java\",\"job\":\"build\",\"version\":\"jdk11\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `task_pipeline_config` VALUES (2, 'admin', '2021-03-20 22:04:13', 5, 'cutejava', '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\",\"click\":\"true\",\"retry\":\"false\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"tags\":\"java\",\"job\":\"build\",\"version\":\"jdk11\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `task_pipeline_config` VALUES (3, 'admin', '2021-03-20 22:04:13', 6, 'cutejava', '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\",\"click\":\"true\",\"retry\":\"false\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"tags\":\"java\",\"job\":\"build\",\"version\":\"jdk11\"}},{\"code\":\"node_image_scan\",\"type\":\"service\",\"name\":\"镜像扫描\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"job\":\"image-scan\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"false\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');

-- ----------------------------
-- Table structure for task_template_info
-- ----------------------------
DROP TABLE IF EXISTS `task_template_info`;
CREATE TABLE `task_template_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `available` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据有效性',
  `change_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变更类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线描述',
  `template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线模板内容',
  `env_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '环境别名',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用为语言，资源为类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task_template_info
-- ----------------------------
INSERT INTO `task_template_info` VALUES (4, 'admin', '2021-03-20 22:04:13', 'admin', '2021-03-20 22:04:13', 1, 'AppContainerDeploy', '日常发布流水线', '日常环境流水线(Java)', '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\",\"click\":\"true\",\"retry\":\"false\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"tags\":\"java\",\"job\":\"build\",\"version\":\"jdk11\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]', 'daily', 'java');
INSERT INTO `task_template_info` VALUES (5, 'admin', '2021-03-20 22:04:13', 'admin', '2021-03-20 22:04:13', 1, 'AppContainerDeploy', '预发发布流水线', '预发环境流水线(Java)', '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\",\"click\":\"true\",\"retry\":\"false\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"tags\":\"java\",\"job\":\"build\",\"version\":\"jdk11\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]', 'stage', 'java');
INSERT INTO `task_template_info` VALUES (6, 'admin', '2021-03-20 22:04:13', 'admin', '2021-03-20 22:04:13', 1, 'AppContainerDeploy', '生产发布流水线', '生产环境流水线(Java)', '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\",\"click\":\"true\",\"retry\":\"false\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"tags\":\"java\",\"job\":\"build\",\"version\":\"jdk11\"}},{\"code\":\"node_image_scan\",\"type\":\"service\",\"name\":\"镜像扫描\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"job\":\"image-scan\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"false\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]', 'production', 'java');

SET FOREIGN_KEY_CHECKS = 1;
