package com.ps.view.user;

public class UserInfo {
	private int id;
	private String name;
	private String password;
	private String addess;
	private String phone;
	private float money;

	
	public UserInfo() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddess() {
		return addess;
	}
	public void setAddess(String addess) {
		this.addess = addess;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "UserInfo [name=" + name + ", password=" + password + ", addess=" + addess + ", phone=" + phone
				+ ", money=" + money + "]";
	}
	public UserInfo(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
