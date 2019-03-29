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
 * Servlet implementation class MenuDeleteOrDown
 */
@WebServlet("/MenuDeleteOrDown")
public class MenuDeleteOrDown extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MenuDeleteOrDown() {
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
		response.setHeader("Content-type","text/html;charset=UTF-8");
		
		String id = request.getParameter("id");
		String state = request.getParameter("m");
		System.out.println(id+"+++++"+state);
		MenuServer ms = new MenuServer();
		
		//修改商品状态
		try {
			ms.updateMenuState(Integer.valueOf(id),state);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		MessageVO<Object> msg = new MessageVO<>();
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
