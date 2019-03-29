package com.ps.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ps.dao.Dao;
import com.ps.view.shopCar.ShopCarInfo;
import com.ps.view.user.UserInfo;

public class UserServer {
	private Dao dao = new Dao();
	/**
	 * 判断用户名是否存在
	 */
	public boolean checkName(String name) throws SQLException {
		//判断用户名是否正确
		ResultSet set = dao.queryUserInfo(name);
		return set.next();
	}
	/**
	 * 判断密码和用户名是否一致
	 */
	public boolean checkPassword(String name, String password) throws SQLException {
		ResultSet set = dao.queryUserInfo(name);
		while(set.next()){
			return set.getString("password").equals(password);
		}
		return false;
	}
	public String findUserType(String name) throws SQLException {
		ResultSet set = dao.queryUserInfo(name);
		set.next();
		return set.getString("static");
	
	}
	/**
	 *注册用户 
	 * 	返回结果 0：用户名已存在
	 * 		  1：成功
	 * @throws SQLException 
	 * 		 
	 */
	public int register(UserInfo userInfo) throws SQLException {
		//先判断用户名是否存在;
		if(checkName(userInfo.getName())) {
			return 0;
		}
		//注册
		dao.register(userInfo);
		return 1;
	}
	
	public ResultSet queryUserInfoByUserId(int userId) throws SQLException {
		return dao.queryUserInfoByUserId(userId);
	}
	public  UserInfo queryUserInfoByUserId(String id) {
		UserInfo info = new UserInfo();
		ResultSet set = dao.queryUserInfoByUserId(id);
		try {
			while(set.next()) {
				info.setId(set.getInt(1));
				info.setName(set.getString(2));
				info.setAddess(set.getString(4));
				info.setPhone(set.getString(5));
				info.setMoney(set.getFloat(7));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	public int findUserId(String name) throws SQLException {
		ResultSet set = dao.findUserId(name);
		set.next();
		
		return set.getInt("id");
	}
	public Double updateMoney(List<ShopCarInfo> shopCars) throws SQLException {
		//查询单个购物车多少钱，然后按照总价修改
		double moneys = 0;
		for(int i=0;i<shopCars.size();i++) {
			ShopCarInfo scar = shopCars.get(i);
			double money = dao.queryMoneyByShopOrder(scar);
			moneys+=money;
		}
		//修改钱数
		dao.userUpdateMoney(shopCars.get(0).getUser_id(),moneys);
		dao.adminUpdateMoney(moneys);
		return moneys;
		
	}
	public ResultSet queryAllUser() {
		return dao.queryAllUser();
	}
	public double queryUserMoney(List<ShopCarInfo> shopCars) {
		double moneys = 0;
		try {
			for(int i=0;i<shopCars.size();i++) {
				ShopCarInfo scar = shopCars.get(i);
				double money = dao.queryMoneyByShopOrder(scar);
				moneys+=money;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dao.queryUserMoney(shopCars.get(0).getUser_id())-moneys;
	}
	public void getMoney(String totalMoney, String userName) {
		dao.getMoney(totalMoney,userName);
	}
	public void recharge(String money,int id) {
		dao.recharge(money,id);
	}
}
