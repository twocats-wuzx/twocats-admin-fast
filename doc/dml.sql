-- 用户
insert into db_twocats_admin_fast.t_user (id, username, password, realName, nickname, avatar, gender, email, mobile, status, create_by, update_by)
values  (1, 'admin', '{bcrypt}$2a$10$3eVYMeSyNlPHTqe.8rwO5eh7ZfjsnSygUeOTg.hB8/5llfKxS3M4e', '两只猫', '两只猫', '', 'MALE', 'wuzx_work@outlook.com', '15985007497', 1, 'SYSTEM', 'SYSTEM');

-- 角色
insert into db_twocats_admin_fast.t_role (id, role_name, role_code, remark, create_by, update_by)
values  (1, '超级管理员', 'super_admin', '超级管理员', 'SYSTEM', 'SYSTEM');

-- 菜单
insert into db_twocats_admin_fast.t_menu (id, pid, title, icon, href, target, type, authority, sort, create_by, update_by)
values  (1, null, '超级管理员', '', '', 'SELF', 'PERMISSION', 'ADMIN', 0, 'SYSTEM', 'SYSTEM');

-- 用户角色关联
insert into db_twocats_admin_fast.t_user_role (id, user_id, role_id, create_by, update_by)
values  (1, 1, 1, 'SYSTEM', 'SYSTEM');

-- 角色菜单关联
insert into db_twocats_admin_fast.t_role_menu (id, role_id, menu_id, create_by, update_by)
values  (1, 1, 1, 'SYSTEM', 'SYSTEM');