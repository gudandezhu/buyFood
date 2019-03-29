package com.ps.view.menu;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.server.MenuServer;
import com.ps.server.UserServer;
import com.ps.view.MessageVO;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * Servlet implementation class MenuList
 */
@WebServlet("/MenuList")
public class MenuList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MenuList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	
		//设置服务器编码
		response.setCharacterEncoding("UTF-8");
		//设置客户端编码
		response.setHeader("Content-type","text/html;charset=UTF-8");
		
//		String obj = request.getParameter("obj");
//		MenuInfo menuInfo = JSONObject.toJavaObject(JSONObject.parseObject(obj),MenuInfo.class);
		
		
		MessageVO msv=null;
		MenuServer ms = new MenuServer();
		List<MenuInfo> menus = new ArrayList<MenuInfo>();
		try {
			ResultSet set = ms.menuInfo();
			while(set.next()) {
				int id = set.getInt(1);
				String name  = set.getString(2);
				double price = set.getDouble(3);
				double money = set.getDouble(4);
				String is_top= set.getString(5);
				String putaway_time = set.getTimestamp(6).toString().split("\\.")[0];
				String state = set.getString(7);
				int volume = set.getInt(8);
				
				
				MenuInfo menuInfo = new MenuInfo();
				menuInfo.setId(id);
				menuInfo.setName(name);
				menuInfo.setPrice(price);
				menuInfo.setMoney(money);
				menuInfo.setIs_top(is_top);
				menuInfo.setPutaway_time(putaway_time);
				menuInfo.setState(state);
				setState(menuInfo,state);
				menuInfo.setVolume(volume);
				menus.add(menuInfo);
			}
			msv = new MessageVO();
			msv.setBody(menus);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter pw = response.getWriter();
		String json = JSONObject.toJSONString(msv);
		pw.write(json);
	}

	private void setState(MenuInfo menuInfo, String state) {
		if(state.equals("1")) {
			menuInfo.setStateName("上架");
		}else if(state.equals("2")){
			menuInfo.setStateName("下架");
		}else {
			menuInfo.setStateName("删除");
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
