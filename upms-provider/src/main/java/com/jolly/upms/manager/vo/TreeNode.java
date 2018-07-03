package com.jolly.upms.manager.vo;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public class TreeNode {
    private Integer id;
    private String text;
    private String state;
    private boolean checked;
    private int nodeType = NodeType.MENU.getValue();//节点类型，默认为菜单
    private String dimensionValueDetail;//数据维度值详情，具体结构为"菜单ID,数据维度属性名,数据维度属性值"
    private List<TreeNode> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getDimensionValueDetail() {
        return dimensionValueDetail;
    }

    public void setDimensionValueDetail(String dimensionValueDetail) {
        this.dimensionValueDetail = dimensionValueDetail;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
