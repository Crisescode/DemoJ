-- ========== v2: 权限、日志、刷新Token ==========

-- 1. 权限资源表
create table ums_permission
(
    id                   bigint not null auto_increment,
    name                 varchar(100) comment '权限名称',
    description          varchar(500) comment '描述',
    url                  varchar(200) comment '请求路径',
    method               varchar(20) comment '请求方式 GET/POST/PUT/DELETE',
    parent_id            bigint default 0 comment '父级ID，0表示顶级',
    type                 int(1) default 1 comment '类型：0->目录 1->菜单 2->按钮',
    icon                 varchar(100) comment '图标',
    sort                 int default 0 comment '排序',
    create_time          datetime comment '创建时间',
    status               int(1) default 1 comment '启用状态：0->禁用 1->启用',
    primary key (id),
    index idx_parent (parent_id)
);

-- 2. 角色-权限关联表
create table ums_role_permission_relation
(
    id                   bigint not null auto_increment,
    role_id              bigint comment '角色ID',
    permission_id        bigint comment '权限ID',
    create_time          datetime comment '创建时间',
    primary key (id),
    index idx_role (role_id),
    index idx_permission (permission_id)
);

-- 3. 操作日志表
create table ums_operation_log
(
    id                   bigint not null auto_increment,
    user_id              bigint comment '用户ID',
    username             varchar(64) comment '用户名',
    operation            varchar(200) comment '操作描述',
    method               varchar(200) comment '请求方法名',
    params               text comment '请求参数',
    url                  varchar(200) comment '请求路径',
    ip                   varchar(64) comment '客户端IP',
    duration             bigint comment '耗时(毫秒)',
    result               text comment '响应结果',
    create_time          datetime comment '创建时间',
    primary key (id),
    index idx_user (user_id),
    index idx_create_time (create_time)
);

-- 4. 初始化基础权限数据
INSERT INTO ums_permission (name, description, url, method, parent_id, type, sort, create_time, status) VALUES
('用户管理', '用户管理模块', '/admin/**', '', 0, 0, 1, NOW(), 1),
('查询用户', '查询用户信息', '/admin/info', 'POST', 1, 2, 1, NOW(), 1),
('用户列表', '用户列表分页', '/admin/list', 'POST', 1, 2, 2, NOW(), 1),
('更新用户', '更新用户信息', '/admin/update', 'POST', 1, 2, 3, NOW(), 1),
('删除用户', '删除用户', '/admin/delete', 'POST', 1, 2, 4, NOW(), 1),
('分配角色', '给用户分配角色', '/admin/role/update', 'POST', 1, 2, 5, NOW(), 1),
('查看角色', '查看用户角色', '/admin/role/*', 'GET', 1, 2, 6, NOW(), 1),
('角色管理', '角色管理模块', '/role/**', '', 0, 0, 2, NOW(), 1),
('创建角色', '创建角色', '/role/create', 'POST', 8, 2, 1, NOW(), 1),
('查询角色', '查询角色', '/role/get', 'POST', 8, 2, 2, NOW(), 1),
('修改角色', '修改角色', '/role/update', 'POST', 8, 2, 3, NOW(), 1),
('删除角色', '删除角色', '/role/delete', 'POST', 8, 2, 4, NOW(), 1),
('角色列表', '角色列表分页', '/role/list', 'POST', 8, 2, 5, NOW(), 1),
('所有角色', '获取所有角色', '/role/listAll', 'POST', 8, 2, 6, NOW(), 1),
('批量删除角色', '批量删除角色', '/role/deleteAll', 'POST', 8, 2, 7, NOW(), 1),
('权限管理', '权限管理模块', '/permission/**', '', 0, 0, 3, NOW(), 1);
