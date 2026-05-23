-- 用户表
create table ums_admin
(
    id                   bigint not null auto_increment,
    username             varchar(64) comment '用户名',
    password             varchar(64) comment '密码',
    icon                 varchar(500) comment '头像',
    email                varchar(100) comment '邮箱',
    nick_name            varchar(200) comment '昵称',
    note                 varchar(500) comment '备注信息',
    create_time          datetime comment '创建时间',
    login_time           datetime comment '最后登录时间',
    status               int(1) default 1 comment '帐号启用状态：0->禁用；1->启用',
    primary key (id)
);

-- 用户角色表
create table ums_role
(
    id                   bigint not null auto_increment,
    name                 varchar(100) comment '名称',
    description          varchar(500) comment '描述',
    admin_count          int comment '后台用户数量',
    create_time          datetime comment '创建时间',
    status               int(1) default 1 comment '启用状态：0->禁用；1->启用',
    sort                 int default 0,
    update_time          datetime comment '更新时间',
    is_active            int(1) default 1 comment '数据是否删除',
    delete_token         varchar(32) comment '删除标记',

    primary key (id),
    unique key uniq_role (name, is_active)
);


-- 用户与角色关联表，多对多关系，一个用户可以分配多个角色，一个角色也可以分配给多个用户
create table ums_admin_role_relation
(
    id                   bigint not null auto_increment,
    admin_id             bigint comment '用户 ID',
    role_id              bigint comment '角色 ID',
    create_time          datetime comment '创建时间',
    update_time          datetime comment '更新时间',
    is_active            int(1) default 1 comment '数据是否删除',
    delete_token         varchar(32) comment '删除标记',
    primary key (id)
);
