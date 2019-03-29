package com.ps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.ps.Util;
import com.ps.view.menu.MenuInfo;
import com.ps.view.shopCar.ShopCarInfo;
import com.ps.view.user.UserInfo;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class Dao {
	Connection conn = null;
	PreparedStatement ps = null;
	{
		try {
			conn = Util.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 查询用户信息
	 */
	public ResultSet queryUserInfo(String name) throws SQLException {
		ps = conn.prepareStatement("select * from e_user_t where name=?");
		ps.setString(1, name);
		
		return ps.executeQuery();
	}
	/**
	 * 注册
	 */
	public void register(UserInfo userInfo) throws SQLException {
		ps = conn.prepareStatement("insert into e_user_t values(null,?,?,?,?,2,500)");	
		ps.setString(1, userInfo.getName());
		ps.setString(2, userInfo.getPassword());
		ps.setString(3, userInfo.getAddess());
		ps.setString(4, userInfo.getPhone());
	
		ps.executeUpdate();
	}
	
	/**
	 * 菜品信息
	 */
	public ResultSet queryMenuInfo() throws SQLException {
		 ps = conn.prepareStatement("select * from e_memu_t order by volume desc");	
		
		return ps.executeQuery();
	}
	
	/**
	 * 修改菜品状态为删除或下架
	 */
	public void updateMenuState(int id, String state) throws SQLException {
		ps = conn.prepareStatement("update e_memu_t set state=? where id=?");
		ps.setString(1, state);
		ps.setInt(2, id);
		ps.executeUpdate();
	}
	public void updateMenuInfo(MenuInfo menuInfo) throws SQLException {
		ps = conn.prepareStatement("update e_memu_t set name=?,price=?,money=?,is_top=?,state=? where id=?");
		ps.setString(1, menuInfo.getName());
		ps.setDouble(2, menuInfo.getPrice());
		ps.setDouble(3, menuInfo.getMoney());
		ps.setString(4, menuInfo.getIs_top());
		ps.setString(5, menuInfo.getState());
		ps.setInt(6, menuInfo.getId());
		ps.executeUpdate();
	}
	
	/**
	 * 判断菜品名是否存在
	 */
	public boolean queryMenuByName(String name) throws SQLException {
		ps = conn.prepareStatement("select * from e_memu_t where name=?");
		ps.setString(1, name);
		return  ps.executeQuery().next();
	}
	public boolean queryIsMenuByName(String name,int id) throws SQLException {
		ps = conn.prepareStatement("select * from e_memu_t where id=?");
		ps.setInt(1, id);
		ResultSet set = ps.executeQuery();
		set.next();
		String oldname = set.getString("name");
		if(oldname.equals(name)) {
			return  false;
		}else {
			ps = conn.prepareStatement("select * from e_memu_t where name=?");
			ps.setString(1, name);
			return ps.executeQuery().next();
		}
	}
	public void addMenu(MenuInfo menuInfo) throws SQLException {
		ps = conn.prepareStatement("insert into e_memu_t values(null,?,?,?,?,now(),?,0)");
		ps.setString(1, menuInfo.getName());
		ps.setDouble(2, menuInfo.getPrice());
		ps.setDouble(3, menuInfo.getMoney());
		ps.setString(4, menuInfo.getIs_top());
		ps.setString(5, menuInfo.getState());
		
		ps.executeUpdate();
	}
	public ResultSet queryAllOrder() throws SQLException {
		ps = conn.prepareStatement("select id,create_time,state,user_id,cancel_reason from e_order_t");
		return  ps.executeQuery();
	}
	public ResultSet queryAllOrder(String userId) throws SQLException {
		ps = conn.prepareStatement("select id,create_time,state,user_id,cancel_reason from e_order_t where user_id="+userId);
		return  ps.executeQuery();
	}
	public ResultSet queryOrderByState(int state) throws SQLException {
		ps = conn.prepareStatement("select id,create_time,state,user_id,cancel_reason from e_order_t where state = ?");
		ps.setInt(1, state);
		return  ps.executeQuery();
	}
	public ResultSet queryMenuInfoByOrderId(int orderId) throws SQLException {
		ps = conn.prepareStatement("select t2.name,t1.count,t2.price,t2.money from e_order_menu_ref_t t1 left join e_memu_t t2 on(t1.menu_id=t2.id) where t1.order_id=?");
		ps.setInt(1, orderId);
		return  ps.executeQuery();
	}
	public ResultSet queryUserInfoByUserId(int userId) throws SQLException {
		ps = conn.prepareStatement("select name,phone,addess from e_user_t where id=?");
		ps.setInt(1, userId);
		return  ps.executeQuery();
	}
	public ResultSet queryUserInfoByUserId(String userId){
		ResultSet set=null;
		try {
			ps = conn.prepareStatement("select * from e_user_t where id="+userId);
			set= ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}
	public void updateOrderStateById(int orderid,int state) throws SQLException {
		if(state==1) {
			ps = conn.prepareStatement("update e_order_t set state=?,order_receiving_time=now()	 where id=?");
			ps.setInt(1, 2);
		}else if(state==2){
			ps = conn.prepareStatement("update e_order_t set state=?,production_time=now()	 where id=?");
			ps.setInt(1, 3);
		}else if(state==3) {
			ps = conn.prepareStatement("update e_order_t set state=?,done_time=now()	 where id=?");
			ps.setInt(1, 4);
		}
		ps.setInt(2, orderid);
		ps.executeUpdate();
	}
	public void cancelOrder(Integer id, String reason) throws SQLException {
		ps = conn.prepareStatement("update e_order_t set state=5,cancel_reason=? where id=?");
		ps.setString(1, reason);
		ps.setInt(2, id);
		ps.executeUpdate();
		
		//管理员扣钱
		ps=conn.prepareStatement("update e_user_t set money=money-(select sum(count*(price-money)) from e_order_menu_ref_t t1 left join e_memu_t t2 on(t1.menu_id=t2.id) where order_id=?) where id = 1");
		ps.setInt(1, id);
		ps.executeUpdate();
		
		//用户加钱
		ps=conn.prepareStatement("update e_user_t set money=money+(select sum(count*(price-money)) from e_order_menu_ref_t t1 left join e_memu_t t2 on(t1.menu_id=t2.id) where order_id=?) where id =(select user_id from e_order_t where id=?)");
		ps.setInt(1, id);
		ps.setInt(2, id);
		ps.executeUpdate();
	}
	public ResultSet findUserId(String name) throws SQLException {
		ps = conn.prepareStatement("select id from e_user_t where name=?");
		ps.setString(1, name);
		
		return ps.executeQuery();
	}
	public void add(ShopCarInfo shopCar) throws SQLException {
		ps=conn.prepareStatement("insert into e_shopping_cart_t values(null,?,?,?,now())");
		ps.setInt(1, shopCar.getMenu_id());
		ps.setInt(2, shopCar.getUser_id());
		ps.setInt(3, shopCar.getCount());
		
		ps.executeUpdate();
	}
	public ResultSet qeuryAllShopCarInfo(int userid, int page, int rows) throws SQLException {
		ps=conn.prepareStatement("select t1.id,name,price,money,count,user_id,t2.id,t1.create_time from e_shopping_cart_t t1 left join e_memu_t t2 on(t1.menu_id=t2.id) where user_id=? limit ?,?");
		ps.setInt(1, userid);
		ps.setInt(2, (page-1)*rows);
		ps.setInt(3, rows);
		return ps.executeQuery();
	}
	public int createorder(int userid) throws SQLException {
		ps=conn.prepareStatement("insert into e_order_t values(null,?,1,now(),null,null,null,null)",PreparedStatement.RETURN_GENERATED_KEYS);
		ps.setInt(1, userid);
		
		ps.executeUpdate();
		ResultSet set = ps.getGeneratedKeys();
		set.next();
		return set.getInt(1);
	}
	public void createOrderAndMenuRef(ShopCarInfo carInfo, int orderId) throws SQLException {
		ps=conn.prepareStatement("insert into e_order_menu_ref_t values(null,?,?,?)");
		ps.setInt(1,orderId);
		ps.setInt(2,carInfo.getMenu_id());
		System.out.println();
		ps.setInt(3,carInfo.getCount());
		ps.executeUpdate();
	}
	public void delete(ShopCarInfo shopCarInfo) throws SQLException {
		ps=conn.prepareStatement("delete from e_shopping_cart_t where id=?");
		ps.setInt(1, shopCarInfo.getId());
		ps.executeUpdate();
	}
	public double queryMoneyByShopOrder(ShopCarInfo scar) throws SQLException {
		ps=conn.prepareStatement("select (select (price-money) from e_memu_t where id=t1.menu_id)*count from e_shopping_cart_t t1 where id=?");
		ps.setInt(1, scar.getId());
		ResultSet set = ps.executeQuery();
		set.next();
		return set.getDouble(1);
	}
	public void userUpdateMoney(int user_id, double moneys) throws SQLException {
		ps=conn.prepareStatement("update e_user_t set money=(money-?) where id=?");
		ps.setDouble(1, moneys);
		ps.setInt(2, user_id);
		ps.executeUpdate();
		
	}
	public void adminUpdateMoney(double moneys) throws SQLException {
		ps=conn.prepareStatement("update e_user_t set money=(money+?) where id=1");
		ps.setDouble(1, moneys);
		ps.executeUpdate();
	}
	public void delete(Integer id) throws SQLException {
		ps=conn.prepareStatement("delete from e_shopping_cart_t where id=?");
		ps.setInt(1, id);
		ps.executeUpdate();
		
	}
	public ResultSet queryOrderByState(Integer state, String userId) throws SQLException {
		ps = conn.prepareStatement("select id,create_time,state,user_id,cancel_reason from e_order_t where state = ? and user_id=?");
		ps.setInt(1, state);
		ps.setInt(2, Integer.valueOf(userId));
		return  ps.executeQuery();
	}
	public int queryMenu_idByName(String menu_name) throws SQLException {
		ps=conn.prepareStatement("select id from e_memu_t where name = ?");
		ps.setString(1, menu_name);
		ResultSet set = ps.executeQuery();
		set.next();
		return set.getInt(1);
	}
	
	public ResultSet selectOrder(int orderid) throws SQLException {
		ps=conn.prepareStatement("select menu_id,count from e_order_menu_ref_t where order_id=?");
		ps.setInt(1, orderid);
		return ps.executeQuery();
	}
	public void addVolume(int menu_id, int count) {
		try {
			ps=conn.prepareStatement("update e_memu_t set volume=(volume+?) where id = ?");
			
			ps.setInt(1, count);
			ps.setInt(2, menu_id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ResultSet queryMenuListCount(String name) {
		if(name==null){
			name = "%";
		}
		ResultSet set=null;
		try {
			ps=conn.prepareStatement("select count(1) from e_memu_t where state=1 and name like concat('%',?,'%')");
			ps.setString(1, name);
			set =  ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	public ResultSet queryMenuListByPage(int page, int rows, String name, String sort, String order) {
		ResultSet set=null;
		if(name==null){
			name = "%";
		}
		if(sort==null) {
			sort = "is_top";
			order = "desc";
		}
		
		try {
			ps=conn.prepareStatement("select * from e_memu_t where state=1 and name like concat('%',?,'%') order by "+sort+" "+order+",name desc limit ?,?");
			ps.setString(1, name);
			ps.setInt(2, (page-1)*rows);
			ps.setInt(3, rows);
			
			set =  ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	public int queryshopcartotal(int id,int page, int rows) {
		int total = 0;
		try {
			ps=conn.prepareStatement("select count(1) from e_shopping_cart_t where user_id="+id);
			ResultSet set =  ps.executeQuery();
			set.next();
			total = set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}
	public int QueryAllOrderCount(int userId, int state) {
		int total = 0;
		
		try {
			if(state==-1) {
				ps=conn.prepareStatement("select count(1) from e_order_t where user_id like ?");
			}else {
				ps=conn.prepareStatement("select count(1) from e_order_t where user_id like ? and state="+state);
			}
			
			if(userId==1) {              //此处应该查user表的static如果为1则是管理员,暂不写
				ps.setString(1, "%");
			}else {
				ps.setInt(1, userId);
			}
			ResultSet set =  ps.executeQuery();
			set.next();
			total = set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
		
	}
	public ResultSet queryAllOrderPagination(int userId, int page, int rows, int state) {
		ResultSet set=null;
		try {
			if(state==-1) {
				ps=conn.prepareStatement("select t1.id,create_time,t2.name,t2.addess,t1.state,t1.cancel_reason,t2.phone from e_order_t t1 left join e_user_t t2 on(t1.user_id=t2.id) left join e_order_menu_ref_t t3 on(t1.id=t3.order_id) where t2.id=? group by t1.id limit ?,?");
			}else {
				ps=conn.prepareStatement("select t1.id,create_time,t2.name,t2.addess,t1.state,t1.cancel_reason,t2.phone from e_order_t t1 left join e_user_t t2 on(t1.user_id=t2.id) left join e_order_menu_ref_t t3 on(t1.id=t3.order_id) where t2.id=? and t1.state="+state+" group by t1.id limit ?,?");
			}
			ps.setInt(1, userId);
			ps.setInt(2, (page-1)*rows);
			ps.setInt(3, rows);
			
			set =  ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	public ResultSet queryAllOrderPagination(int page, int rows, int state) {
		ResultSet set=null;
		try {
			if(state==-1) {
				ps=conn.prepareStatement("select t1.id,create_time,t2.name,t2.addess,t1.state,t1.cancel_reason,t2.phone from e_order_t t1 left join e_user_t t2 on(t1.user_id=t2.id) left join e_order_menu_ref_t t3 on(t1.id=t3.order_id)  group by t1.id limit ?,?");
			}else {
				ps=conn.prepareStatement("select t1.id,create_time,t2.name,t2.addess,t1.state,t1.cancel_reason,t2.phone from e_order_t t1 left join e_user_t t2 on(t1.user_id=t2.id) left join e_order_menu_ref_t t3 on(t1.id=t3.order_id) where t1.state="+state+" group by t1.id limit ?,?");
			}
			ps.setInt(1, (page-1)*rows);
			ps.setInt(2, rows);
			
			set =  ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	public ResultSet queryAllUser() {
		ResultSet set =null;
		try {
			ps=conn.prepareStatement("select id,name from e_user_t where static!=1");
			set =  ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}
	public ResultSet queryAllMenuCount(String name, String is_top, String state) {
		ResultSet set =null;
		try {
			System.out.println(name+":"+is_top+":"+state);
			ps=conn.prepareStatement("select count(1) from e_memu_t where name like concat('%',?,'%') and is_top like ? and state like ?");
			ps.setString(1, name);
			ps.setString(2, is_top);
			ps.setString(3, state);
			set = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}
	public boolean checkShopCarMenu(ShopCarInfo shopCar) {
		boolean bool =false;
		try {
			ps=conn.prepareStatement("select * from e_shopping_cart_t where menu_id="+shopCar.getMenu_id()+" and user_id="+shopCar.getUser_id());
			bool = ps.executeQuery().next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bool;
	}
	public void updateShopCarMenuCount(ShopCarInfo shopCar) {
		try {
			ps=conn.prepareStatement("update e_shopping_cart_t set count=count+"+shopCar.getCount()+" where menu_id="+shopCar.getMenu_id());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public double queryUserMoney(int user_id) {
		double money = 0;
		try {
			ps=conn.prepareStatement("select money from e_user_t where id="+user_id);
			ResultSet set = ps.executeQuery();
			while(set.next()) {
				money = set.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return money;
	}
	public void getMoney(String totalMoney, String userName) {
		try {
			ps=conn.prepareStatement("update e_user_t set money=money+"+totalMoney+" where name='"+userName+"'");
			ps.executeUpdate();
			
			ps=conn.prepareStatement("update e_user_t set money=money-"+totalMoney+" where id=1");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void recharge(String money,int id) {
		try {
			ps=conn.prepareStatement("update e_user_t set money=money+"+money+" where id="+id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
