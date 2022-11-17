/*
 Navicat MySQL Data Transfer

 Source Server         : wiki@localhost
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : wiki

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 15/11/2022 09:26:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ebook_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '电子书id',
  `parent` bigint(20) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `view_count` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (1, 1, 0, '文档1', 1, 0, 0);
INSERT INTO `doc` VALUES (2, 1, 1, '文档1.1', 1, 0, 0);
INSERT INTO `doc` VALUES (3, 1, 0, '文档2', 2, 0, 0);
INSERT INTO `doc` VALUES (4, 1, 3, '文档2.1', 1, 0, 0);
INSERT INTO `doc` VALUES (5, 1, 3, '文档2.2', 2, 0, 0);
INSERT INTO `doc` VALUES (6, 1, 5, '文档2.2.1', 1, 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
