SET @pm_base = _pm_base(5);

-- 抽奖管理
SET @pm_manage_lottery = @pm_base + 1;

INSERT INTO t_permission (id, name, description)
VALUES (@pm_manage_lottery, '抽奖管理', '抽奖管理的相关权限');