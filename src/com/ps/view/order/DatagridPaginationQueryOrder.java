package com.ps.view.order;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.Util;
import com.ps.server.OrderServer;
import com.ps.view.menu.MenuVO;

/**
 * Servlet implementation class DatagridPaginationQueryOrder
 */
@WebServlet("/DatagridPaginationQueryOrder")
public class DatagridPaginationQueryOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatagridPaginationQueryOrder() {
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
		int page = Integer.valueOf(request.getParameter("page"));  //第几页
		int rows = Integer.valueOf(request.getParameter("rows"));   //每一页数量
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		int userId = Integer.valueOf(request.getParameter("userId"));   //管理员为1
		int state = Integer.valueOf(request.getParameter("state"));
		try {
			MenuVO<Object> mv = new MenuVO<>();
			OrderServer os = new OrderServer();
			//分页必要元素，总数和分页SQL
			mv.setTotal(os.QueryAllOrderCount(userId,state));
			mv.setRows(os.queryAllOrderPagination(userId,page,rows,state));
			response.getWriter().write(JSONObject.toJSONString(mv));
		} catch (Exception e) {
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
