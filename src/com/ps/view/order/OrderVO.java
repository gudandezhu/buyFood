package com.ps.view.order;

import java.util.ArrayList;
import java.util.List;

import com.ps.view.menu.MenuInfo;

public class OrderVO {
	private int id;
	private String userName;
	private String userPhone;
	private String create_time;
	private String address;
	private double totalMoney;
	private int state;
	private String remark;
	private List<MenuInfo> menus = new ArrayList<>();
	
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public List<MenuInfo> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuInfo> menus) {
		this.menus = menus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "OrderVO [id=" + id + ", userName=" + userName + ", create_time=" + create_time + ", address=" + address
				+ ", totalMoney=" + totalMoney + ", state=" + state + "]";
	}
	
	
	
}
