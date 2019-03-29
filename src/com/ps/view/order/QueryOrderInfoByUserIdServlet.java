package com.ps.view.order;

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
import com.ps.server.OrderServer;
import com.ps.server.UserServer;
import com.ps.view.MessageVO;
import com.ps.view.menu.MenuInfo;
import com.ps.view.user.UserInfo;

/**
 * Servlet implementation class QueryOrderInfoByUserIdServlet
 */
@WebServlet("/QueryOrderInfoByUserIdServlet")
public class QueryOrderInfoByUserIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryOrderInfoByUserIdServlet() {
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
		
		//查询订单信息.id, create_time, state, user_id
		String state = request.getParameter("state");
		String userId = request.getParameter("userId");
		OrderServer os = new OrderServer();
		ResultSet set = null;
		try {
			if(Integer.valueOf(state)==0) {
				set = os.queryAllOrder(userId);
			}else {
				set = os.queryOrderByState(Integer.valueOf(state),userId);
			}
			List<OrderInfo> orders = new ArrayList<>();
			while(set.next()) {
				OrderInfo order = new OrderInfo();
				order.setId(set.getInt("id"));
				order.setCreate_time(set.getString("create_time").split("\\.")[0]);
				order.setState(set.getString("state"));
				order.setUser_id(set.getInt("user_id"));
				order.setCancel_reason(set.getString("cancel_reason"));
				
				//查询此单个订单下的菜品
				order.setMenus(queryMenuInfoByOrderId(order.getId()));
				
				//查询此订单用户信息
				order.setUserInfo(queryUserInfoByUserId(order.getUser_id()));
				
				orders.add(order);
			}
			
			/*
			 * 响应浏览器
			 * */
			MessageVO messageVO = new MessageVO();
			messageVO.setBody(orders);
			String msg = JSONObject.toJSONString(messageVO);
			//将一个json字符串放回给客户端
			response.getWriter().write(msg);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	

	private UserInfo queryUserInfoByUserId(int userId) throws SQLException {
		ResultSet set = new UserServer().queryUserInfoByUserId(userId);
		while(set.next()) {
			UserInfo userInfo = new UserInfo();
			userInfo.setName(set.getString(1));
			userInfo.setPhone(set.getString(2));
			userInfo.setAddess(set.getString(3));
			return userInfo;
		}
		
		return null;
	}

	private List<MenuInfo> queryMenuInfoByOrderId(int orderId) throws SQLException {
		List<MenuInfo> menus = new ArrayList<>();
		ResultSet set = new OrderServer().queryMenuInfoByOrderId(orderId);
		while(set.next()) {	
			
			MenuInfo menu = new MenuInfo();
			
			menu.setName(set.getString(1));
			menu.setCount(set.getInt(2));
			menu.setPrice(set.getDouble(3));
			menu.setMoney(set.getDouble(4));
			
			menus.add(menu);
			
		}
		
		
		return menus;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
