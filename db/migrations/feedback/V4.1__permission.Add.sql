SET @pm_base = _pm_base(4);

-- 反馈管理
SET @pm_manage_feedback = @pm_base + 1;

INSERT INTO t_permission (id, name, description)
VALUES (@pm_manage_feedback, '反馈管理', '反馈管理的相关权限');