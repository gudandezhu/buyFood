package com.ps.view.order;

import java.sql.Timestamp;
import java.util.List;

import com.ps.view.menu.MenuInfo;
import com.ps.view.user.UserInfo;

public class OrderInfo {
	private int id;
	private int user_id;
	private String state;
	private String stateName="";
	private String create_time;
	private String order_receiving_time;
	private String production_time;
	private String done_time;
	private String cancel_reason;
	
	//一个订单下面的餐品信息
	private List<MenuInfo> menus;
	
	//此订单的用户信息
	private UserInfo userInfo;

	
	
	
	
	
	
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getState() {
		return state;
	}

//	1:待下单，2：待生产，3：待收货，4：完成，5：已取消 默认：1
	public void setState(String state) {
		this.state = state;
		int s = Integer.valueOf(state);
		if(s==1) {
			this.stateName="待接单";
		}else if(s==2) {
			this.stateName="待生产";
		}else if(s==3) {
			this.stateName="待收货";
		}else if(s==4){
			this.stateName="完成";
		}else {
			this.stateName="已取消";
		}
	}


	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getOrder_receiving_time() {
		return order_receiving_time;
	}

	public void setOrder_receiving_time(String order_receiving_time) {
		this.order_receiving_time = order_receiving_time;
	}

	public String getProduction_time() {
		return production_time;
	}

	public void setProduction_time(String production_time) {
		this.production_time = production_time;
	}

	public String getDone_time() {
		return done_time;
	}

	public void setDone_time(String done_time) {
		this.done_time = done_time;
	}

	public String getCancel_reason() {
		return cancel_reason;
	}

	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}

	public List<MenuInfo> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuInfo> menus) {
		this.menus = menus;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
	
}
