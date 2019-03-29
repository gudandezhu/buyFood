package com.ps.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ps.dao.Dao;
import com.ps.view.menu.MenuInfo;
import com.ps.view.menu.MenuVO;
import com.ps.view.order.OrderVO;
import com.ps.view.shopCar.ShopCarInfo;

public class OrderServer {
	private Dao dao = new Dao();

	public ResultSet queryAllOrder() throws SQLException {
		return dao.queryAllOrder();
	}

	public ResultSet queryOrderByState(int state) throws SQLException {
		return  dao.queryOrderByState(state);
	}

	public ResultSet queryMenuInfoByOrderId(int orderId) throws SQLException {
		
		return dao.queryMenuInfoByOrderId(orderId);
	}

	public void updateOrderStateById(int orderid, int state) throws SQLException {
		dao.updateOrderStateById(orderid,state); 
		
	}

	public void cancelOrder(Integer id, String reason) throws SQLException {
		dao.cancelOrder(id,reason);
		
	}

	public void createOrder(List<ShopCarInfo> shopCars) throws SQLException {
		//创建一个订单数据，多个关系数据
		
		int orderId = dao.createorder(shopCars.get(0).getUser_id());
		
		for(int i=0;i<shopCars.size();i++) {
			ShopCarInfo carInfo = shopCars.get(i);
			carInfo.setMenu_id(dao.queryMenu_idByName(carInfo.getMenu_name()));
			
			dao.createOrderAndMenuRef(carInfo,orderId);
		}
	}

	public ResultSet queryAllOrder(String userId) throws SQLException {
		return dao.queryAllOrder(userId);
	}

	public ResultSet queryOrderByState(Integer state, String userId) throws SQLException {
		return  dao.queryOrderByState(state,userId);
	}

	public int QueryAllOrderCount(int userId, int state) {
		
		return dao.QueryAllOrderCount(userId,state);
	}

	//t1.id,create_time,t2.name,t2.addess,t1.state
	public Object queryAllOrderPagination(int userId, int page, int rows, int state) {
		ResultSet set = null;
		List<OrderVO> list = new ArrayList<>();
		
		try {
			if(userId == 1) {
				set = dao.queryAllOrderPagination(page,rows,state);
			}else {
				set = dao.queryAllOrderPagination(userId,page,rows,state);
			}
			while(set.next()){
				OrderVO ovo = new OrderVO();
				int id = set.getInt(1);
				String create_time = set.getString(2);
				String userName = set.getString(3);
				String address = set.getString(4);
				int state2 = set.getInt(5);
				String reason = set.getString(6);
				String userPhone = set.getString(7);
				
				
				ovo.setId(id);
				ovo.setCreate_time(create_time);
				ovo.setUserName(userName);
				ovo.setAddress(address);
				ovo.setState(state2);
				ovo.setRemark(reason);
				ovo.setUserPhone(userPhone);
				//查每个订单下面的菜品，然后放到list集合里，再赋给ovo这个集合
				ResultSet menuset = queryMenuInfoByOrderId(id);
				List<MenuInfo> menulist = new ArrayList<>();
				double total = 0;
				while(menuset.next()) {
					MenuInfo info = new MenuInfo();
					info.setName(menuset.getString(1));
					info.setCount(menuset.getInt(2));
					info.setPrice(menuset.getDouble(3));
					info.setMoney(menuset.getDouble(4));
					total += info.getCount()*(info.getPrice()-info.getMoney());
					
					menulist.add(info);
					ovo.setMenus(menulist);
				}
				ovo.setTotalMoney(total);
				list.add(ovo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
	
	
}
