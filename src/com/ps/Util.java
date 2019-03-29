package com.ps;



import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;


public class Util {
	private static Scanner input = new Scanner(System.in);
	private static Random rd = new Random();
	private static Connection conn;			//连接
	//public static BasicDataSource  ds;   //连接池
	
	private Util() {}   //不能创建实例
	public static String inputString() {
		return input.next();
	}
	
	public static int inputInt() {
		return input.nextInt();
	}
	
	
	
	public static int random(int i,int j) {
		return rd.nextInt(i)+j;
	}
	/**
	 * 连接本地数据库的方法
	 * 可传入数据库名
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		
		if(conn==null) {
			Class.forName("com.mysql.jdbc.Driver");
			Properties p =new Properties();
			InputStream is = Util.class.getClassLoader().getResourceAsStream("\\db.properties");
			p.load(is);
			String url=p.getProperty("url");
			System.out.print("正在连接数据库 "+url+"  ");
			for(int i=0;i<6;i++) {
				//Thread.sleep(500);
				System.out.print(".");
			}
			System.out.println();
			//Thread.sleep(1000);
			String user=p.getProperty("name");
			System.out.println("用户名: "+user);
			//Thread.sleep(200);
			String password=p.getProperty("password");
			System.out.println("密码：******");
			//Thread.sleep(200);
			conn = DriverManager.getConnection(url,user,password);
			is.close();
			System.out.println("连接成功!!!\n\n");
		}
		return conn;
	}
//	public static synchronized Connection getConnectionByPool() throws Exception {
//		
//		if(ds==null) {
//			Properties p = new Properties();
//			p.load(Util.class.getClassLoader().getResourceAsStream("\\pool.properties"));
//			ds = BasicDataSourceFactory.createDataSource(p);
//		}
//		return ds.getConnection();
//	}
	
	
	
	
	/**
	 * 给一个sql数据库传过来的结果集SET，将其全部打印出来
	 */
	public static void printResultSet(ResultSet set) throws SQLException {
		ResultSetMetaData md = set.getMetaData();
		while(set.next()) {
			for(int i=1;i<=md.getColumnCount();i++) {
				String label=md.getColumnLabel(i);
				System.out.print(set.getObject(label)+"\t");
			}
			System.out.println();
		}
	}
}
