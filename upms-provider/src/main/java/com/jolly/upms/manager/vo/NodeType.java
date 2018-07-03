package com.jolly.upms.manager.vo;

/**
 * 树节点的类型
 *
 * @author chenjc
 * @since 2017-06-16
 */
public enum NodeType {

    APPLICATION(1, "应用系统"), MENU(2, "菜单(包含一级菜单、二级菜单、按钮类型的菜单)"), DIMENSION(3, "数据维度"), DIMENSION_VALUE(4, "数据维度值"), MENU_GROUP(5, "菜单组");

    private final int value;
    private final String desc;

    NodeType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
