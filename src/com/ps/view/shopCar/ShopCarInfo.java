package com.ps.view.shopCar;

public class ShopCarInfo {
	private int id;
	private int menu_id;
	private int user_id;
	private int count;
	private String create_time;
	private String menu_name;
	private double menu_price;
	private double menu_money;
	
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public double getMenu_price() {
		return menu_price;
	}
	public void setMenu_price(double menu_price) {
		this.menu_price = menu_price;
	}
	public double getMenu_money() {
		return menu_money;
	}
	public void setMenu_money(double menu_money) {
		this.menu_money = menu_money;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	@Override
	public String toString() {
		return "ShopCarInfo [id=" + id + ", menu_id=" + menu_id + ", user_id=" + user_id + ", count=" + count
				+ ", create_time=" + create_time + ", menu_name=" + menu_name + ", menu_price=" + menu_price
				+ ", menu_money=" + menu_money + "]";
	}
	
	
	
}
