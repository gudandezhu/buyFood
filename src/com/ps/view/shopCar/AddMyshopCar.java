package com.ps.view.shopCar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ps.server.ShopCarServer;
import com.ps.view.MessageVO;
import com.ps.view.menu.MenuInfo;

/**
 * Servlet implementation class AddMyshopCar
 */
@WebServlet("/AddMyshopCar")
public class AddMyshopCar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMyshopCar() {
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
		
		String obj = request.getParameter("obj");
//		List<ShopCarInfo> shopCars = JSONObject.parseArray("obj", ShopCarInfo.class);
		
		List<ShopCarInfo> shopCars = JSONObject.parseArray(obj).toJavaList(ShopCarInfo.class);
		
		for(int i=0;i<shopCars.size();i++) {
			ShopCarInfo shopCar = shopCars.get(i);
			//插入数据库
			ShopCarServer scs = new ShopCarServer();
			try {
				System.out.println(shopCar.getUser_id());
				scs.add(shopCar);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//将一个json字符串放回给客户端
		response.getWriter().write("");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
