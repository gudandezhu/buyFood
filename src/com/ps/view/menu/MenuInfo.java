package com.ps.view.menu;

public class MenuInfo {
	private int id;
	private String name;
	private double price;
	private double money;
	private String is_top;
	private String putaway_time;
	private String state;
	private String stateName;
	private int volume;
	private int count;
	private String remark;
	
	
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getIs_top() {
		return is_top;
	}
	public void setIs_top(String is_top) {
		this.is_top = is_top;
	}
	public String getPutaway_time() {
		return putaway_time;
	}
	public void setPutaway_time(String putaway_time) {
		this.putaway_time = putaway_time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	@Override
	public String toString() {
		return "MenuInfo [id=" + id + ", name=" + name + ", price=" + price + ", money=" + money + ", is_top=" + is_top
				+ ", putaway_time=" + putaway_time + ", state=" + state + ", stateName=" + stateName + ", volume="
				+ volume + "]";
	}
	
	
	
	
}
