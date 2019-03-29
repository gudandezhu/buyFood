package com.ps.view.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.User;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.UserServer;
import com.ps.view.MessageVO;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		
//		接收json对象数据，转换成java对象
		String obj = request.getParameter("obj");
		UserInfo userInfo = JSONObject.toJavaObject(JSONObject.parseObject(obj), UserInfo.class);
		System.out.println(userInfo);
		
		try {
			int result = new UserServer().register(userInfo);
			PrintWriter pw = response.getWriter();
			MessageVO<Object> msg = new MessageVO<>();
			if(result==1) {
				//注册成功
				msg.setError(false);
				msg.setMsg("注册成功");
			}else {
				msg.setError(true);
				msg.setMsg("账号已存在");
			}
			pw.write(JSONObject.toJSONString(msg));
			
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
