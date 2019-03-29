package com.ps;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/CityInfoUpdateServlet")
public class CityInfoUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置服务器编码
        response.setCharacterEncoding("UTF-8");
        //设置客户端编码
        response.setHeader("Content-type","text/html;charset=UTF-8");
        String text = request.getParameter("text");
        int id = Integer.valueOf(request.getParameter("id"));
        try {
            Connection conn = Util.getConnection();
            PreparedStatement ps = conn.prepareStatement("update e_level_t set name ='"+text+"' where id = "+id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
