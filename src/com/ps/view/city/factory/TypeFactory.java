package com.ps.view.city.factory;


public class TypeFactory {
	public static String returnSQL(String type,String parentId){
		String sql = "";
		switch (type) {
		case "province":
			sql="select * from e_level_t where type="+type;
			
			break;
		case "city":
			sql="select * from e_level_t where  parentId="+parentId+" and type="+type;
			break;
		case "area":
			sql="select * from e_level_t where  parentId="+parentId+" and type="+type;
			break;

		default:
			break;
		}
		
		
		
		return sql;
		
	}
}
