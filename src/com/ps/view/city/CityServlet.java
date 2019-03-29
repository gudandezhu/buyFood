package com.ps.view.city;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.Util;
import com.ps.server.MenuServer;
import com.ps.view.MessageVO;

/**
 * Servlet implementation class City
 */
@WebServlet("/City")
public class CityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityServlet() {
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
		int state = Integer.valueOf(request.getParameter("state"));
		String province_id = request.getParameter("province_id");
		String city_id = request.getParameter("city_id");
		
		try {
			Connection conn = Util.getConnection();
			PreparedStatement ps =null; 
			
			if(state==1) {
				ps = conn.prepareStatement("select * from e_province_t");
			}else if(state==2) {
				ps = conn.prepareStatement("select * from e_city_t where province_id="+province_id);
			}else if(state==3) {
				ps = conn.prepareStatement("select * from e_area_t where city_id="+city_id);
			}
			
			ResultSet set = ps.executeQuery();
			
			List<MessageWC> list = new ArrayList<>();
			while(set.next()) {
				MessageWC info = new MessageWC();
				info.setId(set.getInt(1));
				info.setName(set.getString(2));
				if(state==2) {
					info.setProvince_id(set.getInt(3));
				}else if(state==3) {
					info.setCity_id(set.getInt(3));
				}
				list.add(info);
			}
			
			//响应客户端
			response.getWriter().write(JSONObject.toJSONString(list));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
