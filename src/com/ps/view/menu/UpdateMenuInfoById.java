package com.ps.view.menu;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.MenuServer;
import com.ps.view.MessageVO;

/**
 * Servlet implementation class UpdateMenuInfoById
 */
@WebServlet("/UpdateMenuInfoById")
public class UpdateMenuInfoById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateMenuInfoById() {
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
		MenuInfo menuInfo = JSONObject.toJavaObject(JSONObject.parseObject(obj),MenuInfo.class);
		
		MenuServer ms = new MenuServer();
		try {
			MessageVO<Object> msg = new MessageVO<>();
			if(!ms.updateMenu(menuInfo)) {
				msg.setError(true);
				msg.setMsg("菜品名字不能重复");
				response.getWriter().write(JSONObject.toJSONString(msg));
			}else {
				response.getWriter().write(JSONObject.toJSONString(msg));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
