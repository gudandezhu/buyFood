package com.ps.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ps.dao.Dao;
import com.ps.view.menu.MenuInfo;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import sun.management.counter.Variability;

public class MenuServer {
	private Dao dao = new Dao();

		/**
		 * 展示菜品列表
		 * @return 
		 * @throws SQLException 
		 */
	public ResultSet menuInfo() throws SQLException {
		return dao.queryMenuInfo();
		
		
	}

	public void updateMenuState(int id, String state) throws SQLException {
		dao.updateMenuState(id,state);
	}

	public boolean updateMenu(MenuInfo menuInfo) throws SQLException {
		if(checkName(menuInfo.getName(),menuInfo.getId())) {
			return false;
		}else {
			dao.updateMenuInfo(menuInfo);
			return true;
		}
	}

	private boolean checkName(String name, int id) throws SQLException {
		return dao.queryIsMenuByName(name,id);
	}

	public boolean checkName(String name) throws SQLException {
		
		return dao.queryMenuByName(name);
	}

	public void addMenu(MenuInfo menuInfo) throws SQLException {
		dao.addMenu(menuInfo);
	}

	public void addVolume(int orderid) throws SQLException {
		//查询此订单有多少个菜品，需要增加多少销量
		ResultSet set = dao.selectOrder(orderid);
		while(set.next()) {
			dao.addVolume(set.getInt(1),set.getInt(2));
		}
		
		
	}

	public int queryMenuTatal(String name) {
		//首先查询出一共多少行数据
		ResultSet set = dao.queryMenuListCount(name);
		int count = -1;
		try {
			while(set.next()) {
				count = set.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	public Object queryMenuListByPage(String page, String rows, String menuname, String sort, String order) {
		ResultSet set = dao.queryMenuListByPage(Integer.valueOf(page),Integer.valueOf(rows),menuname,sort,order);
		List<MenuInfo> list = new ArrayList<>();
		try {
			while(set.next()) {
				MenuInfo info = new MenuInfo();
				int id = set.getInt("id");
				String name = set.getString("name");
				double price = set.getDouble("price");
				double money = set.getDouble("money");
				int volume = set.getInt("volume");
				info.setId(id);
				info.setName(name);
				info.setPrice(price);
				info.setMoney(money);
				info.setVolume(volume);
				
				list.add(info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public int getTotal(String name, String is_top, String state) throws SQLException {
		if(name.equals("")) {
			name = "%";
		}
		if(is_top.equals("")) {
			is_top="%";
		}
		if(state.equals("")) {
			state="%";
		}
		ResultSet set = dao.queryAllMenuCount(name,is_top,state);
		set.next();
		return set.getInt(1);
	}

}
