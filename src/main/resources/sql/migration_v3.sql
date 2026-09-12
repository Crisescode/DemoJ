-- =====================================================
-- v3: 菜单图标 + 缓存监控权限（用于已有数据库升级）
-- =====================================================

-- 1. 更新现有模块的图标
UPDATE ums_permission SET icon = 'UserOutlined' WHERE id = 1 AND (icon IS NULL OR icon = '');
UPDATE ums_permission SET icon = 'TeamOutlined' WHERE id = 8 AND (icon IS NULL OR icon = '');
UPDATE ums_permission SET icon = 'SafetyOutlined' WHERE id = 16 AND (icon IS NULL OR icon = '');

-- 2. 添加缓存监控模块权限
INSERT IGNORE INTO ums_permission (id, name, description, url, method, parent_id, type, icon, sort, create_time, status) VALUES
(25, '缓存监控', 'Redis缓存监控', '/cache/**', '', 0, 0, 'DatabaseOutlined', 4, NOW(), 1);

-- 3. 将管理员角色关联缓存监控权限
INSERT IGNORE INTO ums_role_permission_relation (role_id, permission_id, create_time)
SELECT 1, 25, NOW() FROM DUAL
WHERE EXISTS (SELECT 1 FROM ums_role WHERE id = 1);
