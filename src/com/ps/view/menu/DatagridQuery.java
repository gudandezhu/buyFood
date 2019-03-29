package com.ps.view.menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.ps.Util;

/**
 * Servlet implementation class DatagridQuery
 */
@WebServlet("/DatagridQuery")
public class DatagridQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PreparedStatement ps;
	private List<String> listStr = new ArrayList<>();
	private boolean firstSort =true;
	private String moreRows = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatagridQuery() {
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
		
		/*前台传输  page   rows   sort   order   四个属性
		 * Enumeration<String> pn = request.getParameterNames();
		while(pn.hasMoreElements()) {
			System.out.println(pn.nextElement());
		}*/
		int page = Integer.valueOf(request.getParameter("page"));  //第几页
		int rows = Integer.valueOf(request.getParameter("rows"));   //每一页数量
		String name = request.getParameter("name");
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		moreRows = request.getParameter("moreRows");
		String price = request.getParameter("price");
		MenuVO<Object> mv = new MenuVO<>();
		try {
				Connection conn  = Util.getConnection();
				
				if(price!=null) {
					//设置总记录数
					setTotalBynearPrice(price,name,conn,mv);
					//价格相近查询
					//System.out.println(price+":"+page+":"+rows);
					ps = conn.prepareStatement("select * from e_memu_t where price>("+price+"-10) and price<("+price+"+10) and name like concat('%',?,'%') limit ?,?");
				}else {
					//设置总记录数
					setTotal(name,conn,mv);
					if( moreRows==null || moreRows.equals("关闭")) {
						//单行排序加分页
						singleSort(sort,order);
					}else if(moreRows.equals("启动")){
						//多行排序加分页
						moreRowsSort(sort,order,conn);
					}
				}
				allMessage(name,page,rows,mv);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().write(JSONObject.toJSONString(mv));
		
		
	}
	private void setTotalBynearPrice(String price, String name, Connection conn, MenuVO<Object> mv) throws SQLException {
		ps = conn.prepareStatement("select count(1) from e_memu_t where price="+price+" and name like concat('%',?,'%')");
		if(name==null) {
			ps.setString(1, "");
		}else {
			ps.setString(1, name);
		}
		ResultSet set2 = ps.executeQuery();
		while(set2.next()) {
			mv.setTotal(set2.getInt(1));
		}
	}

	public void setTotal(String name, Connection conn, MenuVO<Object> mv) throws SQLException {
		if(name==null) {
			ps = conn.prepareStatement("select count(1) from e_memu_t");
		}else {
			ps = conn.prepareStatement("select count(1) from e_memu_t  where name like concat('%',?,'%')");
			ps.setString(1, name);
		}
		ResultSet set2 = ps.executeQuery();
		while(set2.next()) {
			mv.setTotal(set2.getInt(1));
		}
		
	}

	private void allMessage(String name, int page, int rows, MenuVO<Object> mv) throws SQLException {
		if(name==null) {
			ps.setString(1, "");
		}else {
			ps.setString(1, name);
		}
		ps.setInt(2, (page-1)*rows);
		ps.setInt(3, rows);
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
	}

	private void singleSort(String sort, String order) throws Exception {
		listStr.clear();
		Connection conn  = Util.getConnection();
		if(sort==null) {
			ps = conn.prepareStatement("select * from e_memu_t where name like concat('%',?,'%') limit ?,?");
		}else {
			ps = conn.prepareStatement("select * from e_memu_t where name like concat('%',?,'%') order by "+sort+" "+order+" limit ?,?");
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
	private void moreRowsSort(String sort,String order,Connection conn) throws SQLException {
		if(firstSort && sort==null) {
			ps = conn.prepareStatement("select * from e_memu_t where name like concat('%',?,'%') limit ?,?");
			System.out.println(1);
		}else if(firstSort && sort!=null){
			firstSort = false;
			listStr.add(sort+" "+order);
			ps = conn.prepareStatement("select * from e_memu_t where name like concat('%',?,'%') order by "+listStr.get(0)+" limit ?,?");
			System.out.println(2+"----"+listStr.get(0));
		}else if(!firstSort && sort==null) {
			String string = "";
			for(int i=0;i<listStr.size();i++) {
				if(i==0) {
					string+=listStr.get(i);
					continue;
				}
				string+=","+listStr.get(i);
				
			}
			ps = conn.prepareStatement("select * from e_memu_t where name like concat('%',?,'%') order by "+string+" limit ?,?");
			System.out.println(3+"----"+string);
		}else {
			boolean bool = false;
			for(int i=0;i<listStr.size();i++) {
				if(listStr.get(i).contains(sort)) {
					bool = true;
					listStr.set(i,  sort+" "+order);
				}
			}
			if(!bool) {
				listStr.add(sort+" "+order);
			}
			
			String string = "";
			for(int i=0;i<listStr.size();i++) {
				if(i==0) {
					string+=listStr.get(i);
					continue;
				}
				string+=","+listStr.get(i);
			}
			ps = conn.prepareStatement("select * from e_memu_t where name like concat('%',?,'%') order by "+string+" limit ?,?");
			System.out.println(4+"----"+string);
		}
	}
}
