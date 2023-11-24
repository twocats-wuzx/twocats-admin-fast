-- 菜单表
create table t_menu
(
    id          int auto_increment comment 'ID',
    pid         int          default -1                                            not null comment 'PID',
    title       varchar(20)  default ''                                            not null comment '标题',
    icon        varchar(30)  default ''                                            not null comment '图标',
    href        varchar(50)  default ''                                            not null comment '路由地址',
    target      varchar(30)  default 'SELF'                                        not null comment '链接打开方式 SELF.当前页面 BLANK.新页面',
    type        varchar(10)  default 'MENU'                                        not null comment '类型: MENU.菜单 PERMISSION.权限',
    authority   varchar(50)  default ''                                            not null comment '授权标识',
    sort        int          default 0                                             not null comment '菜单顺序',
    create_by   varchar(120) default ''                                            not null comment '创建人',
    update_by   varchar(120) default ''                                            not null comment '最后更新人',
    create_time datetime     default CURRENT_TIMESTAMP                             not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '最后更新时间',
    constraint t_menu_pk
        primary key (id)
);

-- 角色表
create table t_role
(
    id          int auto_increment comment 'ID',
    role_name   varchar(20)  default ''                                            not null comment '角色名称',
    role_code   varchar(30)  default ''                                            not null comment '角色编码',
    remark      varchar(50)  default ''                                            not null comment '备注',
    create_by   varchar(120) default ''                                            not null comment '创建人',
    update_by   varchar(120) default ''                                            not null comment '最后更新人',
    create_time datetime     default CURRENT_TIMESTAMP                             not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '最后更新时间',
    constraint t_role_pk
        primary key (id)
);

-- 用户表
create table t_user
(
    id          int auto_increment comment 'ID',
    username    varchar(50)  default ''                                            not null comment '用户名',
    password    varchar(200) default ''                                            not null comment '密码',
    realName    varchar(30)  default ''                                            not null comment '真实姓名',
    nickname    varchar(30)  default ''                                            not null comment '昵称',
    avatar      varchar(200) default ''                                            not null comment '头像',
    gender      varchar(10)  default ''                                            not null comment '性别 MALE.男 FEMALE.女',
    email       varchar(50)  default ''                                            not null comment '邮箱地址',
    mobile      varchar(20)  default ''                                            not null comment '手机号码',
    status      tinyint(1)   default 1                                             not null comment '状态 0.禁用 1.启用',
    create_by   varchar(120) default ''                                            not null comment '创建人',
    update_by   varchar(120) default ''                                            not null comment '最后更新人',
    create_time datetime     default CURRENT_TIMESTAMP                             not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '最后更新时间',
    constraint t_user_pk
        primary key (id)
);
create unique index t_username_unique on t_user (username);

-- 用户角色关联表
create table t_user_role
(
    id          int auto_increment comment 'ID',
    user_id     int                                                                not null comment '用户ID',
    role_id     int                                                                not null comment '角色ID',
    create_by   varchar(120) default ''                                            not null comment '创建人',
    update_by   varchar(120) default ''                                            not null comment '最后更新人',
    create_time datetime     default CURRENT_TIMESTAMP                             not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '最后更新时间',
    constraint t_user_role_pk
        primary key (id)
);
create index t_user_idx on t_user_role (user_id);

-- 角色菜单关联表
create table t_role_menu
(
    id          int auto_increment comment 'ID',
    role_id     int                                                                not null comment '角色ID',
    menu_id     int                                                                not null comment '角色ID',
    create_by   varchar(120) default ''                                            not null comment '创建人',
    update_by   varchar(120) default ''                                            not null comment '最后更新人',
    create_time datetime     default CURRENT_TIMESTAMP                             not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null comment '最后更新时间',
    constraint t_role_menu_pk
        primary key (id)
);
create index t_role_idx on t_role_menu (role_id);

