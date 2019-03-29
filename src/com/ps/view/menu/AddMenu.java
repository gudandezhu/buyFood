package com.ps.view.menu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.MenuServer;
import com.ps.view.MessageVO;

/**
 * Servlet implementation class AddMenu
 */
@WebServlet("/AddMenu")
public class AddMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMenu() {
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
		
		
		MenuInfo menuInfo = JSONObject.toJavaObject(JSONObject.parseObject(obj), MenuInfo.class);
		
		System.out.println(menuInfo);
		
		MenuServer ms = new MenuServer();
		MessageVO msg=new MessageVO(false,"1","");
		try {
			if(ms.checkName(menuInfo.getName())) {
				//已存在此菜品
				msg.setError(true);
				msg.setMsg("商品名已存在，请重填");
			}else {
				//不存在，添加
				ms.addMenu(menuInfo);
			}
			response.getWriter().write(JSONObject.toJSONString(msg));
			
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
