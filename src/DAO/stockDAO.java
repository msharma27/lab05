package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class stockDAO {
 
	private Connection con;
	
	public stockDAO() {
		con = DBConnector.createConnector();
	}
	
	public void sellStockDAO(String stock_symbol,int qty,double price,double amt,String sessionUname) {
		try{
			String sql = "insert into users(stock_symbol, qty, price, amt, sold_by) values (?,?,?,?,?)";
		PreparedStatement st1 = con.prepareStatement(sql);
		st1.setString(1, stock_symbol);
		st1.setDouble(2, qty);
		st1.setDouble(3, price);
		st1.setDouble(4, amt);
		st1.setString(5, sessionUname);
		st1.executeUpdate();
		st1.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}	
	}
   
}
