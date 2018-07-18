-- 添加系统预设的角色
ALTER TABLE t_role
  MODIFY COLUMN id BIGINT;

-- 系统预设角色
SET @role_sys_manager = 1;

-- 系统预设开发者角色
SET @role_developer = 2;

INSERT INTO t_role
(id, name, description)
VALUES (@role_sys_manager, '系统管理员', '预设系统管理员角色');

INSERT INTO t_role
(id, name, description)
VALUES (@role_developer, '开发者', '预设开发者角色');

-- 加入一个预设的可以登录的管理员账户
INSERT INTO t_account (id, username, nickname)
VALUES (2, 'vivo', 'vivo admin');

INSERT  INTO t_account_role(account_id, role_id) VALUES (2, @role_sys_manager);

INSERT INTO t_passwd_auth(id, password) VALUES (2, "$2a$10$dEgMhJLIG6jElqpmBxZedumRXPFJkMyhqVaTY3KMCrta4yPIdpZZa");


ALTER TABLE t_role
  MODIFY COLUMN id BIGINT AUTO_INCREMENT;
