CREATE TABLE `ebook_snapshot`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ebook_id` bigint(20) NOT NULL COMMENT '电子书id',
  `date` date NOT NULL COMMENT '快照日期\r\n',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '阅读数',
  `vote_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `view_increase` int(11) NOT NULL DEFAULT 0 COMMENT '阅读增长',
  `vote_increase` int(11) NOT NULL DEFAULT 0 COMMENT '点赞增长',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;