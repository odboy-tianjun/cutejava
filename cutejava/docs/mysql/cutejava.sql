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

 Date: 29/07/2025 15:23:06
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_time_table
-- ----------------------------
DROP TABLE IF EXISTS `demo_time_table`;
CREATE TABLE `demo_time_table`
(
    `id`          bigint   NOT NULL COMMENT 'id',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_time_table
-- ----------------------------

-- ----------------------------
-- Table structure for demo_user_time_logic_table
-- ----------------------------
DROP TABLE IF EXISTS `demo_user_time_logic_table`;
CREATE TABLE `demo_user_time_logic_table`
(
    `id`          bigint                                                        NOT NULL COMMENT 'id',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
    `create_time` datetime                                                      NOT NULL COMMENT '创建时间',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改人',
    `update_time` datetime                                                      NOT NULL COMMENT '修改时间',
    `available`   tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据有效性',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_user_time_logic_table
-- ----------------------------

-- ----------------------------
-- Table structure for demo_user_time_table
-- ----------------------------
DROP TABLE IF EXISTS `demo_user_time_table`;
CREATE TABLE `demo_user_time_table`
(
    `id`          bigint                                                        NOT NULL COMMENT 'id',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
    `create_time` datetime                                                      NOT NULL COMMENT '创建时间',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改人',
    `update_time` datetime                                                      NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_user_time_table
-- ----------------------------

-- ----------------------------
-- Table structure for devops_ci_git_repo
-- ----------------------------
DROP TABLE IF EXISTS `devops_ci_git_repo`;
CREATE TABLE `devops_ci_git_repo`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_time`  datetime                                                      NOT NULL COMMENT '创建时间',
    `update_time`  datetime                                                      NOT NULL COMMENT '更新时间',
    `app_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
    `ssh_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ssh地址',
    `http_url`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'http地址',
    `namespace`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '命名空间(组、用户)',
    `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
    `project_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_app`(`app_name` ASC) USING BTREE,
    INDEX          `uk_git`(`ssh_url` ASC, `namespace` ASC, `project_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 825 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'devops git仓库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devops_ci_git_repo
-- ----------------------------
INSERT INTO `devops_ci_git_repo`
VALUES (1, '2021-03-12 00:00:00', '2021-03-12 00:00:00', 'cutejava', 'git@local.gitlab.cn.cn:root/cutejava.git', 'http://local.gitlab.cn/root/cutejava.git',
        'root', 'cutejava', '1');

-- ----------------------------
-- Table structure for devops_kubernetes_cluster_config
-- ----------------------------
DROP TABLE IF EXISTS `devops_kubernetes_cluster_config`;
CREATE TABLE `devops_kubernetes_cluster_config`
(
    `id`                 int NOT NULL AUTO_INCREMENT COMMENT '主键',
    `cluster_code`       varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '集群code',
    `env`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '环境daily、stage、online',
    `enabled`            int NULL DEFAULT 1 COMMENT '启用状态 1 启用 0 未启用',
    `cluster_type`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '集群类型(cpaas\\edge\\自建)',
    `cluster_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '集群名称',
    `cluster_server_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '集群服务地址',
    `cluster_config`     blob NULL COMMENT '集群服务SecretsConfig',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `create_by`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
    `update_by`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
    `create_time`        datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `available`          tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据有效性',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'devops kubernetes集群配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of devops_kubernetes_cluster_config
-- ----------------------------
INSERT INTO `devops_kubernetes_cluster_config`
VALUES (1, 'daily', 'daily', 1, 'alicloud', '阿里云日常环境集群', 'https://192.168.1.105:6443', '', NULL, 'admin', 'admin', '2021-03-12 00:00:00',
        '2021-03-12 00:00:00', 1);
INSERT INTO `devops_kubernetes_cluster_config`
VALUES (2, 'stage', 'stage', 1, 'alicloud', '阿里云预发环境集群', 'https://192.168.1.105:6443', '', NULL, 'admin', 'admin', '2021-03-12 00:00:00',
        '2021-03-12 00:00:00', 1);
INSERT INTO `devops_kubernetes_cluster_config`
VALUES (3, 'online', 'online', 1, 'alicloud', '阿里云生产环境集群', 'https://192.168.1.105:6443', '', NULL, 'admin', 'admin', '2021-03-12 00:00:00',
        '2021-03-12 00:00:00', 1);

-- ----------------------------
-- Table structure for pipeline_instance
-- ----------------------------
DROP TABLE IF EXISTS `pipeline_instance`;
CREATE TABLE `pipeline_instance`
(
    `instance_id`         bigint                                                        NOT NULL COMMENT '流水线实例id',
    `create_time`         datetime                                                      NOT NULL COMMENT '创建时间',
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
    `update_time`         datetime                                                      NOT NULL COMMENT '更新时间',
    `instance_name`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线实例名称',
    `env`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '环境编码',
    `context_name`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上下文名称',
    `context_params`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '上下文参数，这里指QuartzJobDataMap',
    `current_node`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '流水线实例当前节点code',
    `current_node_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '流水线实例当前节点状态',
    `status`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '流水线实例状态',
    `template_id`         bigint                                                        NOT NULL COMMENT '流水线模板id',
    `template_type`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '流水线模板类型',
    `template_content`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线模板',
    PRIMARY KEY (`instance_id`) USING BTREE,
    UNIQUE INDEX `udx_pipeline_instance_id`(`instance_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流水线实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipeline_instance
-- ----------------------------
INSERT INTO `pipeline_instance`
VALUES (1949734313813725184, '2025-07-28 15:30:59', 'System', '2025-07-28 15:30:59', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949735698189889536, '2025-07-28 15:30:59', 'System', '2025-07-28 15:30:59', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_branch', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949736689417097216, '2025-07-28 15:30:59', 'System', '2025-07-28 15:30:59', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949747709548179456, '2025-07-28 15:30:59', 'System', '2025-07-28 15:30:59', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949747726652551168, '2025-07-28 15:30:59', 'System', '2025-07-28 15:30:59', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_branch', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949747780465471488, '2025-07-28 15:30:59', 'System', '2025-07-28 15:30:59', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_master', 'success',
        'success', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949750592566050816, '2025-07-28 16:35:41', 'System', '2025-07-28 16:35:41', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_branch', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949753030152871936, '2025-07-28 16:35:41', 'System', '2025-07-28 16:35:41', '流水线测试', 'daily', 'cuteops', NULL, 'node_build_android', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949753101254713344, '2025-07-28 16:35:41', 'System', '2025-07-28 16:35:41', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_master', 'success',
        'success', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949753315009028096, '2025-07-28 16:46:30', 'System', '2025-07-28 16:46:30', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949753384575754240, '2025-07-28 16:46:30', 'System', '2025-07-28 16:46:30', '流水线测试', 'daily', 'cuteops', NULL, 'node_build_android', 'running',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949753461297963008, '2025-07-28 16:46:30', 'System', '2025-07-28 16:46:30', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_master', 'success',
        'success', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949760102374367232, '2025-07-28 17:13:28', 'System', '2025-07-28 17:13:28', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949760122016292864, '2025-07-28 17:13:28', 'System', '2025-07-28 17:13:28', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949760155839160320, '2025-07-28 17:13:28', 'System', '2025-07-28 17:13:28', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_master', 'success',
        'success', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949761136794591232, '2025-07-28 17:17:34', 'System', '2025-07-28 17:17:34', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_master', 'success',
        'success', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949762706709995520, '2025-07-28 17:23:49', 'System', '2025-07-28 17:23:49', '流水线测试', 'daily', 'cuteops', NULL, 'node_sign_android_apk', 'running',
        'running', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949763094402060288, '2025-07-28 17:23:49', 'System', '2025-07-28 17:23:49', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_branch', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949763512100212736, '2025-07-28 17:27:01', 'System', '2025-07-28 17:27:01', '流水线测试', 'daily', 'cuteops', NULL, 'node_init', 'fail', 'fail', 21,
        'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949764704150851584, '2025-07-28 17:31:45', 'System', '2025-07-28 17:31:45', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_branch', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_instance`
VALUES (1949765134918455296, '2025-07-28 17:33:28', 'System', '2025-07-28 17:33:28', '流水线测试', 'daily', 'cuteops', NULL, 'node_merge_branch', 'fail',
        'fail', 21, 'mobile',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');

-- ----------------------------
-- Table structure for pipeline_template
-- ----------------------------
DROP TABLE IF EXISTS `pipeline_template`;
CREATE TABLE `pipeline_template`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
    `create_time` datetime                                                      NOT NULL COMMENT '创建时间',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '修改人',
    `update_time` datetime                                                      NOT NULL COMMENT '修改时间',
    `available`   tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据有效性',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板类型（应用发布流程等）',
    `env`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '环境编码',
    `language`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言',
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线编码',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线描述',
    `template`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水线模板内容',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流水线模板，类型和环境确定可以使用的范围' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipeline_template
-- ----------------------------
INSERT INTO `pipeline_template`
VALUES (1, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'front', 'daily', 'javascript', 'daily_front_javascript', '前端日常流水线',
        '前端日常流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-front-test\",\"npm\":\"npm\",\"nodeVersion\":\"v14.21.3\",\"mirrors\":\"https://registry.npmmirror.com\"}},{\"code\":\"node_deploy_api\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"部署成功，查看详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (2, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'front', 'stage', 'javascript', 'stage_front_javascript', '前端预发流水线',
        '前端预发流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-front-test\",\"npm\":\"npm\",\"nodeVersion\":\"v14.21.3\",\"mirrors\":\"https://registry.npmmirror.com\"}},{\"code\":\"node_deploy_api\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"部署成功，查看详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (3, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'front', 'online', 'javascript', 'online_front_javascript', '前端生产流水线',
        '前端生产流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-front-test\",\"npm\":\"npm\",\"nodeVersion\":\"v14.21.3\",\"mirrors\":\"https://registry.npmmirror.com\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_api\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"部署成功，查看详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (4, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'daily', 'java', 'daily_backend_java', '日常环境流水线',
        '日常环境流水线(JAVA)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\",\"jdkVersion\":\"jdk11\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (5, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'stage', 'java', 'stage_backend_java', '预发环境流水线',
        '预发环境流水线(JAVA)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\",\"jdkVersion\":\"jdk11\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (6, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'online', 'java', 'online_backend_java', '生产环境流水线',
        '生产环境流水线(JAVA)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\"}},{\"code\":\"node_image_scan\",\"type\":\"service\",\"name\":\"镜像扫描\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-image-scan\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"false\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (9, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'online', 'python', 'online_backend_python', '生产环境流水线',
        '生产环境流水线(Python)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-python\"}},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (10, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'stage', 'python', 'stage_backend_python', '预发环境流水线',
        '预发环境流水线(Python)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-python\"}},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (11, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'daily', 'python', 'daily_backend_python', '日常环境流水线',
        '日常环境流水线(Python)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-python\"}},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (16, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'daily', 'node', 'daily_backend_node', 'Node日常环境流水线',
        'Node日常环境流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_node\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-nodejs-server-test\"}},{\"code\":\"node_deploy_node\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (17, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'stage', 'node', 'stage_backend_node', 'Node预发环境流水线',
        'Node预发环境流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_node\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-nodejs-server-test\"}},{\"code\":\"node_deploy_node\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (18, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'online', 'node', 'online_backend_node', 'Node生产环境流水线',
        'Node生产环境流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_node\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-nodejs-server-test\"}},{\"code\":\"node_deploy_node\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (19, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'android', 'daily_mobile_android', 'Android流水线测试阶段',
        'Android流水线测试阶段',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\",\"jdkVersion\":\"v11\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"UI自动化\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/appUiAutoTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/appUiAutoTest/describe\",\"envType\":\"online\"}}]');
INSERT INTO `pipeline_template`
VALUES (20, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'android', 'daily_mobile_android_no_ui',
        'Android流水线测试阶段(不包含UI测试)', 'Android流水线测试阶段(不包含UI测试)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\",\"jdkVersion\":\"v11\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]}]');
INSERT INTO `pipeline_template`
VALUES (21, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android', 'Android流水线发版阶段',
        'Android流水线发版阶段',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (22, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android_no_firm',
        'Android流水线发版阶段(不加固)', 'Android流水线发版阶段(不加固)',
        '[{\"code\":\"node_release_android\",\"type\":\"service\",\"name\":\"合并到Release分支\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (23, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'ios', 'daily_mobile_ios', 'IOS流水线测试阶段',
        'IOS流水线测试阶段',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_ios\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-ios-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]}]');
INSERT INTO `pipeline_template`
VALUES (24, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'ios', 'online_mobile_ios', 'IOS流水线发版阶段',
        'IOS流水线发版阶段',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_ios\",\"type\":\"service\",\"name\":\"IOS构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-ios-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (25, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'front', 'online', 'javascript', 'online_front_javascript_approve_master',
        '前端生产流水线（合并Master确认）', '前端生产流水线（合并Master确认）',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-front-test\",\"npm\":\"npm\",\"nodeVersion\":\"v14.21.3\",\"mirrors\":\"https://registry.npmmirror.com\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_api\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"部署成功，查看详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (28, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'devopsonline', 'java', 'devopsonline_backend_java',
        '运维生产环境流水线（Java）', '运维生产环境流水线（Java）',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (29, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'devopstest', 'java', 'devopstest_backend_java',
        '运维测试环境流水线（Java）', '运维测试环境流水线（Java）',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (30, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'front', 'online', 'javascript', 'online_front_javascript_qa',
        '前端生产流水线-发布准入', '前端生产流水线-发布准入',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-front-test\",\"npm\":\"npm\",\"nodeVersion\":\"v14.21.3\",\"mirrors\":\"https://registry.npmmirror.com\"}},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"发布准入\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/releaseAdmissionExecute\",\"describe\":\"https://cutejava-api.odboy.cn/api/releaseAdmissionDescribe\",\"envType\":\"online\"}},{\"code\":\"node_deploy_api\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"部署成功，查看详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (31, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alitest', 'java', 'alitest_backend_java', '阿里云测试环境流水线',
        '阿里云测试环境流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\",\"jdkVersion\":\"jdk8\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (32, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alionline', 'java', 'alionline_backend_java', '阿里云生产环境流水线',
        '阿里云生产环境流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\",\"jdkVersion\":\"jdk8\"}},{\"code\":\"node_image_scan\",\"type\":\"service\",\"name\":\"镜像扫描\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-image-scan\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (33, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'android', 'daily_mobile_android',
        'Android流水线ECS部署测试阶段', 'Android流水线ECS部署测试阶段',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\",\"jdkVersion\":\"v11\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"UI自动化\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/appAutoUiTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/appAutoUiTest/describe\",\"envType\":\"online\"}},{\"code\":\"node_approve_test_android\",\"type\":\"service\",\"name\":\"测试确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"测试通过\",\"code\":\"agree\",\"parameters\":{}}]}]');
INSERT INTO `pipeline_template`
VALUES (34, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android',
        'Android流水线ECS部署发版阶段', 'Android流水线ECS部署发版阶段',
        '[{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_apk_sign_android\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (38, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android_no_test',
        'Android流水线去除测试部署生产阶段', 'Android流水线去除测试部署发版阶段',
        '[{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"name\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_apk_sign_android\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (42, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alistage', 'java', 'alistage_backend_java', '阿里云预发环境流水线',
        '阿里云预发环境流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_java\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-backend\",\"jdkVersion\":\"jdk8\"}},{\"code\":\"node_deploy_java\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (48, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alitest', 'cpp', 'alitest_backend_cpp', 'C++流水线（自定义构建）',
        'C++流水线（自定义构建）',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_cplus\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/visualplatform/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/visualplatform/describe\",\"envType\":\"daily\"}},{\"code\":\"node_approve_cplus\",\"type\":\"service\",\"name\":\"审批\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/visualplatform/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/visualplatform/describe\",\"envType\":\"daily\"}},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (49, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alitest', 'python', 'alitest_backend_python',
        '阿里云测试环境Python流水线', '阿里云测试环境Python流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"python-pipeline\"}},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (50, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alistage', 'python', 'alistage_backend_python',
        '阿里云预发环境Python流水线', '阿里云预发环境Python流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"python-pipeline\"}},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (51, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alionline', 'python', 'alionline_backend_python',
        '阿里云生产环境Python流水线', '阿里云生产环境Python流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"python-pipeline\"}},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (52, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alitest', 'node', 'alitest_backend_node', '阿里云测试环境Node流水线',
        '阿里云测试环境Node流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_node\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-nodejs-server-test\"}},{\"code\":\"node_deploy_node\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (53, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alistage', 'node', 'alistage_backend_node',
        '阿里云预发环境Node流水线', '阿里云预发环境Node流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_node\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-nodejs-server-test\"}},{\"code\":\"node_deploy_node\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]}]');
INSERT INTO `pipeline_template`
VALUES (54, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alionline', 'node', 'alionline_backend_node',
        '阿里云生产环境Node流水线', '阿里云生产环境Node流水线',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_node\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-nodejs-server-test\"}},{\"code\":\"node_deploy_node\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"name\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (55, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alionline', 'java', 'alionline_backend_java_test',
        '阿里云生产环境流水线（Beta）', '生产环境流水线（test）',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (60, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'android', 'daily_mobile_android_flutter',
        'Android流水线测试阶段(Flutter版本检测)', 'Android流水线测试阶段(Flutter版本检测)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\",\"jdkVersion\":\"v11\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"UI自动化\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/appAutoUiTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/appAutoUiTest/describe\",\"envType\":\"online\"}}]');
INSERT INTO `pipeline_template`
VALUES (61, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'android', 'daily_mobile_android_no_ui_flutter',
        'Android流水线测试阶段(不包含UI测试&Flutter)', 'Android流水线测试阶段(不包含UI测试&Flutter)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\",\"jdkVersion\":\"v11\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]}]');
INSERT INTO `pipeline_template`
VALUES (62, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android_flutter',
        'Android流水线发版阶段(Flutter版本)', 'Android流水线发版阶段(Flutter版本)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APKAndroid APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"专项测试节点\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/specialTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/specialTest/describe\",\"envType\":\"online\"}},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (63, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android_no_firm_flutter',
        'Android流水线发版阶段(不加固&Flutter)', 'Android流水线发版阶段(不加固&Flutter)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"专项测试节点\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/specialTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/specialTest/describe\",\"envType\":\"online\"}},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (64, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'android', 'daily_mobile_android_ecs_flutter',
        'Android流水线ECS部署测试阶段(Flutter版本)', 'Android流水线ECS部署测试阶段(Flutter版本)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\",\"jdkVersion\":\"v11\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"UI自动化\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/appAutoUiTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/appAutoUiTest/describe\",\"envType\":\"online\"}},{\"code\":\"node_approve_test_android\",\"type\":\"service\",\"name\":\"测试确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"测试通过\",\"code\":\"agree\",\"parameters\":{}}]}]');
INSERT INTO `pipeline_template`
VALUES (65, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android_ecs_flutter',
        'Android流水线ECS部署发版阶段(Flutter版本)', 'Android流水线ECS部署发版阶段(Flutter版本)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APKAndroid APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_http_get_plugin\",\"type\":\"service\",\"name\":\"专项测试节点\",\"click\":\"false\",\"retry\":\"true\",\"parameters\":{\"execute\":\"https://cutejava-api.odboy.cn/api/specialTest/execute\",\"describe\":\"https://cutejava-api.odboy.cn/api/specialTest/describe\",\"envType\":\"online\"}},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (66, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'android', 'online_mobile_android_no_test_flutter',
        'Android流水线去除测试部署生产阶段(Flutter版本)', 'Android流水线去除测试部署生产阶段(Flutter版本)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_android\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"pipeline-android-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_upload_android_apk\",\"type\":\"service\",\"name\":\"Android APK上传\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_reinforce_android_apk\",\"type\":\"service\",\"name\":\"Android APKAndroid APK加固\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_sign_android_apk\",\"type\":\"service\",\"name\":\"签名\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (67, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'daily', 'ios', 'daily_mobile_ios_flutter',
        'IOS流水线测试阶段(Flutter版本)', 'IOS流水线测试阶段(Flutter版本)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_ios\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"ios-pipeline-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]}]');
INSERT INTO `pipeline_template`
VALUES (68, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'mobile', 'online', 'ios', 'online_mobile_ios_flutter',
        'IOS流水线发版阶段(Flutter版本)', 'IOS流水线发版阶段(Flutter版本)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_check_flutter_version\",\"type\":\"service\",\"name\":\"检查Flutter版本\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"showFlutterMsg\",\"parameters\":{},\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_build_ios\",\"type\":\"service\",\"name\":\"IOS构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"ios-pipeline-test\"},\"buttons\":[{\"type\":\"qrCodeDialog\",\"title\":\"查看APK二维码\",\"code\":\"openQrCode\",\"parameters\":{}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');
INSERT INTO `pipeline_template`
VALUES (69, 'admin', '2020-02-28 01:35:01', 'admin', '2025-07-19 21:17:12', 1, 'backend', 'alionline', 'python', 'alionline_backend_python_nostc',
        '阿里云生产环境Python流水线(No STC)', '阿里云生产环境Python流水线(No STC)',
        '[{\"code\":\"node_init\",\"type\":\"service\",\"name\":\"初始化\"},{\"code\":\"node_merge_branch\",\"type\":\"service\",\"name\":\"合并代码\",\"click\":\"true\",\"retry\":\"true\"},{\"code\":\"node_build_python\",\"type\":\"service\",\"name\":\"构建\",\"click\":\"true\",\"retry\":\"true\",\"detailType\":\"gitlab\",\"parameters\":{\"pipeline\":\"python-pipeline\"}},{\"code\":\"node_approve_deploy\",\"type\":\"service\",\"name\":\"部署审批\",\"click\":\"false\",\"retry\":\"true\",\"buttons\":[{\"type\":\"execute\",\"title\":\"同意\",\"code\":\"agree\",\"parameters\":{}},{\"type\":\"execute\",\"title\":\"拒绝\",\"code\":\"refuse\",\"parameters\":{}}]},{\"code\":\"node_deploy_python\",\"type\":\"service\",\"name\":\"部署\",\"click\":\"true\",\"retry\":\"true\",\"buttons\":[{\"type\":\"link\",\"title\":\"查看部署详情\",\"code\":\"success\",\"parameters\":{\"isBlank\":\"true\"}}]},{\"code\":\"node_merge_confirm\",\"type\":\"service\",\"name\":\"合并确认\",\"click\":\"false\",\"retry\":\"false\",\"buttons\":[{\"type\":\"execute\",\"title\":\"通过\",\"code\":\"agree\",\"parameters\":{}}]},{\"code\":\"node_merge_master\",\"type\":\"service\",\"name\":\"合并回Master\",\"click\":\"false\",\"retry\":\"true\"}]');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`
(
    `sched_name`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_name`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `blob_data`     blob NULL,
    PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
    INDEX           `sched_name`(`sched_name` ASC, `trigger_name` ASC, `trigger_group` ASC) USING BTREE,
    CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`
(
    `sched_name`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `calendar`      blob                                                          NOT NULL,
    PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`
(
    `sched_name`      varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_name`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_group`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `cron_expression` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `time_zone_id`    varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
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
CREATE TABLE `qrtz_fired_triggers`
(
    `sched_name`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `entry_id`          varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `trigger_name`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_group`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `instance_name`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `fired_time`        bigint                                                        NOT NULL,
    `sched_time`        bigint                                                        NOT NULL,
    `priority`          int                                                           NOT NULL,
    `state`             varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `job_name`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `job_group`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `is_nonconcurrent`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE,
    INDEX               `idx_qrtz_ft_trig_inst_name`(`sched_name` ASC, `instance_name` ASC) USING BTREE,
    INDEX               `idx_qrtz_ft_inst_job_req_rcvry`(`sched_name` ASC, `instance_name` ASC, `requests_recovery` ASC) USING BTREE,
    INDEX               `idx_qrtz_ft_j_g`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
    INDEX               `idx_qrtz_ft_jg`(`sched_name` ASC, `job_group` ASC) USING BTREE,
    INDEX               `idx_qrtz_ft_t_g`(`sched_name` ASC, `trigger_name` ASC, `trigger_group` ASC) USING BTREE,
    INDEX               `idx_qrtz_ft_tg`(`sched_name` ASC, `trigger_group` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`
(
    `sched_name`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `job_name`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `job_group`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `description`       varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `job_class_name`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `is_durable`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `is_nonconcurrent`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `is_update_data`    varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `job_data`          blob NULL,
    PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE,
    INDEX               `idx_qrtz_j_req_recovery`(`sched_name` ASC, `requests_recovery` ASC) USING BTREE,
    INDEX               `idx_qrtz_j_grp`(`sched_name` ASC, `job_group` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`
(
    `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `lock_name`  varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks`
VALUES ('ClusteredScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks`
VALUES ('ClusteredScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`
(
    `sched_name`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
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
CREATE TABLE `qrtz_scheduler_state`
(
    `sched_name`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `instance_name`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `last_checkin_time` bigint                                                        NOT NULL,
    `checkin_interval`  bigint                                                        NOT NULL,
    PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state`
VALUES ('ClusteredScheduler', 'DESKTOP-FM44BLS1742558233429', 1742558255052, 20000);

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`
(
    `sched_name`      varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_name`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_group`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `repeat_count`    bigint                                                        NOT NULL,
    `repeat_interval` bigint                                                        NOT NULL,
    `times_triggered` bigint                                                        NOT NULL,
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
CREATE TABLE `qrtz_simprop_triggers`
(
    `sched_name`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_name`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `str_prop_1`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `str_prop_2`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `str_prop_3`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `int_prop_1`    int NULL DEFAULT NULL,
    `int_prop_2`    int NULL DEFAULT NULL,
    `long_prop_1`   bigint NULL DEFAULT NULL,
    `long_prop_2`   bigint NULL DEFAULT NULL,
    `dec_prop_1`    decimal(13, 4) NULL DEFAULT NULL,
    `dec_prop_2`    decimal(13, 4) NULL DEFAULT NULL,
    `bool_prop_1`   varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `bool_prop_2`   varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
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
CREATE TABLE `qrtz_triggers`
(
    `sched_name`     varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_name`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `trigger_group`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `job_name`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `job_group`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `description`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `next_fire_time` bigint NULL DEFAULT NULL,
    `prev_fire_time` bigint NULL DEFAULT NULL,
    `priority`       int NULL DEFAULT NULL,
    `trigger_state`  varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `trigger_type`   varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `start_time`     bigint                                                        NOT NULL,
    `end_time`       bigint NULL DEFAULT NULL,
    `calendar_name`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `misfire_instr`  smallint NULL DEFAULT NULL,
    `job_data`       blob NULL,
    PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
    INDEX            `idx_qrtz_t_j`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_jg`(`sched_name` ASC, `job_group` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_c`(`sched_name` ASC, `calendar_name` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_g`(`sched_name` ASC, `trigger_group` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_state`(`sched_name` ASC, `trigger_state` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_n_state`(`sched_name` ASC, `trigger_name` ASC, `trigger_group` ASC, `trigger_state` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_n_g_state`(`sched_name` ASC, `trigger_group` ASC, `trigger_state` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_next_fire_time`(`sched_name` ASC, `next_fire_time` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_nft_st`(`sched_name` ASC, `trigger_state` ASC, `next_fire_time` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_nft_misfire`(`sched_name` ASC, `misfire_instr` ASC, `next_fire_time` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_nft_st_misfire`(`sched_name` ASC, `misfire_instr` ASC, `next_fire_time` ASC, `trigger_state` ASC) USING BTREE,
    INDEX            `idx_qrtz_t_nft_st_misfire_grp`(`sched_name` ASC, `misfire_instr` ASC, `next_fire_time` ASC, `trigger_group` ASC, `trigger_state` ASC) USING BTREE,
    CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`
(
    `dept_id`     bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`         bigint NULL DEFAULT NULL COMMENT '上级部门',
    `sub_count`   int NULL DEFAULT 0 COMMENT '子部门数目',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `dept_sort`   int NULL DEFAULT 999 COMMENT '排序',
    `enabled`     bit(1)                                                        NOT NULL COMMENT '状态',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`dept_id`) USING BTREE,
    INDEX         `idx_pid`(`pid` ASC) USING BTREE,
    INDEX         `idx_enabled`(`enabled` ASC) USING BTREE,
    INDEX         `idx_sys_dept_dept_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dept
-- ----------------------------
INSERT INTO `system_dept`
VALUES (2, 7, 1, '研发部', 3, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept`
VALUES (5, 7, 0, '运维部', 4, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept`
VALUES (6, 8, 0, '测试部', 6, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept`
VALUES (7, NULL, 2, '华南分部', 0, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept`
VALUES (8, NULL, 2, '华北分部', 1, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept`
VALUES (15, 8, 0, 'UI部门', 7, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dept`
VALUES (17, 2, 0, '研发一组', 999, b'1', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict
-- ----------------------------
INSERT INTO `system_dict`
VALUES (1, 'user_status', '用户状态', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict`
VALUES (4, 'dept_status', '部门状态', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict`
VALUES (5, 'job_status', '岗位状态', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_detail`;
CREATE TABLE `system_dict_detail`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dict_id`     bigint NULL DEFAULT NULL COMMENT '字典id',
    `label`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
    `value`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
    `dict_sort`   int NULL DEFAULT NULL COMMENT '排序',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX         `idx_dict_id`(`dict_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_dict_detail
-- ----------------------------
INSERT INTO `system_dict_detail`
VALUES (1, 1, '激活', 'true', 1, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail`
VALUES (2, 1, '禁用', 'false', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail`
VALUES (3, 4, '启用', 'true', 1, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail`
VALUES (4, 4, '停用', 'false', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail`
VALUES (5, 5, '启用', 'true', 1, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_dict_detail`
VALUES (6, 5, '停用', 'false', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_email_config
-- ----------------------------
DROP TABLE IF EXISTS `system_email_config`;
CREATE TABLE `system_email_config`
(
    `config_id` bigint NOT NULL COMMENT 'ID',
    `from_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收件人',
    `host`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮件服务器SMTP地址',
    `password`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
    `port`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '端口',
    `user`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发件者用户名',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '邮箱配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_email_config
-- ----------------------------

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
    `enabled`     bit(1)                                                        NOT NULL COMMENT '岗位状态',
    `job_sort`    int NULL DEFAULT NULL COMMENT '排序',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
    INDEX         `idx_enabled`(`enabled` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_job
-- ----------------------------
INSERT INTO `system_job`
VALUES (8, '人事专员', b'1', 3, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_job`
VALUES (10, '产品经理', b'1', 4, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_job`
VALUES (11, '全栈开发', b'1', 2, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_job`
VALUES (12, '软件测试', b'1', 5, 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_local_storage
-- ----------------------------
DROP TABLE IF EXISTS `system_local_storage`;
CREATE TABLE `system_local_storage`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `real_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件真实的名称',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
    `suffix`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后缀',
    `path`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
    `size`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '大小',
    `date_group`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日期分组',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '本地存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_local_storage
-- ----------------------------

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`
(
    `menu_id`     bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`         bigint NULL DEFAULT NULL COMMENT '上级菜单ID',
    `sub_count`   int NULL DEFAULT 0 COMMENT '子菜单数目',
    `type`        int NULL DEFAULT NULL COMMENT '菜单类型',
    `title`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单标题',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名称',
    `component`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件',
    `menu_sort`   int NULL DEFAULT NULL COMMENT '排序',
    `icon`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
    `path`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接地址',
    `i_frame`     bit(1) NULL DEFAULT NULL COMMENT '是否外链',
    `cache`       bit(1) NULL DEFAULT b'0' COMMENT '缓存',
    `hidden`      bit(1) NULL DEFAULT b'0' COMMENT '隐藏',
    `permission`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`menu_id`) USING BTREE,
    UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
    UNIQUE INDEX `uniq_title`(`title` ASC) USING BTREE,
    INDEX         `idx_pid`(`pid` ASC) USING BTREE,
    INDEX         `idx_sys_menu_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 133 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu`
VALUES (1, NULL, 9, 0, '系统管理', NULL, NULL, 1, 'system', 'system', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 2, 'peoples', 'user', b'0', b'0', b'0', 'user:list', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 3, 'role', 'role', b'0', b'0', b'0', 'roles:list', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 5, 'menu', 'menu', b'0', b'0', b'0', 'menu:list', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (6, 1, 3, 0, '系统监控', NULL, NULL, 11, 'monitor', 'monitor', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (9, 6, 0, 1, 'SQL监控', 'DruidSqlConsole', 'system/druidSql/index', 18, 'sqlMonitor', 'druid-console', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (10, NULL, 16, 0, '组件管理', NULL, NULL, 50, 'zujian', 'components', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 51, 'icon', 'icon', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'system/email/index', 35, 'email', 'email', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'system/storage/index', 34, 'qiniu', 'storage', b'0', b'0', b'0', 'storage:list', 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (21, NULL, 2, 0, '演示:多级菜单', NULL, '', 900, 'menu', 'nested', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 999, 'menu', 'menu1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 999, 'menu', 'menu2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 999, 'menu', 'menu1-1', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 999, 'menu', 'menu1-2', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (28, 1, 3, 1, '任务调度', 'QuartzJob', 'system/quartzJob/index', 9, 'timing', 'quartz-job', b'0', b'0', b'0', 'quartzJob:list', 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (34, 10, 0, 1, '演示:Yaml编辑器', 'CuteYamlEditorDemo', 'componentsDemo/CuteYamlEditorDemo', 999, 'menu', 'cuteYamlEditorDemo', b'0', b'0', b'0', NULL,
        'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 6, 'dept', 'dept', b'0', b'0', b'0', 'dept:list', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (36, 1, 2, 0, '系统工具', NULL, '', 10, 'sys-tools', 'sys-tools', b'0', b'0', b'0', NULL, 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 7, 'Steve-Jobs', 'job', b'0', b'0', b'0', 'job:list', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 8, 'dictionary', 'dict', b'0', b'0', b'0', 'dict:list', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'system/online/index', 10, 'Steve-Jobs', 'online', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (44, 2, 0, 2, '用户新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'user:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (45, 2, 0, 2, '用户编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'user:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (46, 2, 0, 2, '用户删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'user:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (48, 3, 0, 2, '角色创建', NULL, '', 2, '', '', b'0', b'0', b'0', 'roles:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (49, 3, 0, 2, '角色修改', NULL, '', 3, '', '', b'0', b'0', b'0', 'roles:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (50, 3, 0, 2, '角色删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'roles:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (52, 5, 0, 2, '菜单新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'menu:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'menu:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (54, 5, 0, 2, '菜单删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'menu:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (56, 35, 0, 2, '部门新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dept:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (57, 35, 0, 2, '部门编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dept:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (58, 35, 0, 2, '部门删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dept:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (60, 37, 0, 2, '岗位新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'job:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'job:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (62, 37, 0, 2, '岗位删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'job:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (64, 39, 0, 2, '字典新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'dict:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (65, 39, 0, 2, '字典编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'dict:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (66, 39, 0, 2, '字典删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'dict:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (73, 28, 0, 2, '任务新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'quartzJob:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (74, 28, 0, 2, '任务编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'quartzJob:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (75, 28, 0, 2, '任务删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'quartzJob:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (77, 18, 0, 2, '上传文件', NULL, '', 2, '', '', b'0', b'0', b'0', 'storage:add', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (78, 18, 0, 2, '文件编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'storage:edit', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (79, 18, 0, 2, '文件删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'storage:del', 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'system/server/index', 14, 'codeConsole', 'server', b'0', b'0', b'0', 'monitor:list', 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/base/Echarts', 50, 'chart', 'echarts', b'0', b'1', b'0', '', 'admin', 'admin', '2025-07-16 21:12:41',
        '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (117, 10, 0, 1, '演示:一键复制组件', 'CuteOneKeyCopy', 'componentsDemo/CuteOneKeyCopyDemo', 999, 'menu', 'cuteOneKeyCopyDemo', b'0', b'0', b'0', NULL,
        'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (118, 10, 0, 1, '演示:Java代码编辑器', 'CuteJavaEditorDemo', 'componentsDemo/CuteJavaEditorDemo', 999, 'menu', 'cuteJavaEditorDemo', b'0', b'0', b'0',
        NULL, 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (119, 10, 0, 1, '数字滚动', 'countToDemo', 'components/base/CountToDemo', 56, 'menu', 'countToDemo', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (121, 10, 0, 1, '分割面板', 'splitPaneDemo', 'components/base/SplitPaneDemo', 57, 'menu', 'splitPaneDemo', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (122, 10, 0, 1, 'WebSocket', 'webSocketDemo', 'components/base/WebSocketDemo', 58, 'menu', 'webSocketDemo', b'0', b'0', b'0', NULL, 'admin', 'admin',
        '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (125, 10, 0, 1, '演示:业务组件', 'CuteBusinessDemo', 'componentsDemo/CuteBusinessDemo', 999, 'menu', 'cuteBusinessDemo', b'0', b'0', b'0', NULL, 'admin',
        'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (126, 10, 2, 1, '演示:简单表格组件', 'CuteSimpleTableDemo', 'componentsDemo/CuteSimpleTableDemo', 999, 'menu', 'cuteSimpleTableDemo', b'0', b'0', b'0',
        NULL, 'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (127, 10, 2, 1, '演示:拖拽表格组件', 'CuteDragTableDemo', 'componentsDemo/CuteDragTableDemo', 999, 'menu', 'cuteDragTableDemo', b'0', b'0', b'0', NULL,
        'admin', 'admin', '2025-07-16 21:12:41', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (128, 10, 0, 1, '演示:Markdown编辑器', 'CuteMarkdownDemo', 'componentsDemo/CuteMarkdownDemo', 999, 'menu', 'cuteMarkdownDemo', b'0', b'0', b'0', NULL,
        'admin', 'admin', '2025-07-17 10:53:29', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (129, 10, 0, 1, '演示:富文本编辑器', 'CuteRichTextEditorDemo', 'componentsDemo/CuteRichTextEditorDemo', 999, 'menu', 'cuteRichTextEditorDemo', b'0',
        b'0', b'0', NULL, 'admin', 'admin', '2025-07-17 13:22:21', '2025-07-17 20:48:58');
INSERT INTO `system_menu`
VALUES (130, 10, 0, 1, '演示:文件上传', 'CuteFileUploadDemo', 'componentsDemo/CuteFileUploadDemo', 999, 'menu', 'cuteFileUploadDemo', b'0', b'0', b'0', NULL,
        'admin', 'admin', '2025-07-17 21:51:35', '2025-07-17 21:58:07');
INSERT INTO `system_menu`
VALUES (131, 10, 0, 1, '演示:流水线节点', 'CutePipelineNodeDemo', 'componentsDemo/CutePipelineNodeDemo.vue', 999, 'menu', 'cutePipelineNodeDemo', b'0', b'0',
        b'0', NULL, 'admin', 'admin', '2025-07-18 10:57:29', '2025-07-18 10:57:29');
INSERT INTO `system_menu`
VALUES (132, 10, 0, 1, '演示:文件拖拽上传', 'CuteFileDragUploadDemo', 'componentsDemo/CuteFileDragUploadDemo', 999, 'menu', 'cuteFileDragUploadDemo', b'0',
        b'0', b'0', NULL, 'admin', 'admin', '2025-07-18 20:20:20', '2025-07-18 20:20:20');

-- ----------------------------
-- Table structure for system_oss_storage
-- ----------------------------
DROP TABLE IF EXISTS `system_oss_storage`;
CREATE TABLE `system_oss_storage`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_by`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time`  datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间',
    `service_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型，比如minio',
    `endpoint`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务地址',
    `bucket_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '存储桶名称',
    `file_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '完整文件名称',
    `file_size`    bigint NULL DEFAULT NULL COMMENT '文件大小, 单位：字节',
    `file_mime`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
    `file_prefix`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '短文件名',
    `file_suffix`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件后缀',
    `file_md5`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件md5',
    `file_url`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件链接',
    `file_code`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件编码',
    `object_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对象路径',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `udx_filemd5`(`file_md5` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OSS存储' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_oss_storage
-- ----------------------------
INSERT INTO `system_oss_storage`
VALUES (2, 'admin', 'admin', '2025-07-17 21:13:21', '2025-07-17 21:13:21', 'minio', 'http://192.168.100.128:9000', 'cutejava', '7z2500-x64.exe', 1641049, 'exe',
        '7z2500-x64-20250717091319922', 'exe', '9eeca41aa10ef3c99d7db2ea97160e17',
        'http://192.168.100.128:9000/cutejava/20250717/704d5389832a4583a406c97453b03e4a.exe', '704d5389832a4583a406c97453b03e4a',
        '20250717/704d5389832a4583a406c97453b03e4a.exe');

-- ----------------------------
-- Table structure for system_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `system_quartz_job`;
CREATE TABLE `system_quartz_job`
(
    `id`                  bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bean_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Spring Bean名称',
    `cron_expression`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
    `is_pause`            bit(1) NULL DEFAULT NULL COMMENT '状态：1暂停、0启用',
    `job_name`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
    `method_name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名称',
    `params`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
    `description`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `person_in_charge`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
    `email`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报警邮箱',
    `sub_task`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子任务ID',
    `pause_after_failure` bit(1) NULL DEFAULT NULL COMMENT '任务失败后是否暂停',
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX                 `idx_is_pause`(`is_pause` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_quartz_job
-- ----------------------------
INSERT INTO `system_quartz_job`
VALUES (2, 'testTask', '0/5 * * * * ?', b'1', '测试1', 'run1', 'test', '带参测试，多参使用json', '测试', NULL, NULL, NULL, 'admin', 'admin',
        '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_quartz_job`
VALUES (3, 'testTask', '0/5 * * * * ?', b'1', '测试', 'run', '', '不带参测试', 'Zheng Jie', '', '6', b'1', 'admin', 'admin', '2025-01-05 00:00:00',
        '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_quartz_log
-- ----------------------------
DROP TABLE IF EXISTS `system_quartz_log`;
CREATE TABLE `system_quartz_log`
(
    `id`               bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bean_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Bean名称',
    `cron_expression`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'cron 表达式',
    `is_success`       bit(1) NULL DEFAULT NULL COMMENT '是否执行成功',
    `job_name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
    `method_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名称',
    `params`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数',
    `time`             bigint NULL DEFAULT NULL COMMENT '执行耗时',
    `exception_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常详情',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_quartz_log
-- ----------------------------

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`
(
    `role_id`     bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `level`       int NULL DEFAULT NULL COMMENT '角色级别',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
    `data_scope`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据权限',
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`role_id`) USING BTREE,
    UNIQUE INDEX `uniq_name`(`name` ASC) USING BTREE,
    INDEX         `idx_level`(`level` ASC) USING BTREE,
    INDEX         `idx_sys_role_level`(`level` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role`
VALUES (1, '超级管理员', 1, '6666', '全部', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_role`
VALUES (2, '普通用户', 2, '-', '本级', 'admin', 'admin', '2025-01-05 00:00:00', '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_roles_depts
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_depts`;
CREATE TABLE `system_roles_depts`
(
    `role_id` bigint NOT NULL COMMENT '角色ID',
    `dept_id` bigint NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`role_id`, `dept_id`) USING BTREE,
    INDEX     `idx_role_id`(`role_id` ASC) USING BTREE,
    INDEX     `idx_dept_id`(`dept_id` ASC) USING BTREE,
    INDEX     `idx_sys_roles_depts_role_dept`(`role_id` ASC, `dept_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色部门关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_roles_depts
-- ----------------------------

-- ----------------------------
-- Table structure for system_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `system_roles_menus`;
CREATE TABLE `system_roles_menus`
(
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
    INDEX     `idx_menu_id`(`menu_id` ASC) USING BTREE,
    INDEX     `idx_role_id`(`role_id` ASC) USING BTREE,
    INDEX     `idx_sys_roles_menus_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_roles_menus
-- ----------------------------
INSERT INTO `system_roles_menus`
VALUES (1, 1);
INSERT INTO `system_roles_menus`
VALUES (2, 1);
INSERT INTO `system_roles_menus`
VALUES (3, 1);
INSERT INTO `system_roles_menus`
VALUES (5, 1);
INSERT INTO `system_roles_menus`
VALUES (6, 1);
INSERT INTO `system_roles_menus`
VALUES (9, 1);
INSERT INTO `system_roles_menus`
VALUES (10, 1);
INSERT INTO `system_roles_menus`
VALUES (10, 2);
INSERT INTO `system_roles_menus`
VALUES (11, 1);
INSERT INTO `system_roles_menus`
VALUES (11, 2);
INSERT INTO `system_roles_menus`
VALUES (14, 1);
INSERT INTO `system_roles_menus`
VALUES (18, 1);
INSERT INTO `system_roles_menus`
VALUES (18, 2);
INSERT INTO `system_roles_menus`
VALUES (21, 1);
INSERT INTO `system_roles_menus`
VALUES (21, 2);
INSERT INTO `system_roles_menus`
VALUES (22, 1);
INSERT INTO `system_roles_menus`
VALUES (22, 2);
INSERT INTO `system_roles_menus`
VALUES (23, 1);
INSERT INTO `system_roles_menus`
VALUES (23, 2);
INSERT INTO `system_roles_menus`
VALUES (24, 1);
INSERT INTO `system_roles_menus`
VALUES (24, 2);
INSERT INTO `system_roles_menus`
VALUES (27, 1);
INSERT INTO `system_roles_menus`
VALUES (27, 2);
INSERT INTO `system_roles_menus`
VALUES (28, 1);
INSERT INTO `system_roles_menus`
VALUES (34, 1);
INSERT INTO `system_roles_menus`
VALUES (34, 2);
INSERT INTO `system_roles_menus`
VALUES (35, 1);
INSERT INTO `system_roles_menus`
VALUES (36, 1);
INSERT INTO `system_roles_menus`
VALUES (36, 2);
INSERT INTO `system_roles_menus`
VALUES (37, 1);
INSERT INTO `system_roles_menus`
VALUES (39, 1);
INSERT INTO `system_roles_menus`
VALUES (41, 1);
INSERT INTO `system_roles_menus`
VALUES (44, 1);
INSERT INTO `system_roles_menus`
VALUES (45, 1);
INSERT INTO `system_roles_menus`
VALUES (46, 1);
INSERT INTO `system_roles_menus`
VALUES (48, 1);
INSERT INTO `system_roles_menus`
VALUES (49, 1);
INSERT INTO `system_roles_menus`
VALUES (50, 1);
INSERT INTO `system_roles_menus`
VALUES (52, 1);
INSERT INTO `system_roles_menus`
VALUES (53, 1);
INSERT INTO `system_roles_menus`
VALUES (54, 1);
INSERT INTO `system_roles_menus`
VALUES (56, 1);
INSERT INTO `system_roles_menus`
VALUES (57, 1);
INSERT INTO `system_roles_menus`
VALUES (58, 1);
INSERT INTO `system_roles_menus`
VALUES (60, 1);
INSERT INTO `system_roles_menus`
VALUES (61, 1);
INSERT INTO `system_roles_menus`
VALUES (62, 1);
INSERT INTO `system_roles_menus`
VALUES (64, 1);
INSERT INTO `system_roles_menus`
VALUES (65, 1);
INSERT INTO `system_roles_menus`
VALUES (66, 1);
INSERT INTO `system_roles_menus`
VALUES (73, 1);
INSERT INTO `system_roles_menus`
VALUES (74, 1);
INSERT INTO `system_roles_menus`
VALUES (75, 1);
INSERT INTO `system_roles_menus`
VALUES (77, 1);
INSERT INTO `system_roles_menus`
VALUES (77, 2);
INSERT INTO `system_roles_menus`
VALUES (78, 1);
INSERT INTO `system_roles_menus`
VALUES (78, 2);
INSERT INTO `system_roles_menus`
VALUES (79, 1);
INSERT INTO `system_roles_menus`
VALUES (79, 2);
INSERT INTO `system_roles_menus`
VALUES (80, 1);
INSERT INTO `system_roles_menus`
VALUES (80, 2);
INSERT INTO `system_roles_menus`
VALUES (83, 1);
INSERT INTO `system_roles_menus`
VALUES (83, 2);
INSERT INTO `system_roles_menus`
VALUES (117, 1);
INSERT INTO `system_roles_menus`
VALUES (117, 2);
INSERT INTO `system_roles_menus`
VALUES (118, 1);
INSERT INTO `system_roles_menus`
VALUES (118, 2);
INSERT INTO `system_roles_menus`
VALUES (119, 1);
INSERT INTO `system_roles_menus`
VALUES (119, 2);
INSERT INTO `system_roles_menus`
VALUES (121, 1);
INSERT INTO `system_roles_menus`
VALUES (121, 2);
INSERT INTO `system_roles_menus`
VALUES (122, 1);
INSERT INTO `system_roles_menus`
VALUES (122, 2);
INSERT INTO `system_roles_menus`
VALUES (125, 1);
INSERT INTO `system_roles_menus`
VALUES (126, 1);
INSERT INTO `system_roles_menus`
VALUES (127, 1);
INSERT INTO `system_roles_menus`
VALUES (128, 1);
INSERT INTO `system_roles_menus`
VALUES (129, 1);
INSERT INTO `system_roles_menus`
VALUES (130, 1);
INSERT INTO `system_roles_menus`
VALUES (131, 1);
INSERT INTO `system_roles_menus`
VALUES (132, 1);

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`
(
    `user_id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dept_id`        bigint NULL DEFAULT NULL COMMENT '部门名称',
    `username`       varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
    `nick_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
    `gender`         varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
    `phone`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
    `email`          varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
    `avatar_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
    `avatar_path`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像真实路径',
    `password`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
    `is_admin`       bit(1) NULL DEFAULT b'0' COMMENT '是否为admin账号',
    `enabled`        bit(1) NULL DEFAULT NULL COMMENT '状态：1启用、0禁用',
    `create_by`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
    `update_by`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新者',
    `pwd_reset_time` datetime NULL DEFAULT NULL COMMENT '修改密码的时间',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建日期',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `uniq_email`(`email` ASC) USING BTREE,
    UNIQUE INDEX `uniq_username`(`username` ASC) USING BTREE,
    INDEX            `idx_dept_id`(`dept_id` ASC) USING BTREE,
    INDEX            `idx_enabled`(`enabled` ASC) USING BTREE,
    INDEX            `uniq_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user`
VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '1943815081@qq.com', '', '', '$2a$10$Egp1/gvFlt7zhlXVfEFw4OfWQCGPw0ClmMcc6FjTnvXNRVf9zdMRa', b'1', b'1',
        'admin', 'admin', '2020-05-03 16:38:31', '2025-01-05 00:00:00', '2025-01-05 00:00:00');
INSERT INTO `system_user`
VALUES (3, 2, '测试', '测试', '男', '18888888881', '188888@qq.com', NULL, NULL, '$2a$10$DpAGWp3CYtYySPqJe.CnL.c/OCR0kkPLLAwAyID0yFHLamtYdYsvy', b'0', b'1',
        'admin', 'admin', NULL, '2025-01-05 00:00:00', '2025-01-05 00:00:00');

-- ----------------------------
-- Table structure for system_users_jobs
-- ----------------------------
DROP TABLE IF EXISTS `system_users_jobs`;
CREATE TABLE `system_users_jobs`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `job_id`  bigint NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `job_id`) USING BTREE,
    INDEX     `idx_user_id`(`user_id` ASC) USING BTREE,
    INDEX     `idx_job_id`(`job_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_users_jobs
-- ----------------------------
INSERT INTO `system_users_jobs`
VALUES (1, 11);
INSERT INTO `system_users_jobs`
VALUES (3, 11);

-- ----------------------------
-- Table structure for system_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `system_users_roles`;
CREATE TABLE `system_users_roles`
(
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `role_id` bigint NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
    INDEX     `idx_user_id`(`user_id` ASC) USING BTREE,
    INDEX     `idx_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_users_roles
-- ----------------------------
INSERT INTO `system_users_roles`
VALUES (1, 1);
INSERT INTO `system_users_roles`
VALUES (3, 2);

SET
FOREIGN_KEY_CHECKS = 1;
