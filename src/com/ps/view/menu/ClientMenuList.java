package com.ps.view.menu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.MenuServer;
import com.ps.view.MessageVO;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * Servlet implementation class ClientMenuList
 */
@WebServlet("/ClientMenuList")
public class ClientMenuList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MenuServer ms = new MenuServer();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientMenuList() {
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
		
		//支持分页
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		//支持搜索
		String name = request.getParameter("name");
		MenuVO<Object> msv = new MenuVO<>();
		msv.setTotal(ms.queryMenuTatal(name));   //查询出总数
		msv.setRows(ms.queryMenuListByPage(page,rows,name,sort,order));
		
		
		String json = JSONObject.toJSONString(msv);
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
