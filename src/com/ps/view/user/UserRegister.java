package com.ps.view.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.UserServer;
import com.ps.view.MessageVO;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Servlet implementation class Shopping
 */
@WebServlet("/UserRegister")

public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		//设置服务器编码
		response.setCharacterEncoding("UTF-8");
		//设置客户端编码
		response.setHeader("Content-type","text/html;charset=UTF-8");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		//先判断账号是否存在，在判断密码是否正确，返回set集合
		UserServer us = new UserServer();
		MessageVO	msg = null;
		PrintWriter pw = response.getWriter();
		try {
			if(us.checkName(name)) {  	//账号存在
				//查询密码是否正确
				if(us.checkPassword(name,password)) {
					//返回账号属于管理员还是用户
					String type = us.findUserType(name);
					//查询用户ID
					int userId = us.findUserId(name);
					msg = new MessageVO(true,type,"",userId);
				}else {
					//密码不正确
					msg = new MessageVO(false,"", "密码不正确");
				}
			}else{
				//账号不存在
				msg = new MessageVO(false,"", "账号不存在");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String json = JSONObject.toJSONString(msg);
		pw.write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
