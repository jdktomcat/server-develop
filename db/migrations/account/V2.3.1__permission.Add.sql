SET @pm_base = _pm_base(2);
-- 添加权限

-- 管理账户
SET @pm_manage_account = @pm_base + 1;


INSERT INTO t_permission (id, name, description) VALUES (@pm_manage_account, '账户管理', '账户管理的相关权限');



