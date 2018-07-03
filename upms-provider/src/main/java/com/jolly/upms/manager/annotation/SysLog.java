package com.jolly.upms.manager.annotation;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    Operate value();

    enum Operate {
        APP_INSERT("001", "新增应用", OperateType.INSERT),
        APP_UPDATE("002", "修改应用", OperateType.UPDATE),
        APP_DELETE("003", "删除应用", OperateType.DELETE),
        DIMENSION_NAME_INSERT("004", "新增数据维度名", OperateType.INSERT),
        DIMENSION_NAME_UPDATE("005", "修改数据维度名", OperateType.UPDATE),
        DIMENSION_NAME_DELETE("006", "删除数据维度名", OperateType.DELETE),
        DIMENSION_VALUE_INSERT("007", "新增数据维度值", OperateType.INSERT),
        DIMENSION_VALUE_UPDATE("008", "修改数据维度值", OperateType.UPDATE),
        DIMENSION_VALUE_DELETE("009", "删除数据维度值", OperateType.DELETE),
        MENU_EDIT("010", "编辑菜单", OperateType.EDIT),
        MENU_GROUP_EDIT("011", "编辑权限组", OperateType.EDIT),
        MENU_GROUP_DELETE("012", "删除权限组", OperateType.DELETE),
        ROLE_EDIT("013", "编辑角色", OperateType.EDIT),
        ROLE_DELETE("014", "删除角色", OperateType.DELETE),
        USER_EDIT("015", "编辑用户", OperateType.EDIT),
        USER_DELETE("016", "删除用户", OperateType.DELETE),
        USER_PERMISSION_UPDATE("017", "修改用户私有权限", OperateType.UPDATE),
        USER_PASSWORD_UPDATE("018", "修改用户密码", OperateType.UPDATE),
        USER_APP_SELECT("019", "查询用户开通的应用", OperateType.SELECT),
        MENU_DELETE("020", "删除菜单", OperateType.DELETE),
        ROLE_APPLICATION_UPDATE("021", "修改角色许可应用", OperateType.UPDATE),
        USER_BATCH_DELETE("022", "批量删除用户", OperateType.DELETE);

        final String code;
        final String name;
        final OperateType operateType;

        Operate(String code, String name, OperateType operateType) {
            this.code = code;
            this.name = name;
            this.operateType = operateType;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public OperateType getOperateType() {
            return operateType;
        }
    }

    enum OperateType {

        INSERT, DELETE, UPDATE, SELECT,
        /**
         * 不区分新增和修改
         */
        EDIT
    }
}
