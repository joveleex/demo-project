INSERT INTO `role` VALUES (1, 'ROLE_dba', '数据库管理员');
INSERT INTO `role` VALUES (2, 'ROLE_admin', '系统管理员');
INSERT INTO `role` VALUES (3, 'ROLE_user', '用户');

INSERT INTO `user` VALUES (1, 'root', '$2a$10$/YeJius7g3v3rl16VGqRYemjAzpoeyqfgxJT0LVQQnS8qzTAyo.hi', 0, 0);
INSERT INTO `user` VALUES (2, 'admin', '$2a$10$/YeJius7g3v3rl16VGqRYemjAzpoeyqfgxJT0LVQQnS8qzTAyo.hi', 0, 0);
INSERT INTO `user` VALUES (3, 'joveleex', '$2a$10$/YeJius7g3v3rl16VGqRYemjAzpoeyqfgxJT0LVQQnS8qzTAyo.hi', 0, 0);

INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 1, 2);
INSERT INTO `user_role` VALUES (3, 2, 2);
INSERT INTO `user_role` VALUES (4, 3, 3);