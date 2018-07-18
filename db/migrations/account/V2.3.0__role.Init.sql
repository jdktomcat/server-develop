/**
 * 角色表
 */
CREATE TABLE t_role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL COMMENT '角色名',
  description VARCHAR(512) COMMENT '描述',
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now()
);

/**
 * 权限表
 * 所有权限为系统预配置
 * 权限表的id与代码硬关联，禁止变更
 * 允许提供权限名和描述的修改
 *
 * 权限id
 * 规则：[模块id] + [权限编号]
 * 权限编号为3位数字，位数不足补0
 */
CREATE TABLE t_permission (
  id BIGINT PRIMARY KEY,
  name VARCHAR(128) NOT NULL COMMENT '权限名',
  description VARCHAR(512) COMMENT '描述',
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now()
);

/**
 * 添加权限
 */

/**
 * 角色与权限配置表
 */
CREATE TABLE t_role_permission (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now(),
  UNIQUE KEY `uk_role_permission`(`role_id`, `permission_id`)
);

/**
 * 人员角色表
 */
CREATE TABLE t_account_role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  account_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now(),
  UNIQUE KEY `uk_account_role`(`account_id`, `role_id`)
);

