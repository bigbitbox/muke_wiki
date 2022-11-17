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

 Date: 17/11/2022 09:02:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `password` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name_unique`(`login_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'test', '测试', 'test');
INSERT INTO `user` VALUES (2, 'test2', 'test2', 'cf2326fc76ff917d3d1e6aa9c16c0ef7');
INSERT INTO `user` VALUES (3, 'test3', 'test3', 'eda83b8d9086af0c44e0f3d4a4f92c47');
INSERT INTO `user` VALUES (4, 'test4', 'test5', '67636cb757e68e5e6fe2174dd041ed98');
INSERT INTO `user` VALUES (5, 'test5', 'test5', 'eefeb7b24ad716f3a10ccb0d802fdbbf');

SET FOREIGN_KEY_CHECKS = 1;
