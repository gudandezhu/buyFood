package com.ps.view.order;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ps.server.OrderServer;
import com.ps.server.UserServer;

/**
 * Servlet implementation class CancelOrder
 */
@WebServlet("/CancelOrder")
public class CancelOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelOrder() {
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
		String id = request.getParameter("id");
		String reason = request.getParameter("reason");
		String totalMoney = request.getParameter("totalMoney");
		String userName = request.getParameter("userName");
		OrderServer os = new OrderServer();
		try {
			//返回用户钱，管理员扣钱
			new UserServer().getMoney(totalMoney,userName);
			//取消
			os.cancelOrder(Integer.valueOf(id),reason);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
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
