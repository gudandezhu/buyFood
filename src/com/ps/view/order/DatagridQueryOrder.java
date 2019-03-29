package com.ps.view.order;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.ps.view.menu.MenuVO;

/**
 * Servlet implementation class DatagridQueryOrder
 */
@WebServlet("/DatagridQueryOrder")
public class DatagridQueryOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatagridQueryOrder() {
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
		String order = request.getParameter("order");
		String sort = request.getParameter("sort");
		try {
			Connection conn  = Util.getConnection();
			//先总共多少行数据
			PreparedStatement ps = conn.prepareStatement("select count(*) from e_order_t");
			ResultSet set2 = ps.executeQuery();
			set2.next();
			int total = set2.getInt(1);
			MenuVO<Object> mv = new MenuVO<>();
			mv.setTotal(total);
			if(sort == null) {
				ps = conn.prepareStatement("select t1.id,create_time,t2.name,t2.addess,sum(t3.count*(t4.price-t4.money)) totalMoney,t1.state from e_order_t t1 left join e_user_t t2 on(t1.user_id=t2.id) left join e_order_menu_ref_t t3 on(t1.id=t3.order_id) left join e_memu_t t4 on(t3.menu_id=t4.id) group by t1.id limit ?,?");
			}else {
				ps = conn.prepareStatement("select * from (select t1.id,create_time,t2.name,t2.addess,sum(t3.count*(t4.price-t4.money)) totalMoney,t1.state from e_order_t t1 left join e_user_t t2 on(t1.user_id=t2.id) left join e_order_menu_ref_t t3 on(t1.id=t3.order_id) left join e_memu_t t4 on(t3.menu_id=t4.id) group by t1.id limit ?,?) t order by "+"t."+sort+" "+order);
			}
			ps.setInt(1, (page-1)*rows);
			ps.setInt(2, rows);
			
			ResultSet set = ps.executeQuery();
			List<OrderVO> list = new ArrayList<>();
			while(set.next()) {
				OrderVO orderv = new OrderVO();
				orderv.setId(set.getInt(1));
				orderv.setCreate_time(set.getString(2));
				orderv.setState(set.getInt(6));
				if(orderv.getState()==1) {
					orderv.setRemark(chcekRemark(set));
				}
				orderv.setUserName(set.getString(3));
				orderv.setAddress(set.getString(4));
				orderv.setTotalMoney(set.getDouble(5));
				list.add(orderv);
			}
			mv.setRows(list);
			response.getWriter().write(JSONObject.toJSONString(mv));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	
	
	}

	private String chcekRemark(ResultSet set) throws SQLException {
		long createtime = set.getTimestamp("create_time").getTime();
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
