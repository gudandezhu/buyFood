package com.ps.view.shopCar;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.ShopCarServer;
import com.ps.view.MessageVO;
import com.ps.view.menu.MenuVO;

/**
 * Servlet implementation class QueryShopCarInfo
 */
@WebServlet("/QueryShopCarInfo")
public class QueryShopCarInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryShopCarInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//设置服务器编码
		response.setCharacterEncoding("UTF-8");
		//设置客户端编码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		
		int id = Integer.valueOf(request.getParameter("userId"));
		int page = Integer.valueOf(request.getParameter("page"));  //第几页
		int rows = Integer.valueOf(request.getParameter("rows"));   //每一页数量
		ShopCarServer scs = new ShopCarServer();
		
		List<ShopCarInfo> list = new ArrayList<>();
		try {
			ResultSet set = scs.qeuryAllInfo(id,page,rows);
			while(set.next()) {
				ShopCarInfo carInfo = new ShopCarInfo();
				carInfo.setId(set.getInt(1));
				carInfo.setMenu_name(set.getString(2));
				carInfo.setMenu_price(set.getDouble(3));
				carInfo.setMenu_money(set.getDouble(4));
				carInfo.setCount(set.getInt(5));
				carInfo.setUser_id(set.getInt(6));
				carInfo.setMenu_id(set.getInt(7));
				carInfo.setCreate_time(set.getString(8));
				list.add(carInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MenuVO<Object> msg = new MenuVO<>();
		//设置分页需要的总行数
		msg.setTotal(scs.queryshopcartotal(id,page,rows));
		//返回数据数组
		msg.setRows(list);
		
		response.getWriter().write(JSONObject.toJSONString(msg));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
