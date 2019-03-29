package com.ps;


import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CityTreeServlet")
public class CityTreeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置服务器编码
        response.setCharacterEncoding("UTF-8");
        //设置客户端编码
        response.setHeader("Content-type","text/html;charset=UTF-8");
        try {
            Connection conn = Util.getConnection();
            List<TreeInfoVO> list = queryAll(conn, 0);
            //响应客户端
            response.getWriter().write(JSONObject.toJSONString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String parentId = request.getParameter("parentId");
//        String type = request.getParameter("type");




    }

    private List<TreeInfoVO> queryAll(Connection conn,int parentId){
        List<TreeInfoVO> list = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from e_level_t where parentId = "+parentId);
            ResultSet set = ps.executeQuery();
            
            while (set.next()){
                TreeInfoVO infoVO = new TreeInfoVO();
                infoVO.setId(set.getInt(1));
                infoVO.setText(set.getString(2));
                String type = set.getString(4);
                	switch (type) {
					case "province":
						infoVO.setIconCls("icon-save");
						break;
					case "city":
						infoVO.setIconCls("icon-no");
						break;
					case "area":
						infoVO.setIconCls("icon-back");
						break;

					default:
						break;
					}
                List<TreeInfoVO> queryAll = queryAll(conn,set.getInt(1));
                
                if(!queryAll.isEmpty()) {
                	 infoVO.setChildren(queryAll);
                }else {
                	 infoVO.setState("open");
                }
                list.add(infoVO);
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
