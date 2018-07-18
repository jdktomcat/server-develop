SET @pm_base = _pm_base(3);

-- 照片管理
SET @pm_manage_photo = @pm_base + 1;

INSERT INTO t_permission (id, name, description)
VALUES (@pm_manage_photo, '照片管理', '照片管理的相关权限');