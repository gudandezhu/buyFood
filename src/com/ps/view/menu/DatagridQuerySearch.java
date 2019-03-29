package com.ps.view.menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.Util;
import com.ps.server.MenuServer;

/**
 * Servlet implementation class DatagridQuerySearch
 */
@WebServlet("/DatagridQuerySearch")
public class DatagridQuerySearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PreparedStatement ps;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatagridQuerySearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置服务器编码
		response.setCharacterEncoding("UTF-8");
		//设置客户端编码
		response.setHeader("Content-type","text/html;charset=UTF-8");
		int page = Integer.valueOf(request.getParameter("page"));  //第几页
		int rows = Integer.valueOf(request.getParameter("rows"));   //每一页数量
		String order = request.getParameter("order");				//排序
		String sort = request.getParameter("sort");
		String name = request.getParameter("name");
		String is_top = request.getParameter("isTop");
		String state  = request.getParameter("state");
		if(sort==null) {
			sort = "id";
			order = "asc";
		}
		
		MenuVO<Object> mv = new MenuVO<>();
		
		try {
			Connection conn  = Util.getConnection();
			mv.setTotal(new MenuServer().getTotal(name,is_top,state));
			
			ps=conn.prepareStatement("select * from e_memu_t where is_top like ? and state like ? and name like concat('%',?,'%') order by "+sort+" "+order+",name desc limit ?,?");
			
			if(is_top.equals("")) {
				ps.setString(1, "%");
			}else {
				ps.setString(1, is_top);
			}
			if(state.equals("")) {
				ps.setString(2, "%");
			}else {
				ps.setInt(2, Integer.valueOf(state));
			}
			if(name.equals("")) {
				ps.setString(3, "");
			}else {
				ps.setString(3, name);
			}
			ps.setInt(4, (page-1)*rows);
			ps.setInt(5, rows);
			
			ResultSet set = ps.executeQuery();
			
			List<MenuInfo> list = new ArrayList<>();
			while(set.next()) {
				
				MenuInfo menuInfo = new MenuInfo();
				menuInfo.setId(set.getInt("id"));
				menuInfo.setName(set.getString("name"));
				menuInfo.setState(set.getString("state"));
				menuInfo.setPrice(set.getDouble("price"));
				menuInfo.setMoney(set.getDouble("money"));
				menuInfo.setPutaway_time(set.getString("putaway_time"));
				menuInfo.setIs_top(set.getString("is_top"));
				menuInfo.setVolume(set.getInt("volume"));
				menuInfo.setRemark(checkRemark(set));
				list.add(menuInfo);
			}
			mv.setRows(list);
			response.getWriter().write(JSONObject.toJSONString(mv));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private String checkRemark(ResultSet set) throws SQLException {
		long createtime = set.getTimestamp("putaway_time").getTime();
		long now = new Date().getTime();
		if((now-createtime)/1000/3600>2) {
			return "请尽快处理此单";
		}
		return "";
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
