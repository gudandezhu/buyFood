package com.ps.view.shopCar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ps.server.OrderServer;
import com.ps.server.ShopCarServer;
import com.ps.server.UserServer;
import com.ps.view.MessageVO;

/**
 * Servlet implementation class createOrder
 */
@WebServlet("/CreateOrder")
public class CreateOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setCharacterEncoding("UTF-8");
		//设置客户端编码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		
		String obj = request.getParameter("obj");
		JSONArray parseArray = JSONObject.parseArray(obj);
		List<ShopCarInfo> shopCars = parseArray.toJavaList(ShopCarInfo.class);
		
		try {
			MessageVO<Object> mv = new MessageVO<>();
			//先判断用户钱够不够
			double muliMoney = new UserServer().queryUserMoney(shopCars);
			if(muliMoney<0) {
				mv.setError(true);
				mv.setMsg(muliMoney+"");
				response.getWriter().write(JSONObject.toJSONString(mv));
				return;
			}
			//创建订单
			new OrderServer().createOrder(shopCars);
			//扣钱，管理员涨钱
			Double money= new UserServer().updateMoney(shopCars);
			//删除购物车信息
			new ShopCarServer().delete(shopCars);
			mv.setMsg(money+"");
			response.getWriter().write(JSONObject.toJSONString(mv));
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
