/* 账户 */
CREATE TABLE t_account (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(128) NOT NULL UNIQUE COMMENT '用户名',
  nickname VARCHAR(128) NOT NULL COMMENT '昵称',
  avatar VARCHAR(256) COMMENT '头像链接',
  state VARCHAR(32) NOT NULL DEFAULT 'enable' COMMENT '用户状态。启用：enable, 禁用：disable',
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now()
);

/* oauth uaa */
CREATE TABLE t_oauth2_uaa (
  id BIGINT PRIMARY KEY COMMENT '=t_account.id',
  open_id VARCHAR(255) UNIQUE COMMENT '第三方openId',
  access_token VARCHAR(255) COMMENT '第三方接口调用凭证',
  expired_time DATETIME COMMENT 'access token失效时间',
  refresh_token VARCHAR(255) COMMENT '用于刷新access token',
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now()
);

/* 密码认证 */
CREATE TABLE t_passwd_auth (
  id BIGINT PRIMARY KEY COMMENT '=t_account.id',
  password VARCHAR(255) COMMENT '密码',
  create_time DATETIME NOT NULL DEFAULT now(),
  modify_time DATETIME NOT NULL DEFAULT now()
);