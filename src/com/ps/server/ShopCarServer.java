package com.ps.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ps.dao.Dao;
import com.ps.view.shopCar.ShopCarInfo;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class ShopCarServer {
	private Dao dao = new Dao();

	public void add(ShopCarInfo shopCar) throws SQLException {
		
		//如果购物车有相同菜品，则增加数量
		if(dao.checkShopCarMenu(shopCar)){
			dao.updateShopCarMenuCount(shopCar);
		}else {
			dao.add(shopCar);
		}
	}

	public ResultSet qeuryAllInfo(int userid, int page, int rows) throws SQLException {
		return dao.qeuryAllShopCarInfo(userid,page,rows);
	}

	public void delete(List<ShopCarInfo> shopCars) throws SQLException {
		for(int i=0;i<shopCars.size();i++) {
			dao.delete(shopCars.get(i));
		}
		
	}

	public void deleteCarById(Integer id) throws SQLException {
		dao.delete(id);
		
	}

	public int queryshopcartotal(int id, int page, int rows) {
		return dao.queryshopcartotal(id,page,rows);
	}
	
}
