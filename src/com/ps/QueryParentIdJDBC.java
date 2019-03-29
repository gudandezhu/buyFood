package com.ps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryParentIdJDBC {
	private List<Integer> list = new ArrayList<>();
	
	public List<Integer> queryAllParentByName(String name) {
		//先查有无此名字
		try {
			Connection conn = Util.getConnection();
			
			PreparedStatement ps = conn.prepareStatement("select * from e_level_t t where name=?");
			ps.setString(1, name);
			ResultSet set = ps.executeQuery();
			while(set.next()) {
				int id = set.getInt(1);
				int parentId = set.getInt(3);
				list.add(id);
				if(parentId!=0) {
					list.add(parentId);
					queryParentId(parentId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}

	private void queryParentId(int id) {
		try {
			Connection conn = Util.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from e_level_t t where id="+id);
			ResultSet set = ps.executeQuery();
			while(set.next()) {
				int parentId = set.getInt(3);
				if(parentId!=0) {
					list.add(parentId);
					queryParentId(parentId);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
