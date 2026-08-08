-- =====================================================
-- DemoJ 初始化脚本：创建 admin 用户 + 管理员角色 + 全量权限
-- =====================================================

-- 1. 先确保权限表已有基础数据（如果没执行过 migration_v2.sql）
INSERT IGNORE INTO ums_permission (id, name, description, url, method, parent_id, type, sort, create_time, status) VALUES
(1,  '用户管理',    '用户管理模块',   '/admin/**',          '',    0, 0, 1, NOW(), 1),
(2,  '查询用户',    '查询用户信息',   '/admin/info',        'POST', 1, 2, 1, NOW(), 1),
(3,  '用户列表',    '用户列表分页',   '/admin/list',        'POST', 1, 2, 2, NOW(), 1),
(4,  '更新用户',    '更新用户信息',   '/admin/update',      'POST', 1, 2, 3, NOW(), 1),
(5,  '删除用户',    '删除用户',       '/admin/delete',      'POST', 1, 2, 4, NOW(), 1),
(6,  '分配角色',    '给用户分配角色', '/admin/role/update', 'POST', 1, 2, 5, NOW(), 1),
(7,  '查看角色',    '查看用户角色',   '/admin/role/*',      'GET',  1, 2, 6, NOW(), 1),
(8,  '角色管理',    '角色管理模块',   '/role/**',           '',    0, 0, 2, NOW(), 1),
(9,  '创建角色',    '创建角色',       '/role/create',       'POST', 8, 2, 1, NOW(), 1),
(10, '查询角色',    '查询角色',       '/role/get',          'POST', 8, 2, 2, NOW(), 1),
(11, '修改角色',    '修改角色',       '/role/update',       'POST', 8, 2, 3, NOW(), 1),
(12, '删除角色',    '删除角色',       '/role/delete',       'POST', 8, 2, 4, NOW(), 1),
(13, '角色列表',    '角色列表分页',   '/role/list',         'POST', 8, 2, 5, NOW(), 1),
(14, '所有角色',    '获取所有角色',   '/role/listAll',      'POST', 8, 2, 6, NOW(), 1),
(15, '批量删除角色','批量删除角色',   '/role/deleteAll',    'POST', 8, 2, 7, NOW(), 1),
(16, '权限管理',    '权限管理模块',   '/permission/**',     '',    0, 0, 3, NOW(), 1),
-- 补全权限管理的子权限
(17, '创建权限',    '创建权限',       '/permission/create',           'POST', 16, 2, 1, NOW(), 1),
(18, '修改权限',    '修改权限',       '/permission/update',           'POST', 16, 2, 2, NOW(), 1),
(19, '删除权限',    '删除权限',       '/permission/delete',           'POST', 16, 2, 3, NOW(), 1),
(20, '查询权限',    '查询权限详情',   '/permission/get',              'POST', 16, 2, 4, NOW(), 1),
(21, '权限列表',    '权限列表分页',   '/permission/list',             'POST', 16, 2, 5, NOW(), 1),
(22, '所有权限',    '获取所有权限',   '/permission/listAll',          'POST', 16, 2, 6, NOW(), 1),
(23, '分配权限',    '给角色分配权限', '/permission/assign',           'POST', 16, 2, 7, NOW(), 1),
(24, '角色权限ID',  '获取角色权限ID', '/permission/rolePermissionIds', 'POST', 16, 2, 8, NOW(), 1);

-- 2. 创建管理员角色
INSERT INTO ums_role (id, name, description, admin_count, create_time, status, sort, is_active)
VALUES (1, '管理员', '超级管理员', 1, NOW(), 1, 0, 1)
ON DUPLICATE KEY UPDATE name = name;

-- 3. 将管理员角色关联所有权限
INSERT IGNORE INTO ums_role_permission_relation (role_id, permission_id, create_time)
SELECT 1, id, NOW() FROM ums_permission;

-- 4. 创建 admin 用户（密码是 BCrypt 加密后的 "123456"，由后端注册接口加密）
--    如果已在页面注册过则跳过这一步，直接看第5步
INSERT IGNORE INTO ums_admin (username, password, nick_name, create_time, status)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAthvFua', '系统管理员', NOW(), 1);

-- 5. 将 admin 用户关联管理员角色
INSERT INTO ums_admin_role_relation (admin_id, role_id, create_time, is_active)
SELECT id, 1, NOW(), 1 FROM ums_admin WHERE username = 'admin';
