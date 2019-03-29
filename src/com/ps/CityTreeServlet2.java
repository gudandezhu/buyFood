package com.ps;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;

/**
 * Servlet implementation class CityTreeServlet2
 */
@WebServlet("/CityTreeServlet2")
public class CityTreeServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CityTreeServlet2() {
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
        String parentId = request.getParameter("parentId");
        String id = request.getParameter("id");
        String queryName = request.getParameter("name");
        
        if(queryName!=null) {
        	QueryParentIdJDBC jdbc = new QueryParentIdJDBC();
        	List<Integer> list = jdbc.queryAllParentByName(queryName);
        	response.getWriter().write(JSONObject.toJSONString(list));
        	return;
        }
        if(id!=null){
        	parentId=id;
        }
        try {
        	//Thread.sleep(1000);
        	if(StringUtils.isNullOrEmpty(parentId)) {
        		parentId="0";
        	}
			Connection conn = Util.getConnection();
			PreparedStatement ps = conn.prepareStatement("select t.id,t.name,if((select count(tt.id) from e_level_t tt where t.id=tt.parentId)=0,'open','closed') from e_level_t t  where parentId = "+parentId);
			ResultSet set = ps.executeQuery();
			List<TreeInfoVO> list = new ArrayList<>();
			while(set.next()) {
				TreeInfoVO infoVO = new TreeInfoVO();
				int ids = set.getInt(1);
				String name = set.getString(2);
				String state = set.getString(3);
				
				infoVO.setId(ids);
				infoVO.setText(name);
				infoVO.setState(state);
				
				list.add(infoVO);
			}
			 response.getWriter().write(JSONObject.toJSONString(list));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
