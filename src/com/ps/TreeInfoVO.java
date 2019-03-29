package com.ps;

import java.util.List;

public class TreeInfoVO {
    private int id;
    private String text;
    private String state = "closed";
    private boolean checked=false;
    private String attributes;
    private List<TreeInfoVO> children;
    private String iconCls;
    
    public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public TreeInfoVO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<TreeInfoVO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeInfoVO> children) {
        this.children = children;
    }
}
