package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import POJO.userPOJO;


public class userDAO {

	private Connection con;
	
	public userDAO() {
		con = DBConnector.createConnector();
	}
		
	//Checks if a username already exists in the system
	public boolean userExist(String uname) {
		
		try {
			String sql = "SELECT * from `users` where uname = ?";
			PreparedStatement st = con.prepareStatement(sql);
//			st.setInt(1, this.id);
			System.out.println(uname+" is the username");
			
			st.setString(1, uname);

			// Execute the statement
			ResultSet rs = st.executeQuery();

			// Iterate through results
			if (rs.next()) {
				return true;
			}
			
			
		}
		 catch (SQLException e) { 
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Registers a new user into the system
	public void register(String fname,String lname,String email,String phone,String usertype,int fees,String uname,String password,String status) {

		try {
			System.out.println(usertype+" is the type of user from DAO");
		String sql2="insert into users(fname, lname, email, phone, usertype, uname, password,status) values (?,?,?,?,?,?,?,?)";
		PreparedStatement st1 = con.prepareStatement(sql2);
		st1.setString(1, fname);
		st1.setString(2, lname);
		st1.setString(3, email);
		st1.setString(4, phone);
		st1.setString(5, usertype);
		st1.setString(6, uname);
		st1.setString(7, password);
		st1.setString(8, status);
		st1.executeUpdate();
		st1.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public List<String> checkMStatus(String usertype) {
		 List<String> list;
		    list = new ArrayList<String>();
		try { 
				    String sql7 = "SELECT * from users where usertype= ?";
					PreparedStatement st7 = con.prepareStatement(sql7);
					ResultSet rs = st7.executeQuery();
					while(rs.next()) {
						String fname = rs.getString("fname");
						String lname = rs.getString("lname");
						String uname = rs.getString("uname");
						String status = rs.getString("status");
						
						list.add(fname);
						list.add(lname);
						list.add(uname);
						list.add(status);

						System.out.println(list); 

					}
					
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	
	public void managerfees(int fees, String uname, String usertype) {
		System.out.println(usertype  + " is the usertype from managerfees");
		if(usertype.equals("M")) {
			try {
				System.out.println(usertype);
				System.out.println(fees + " fees from UserDAO");
				String sql3 = "insert into managerfees(muname, mfees) values (?,?)";
				PreparedStatement st2 = con.prepareStatement(sql3);
				st2.setString(1, uname);
				st2.setInt(2, fees);
				st2.execute();
				st2.close();				
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else return; 
	}
	
	public boolean validlogin(String uname, String password) {
		try {
			String sql2 = "SELECT fname from users where uname = ? and password = ?";
			PreparedStatement st2 = con.prepareStatement(sql2);
			// System.out.println(uname+" is the uname");
			
			st2.setString(1, uname);
			st2.setString(2, password);

			// Execute the statement
			ResultSet rs2 = st2.executeQuery();
			
			if (rs2.next()) {
				return true;
			}
		}
		 catch (SQLException e) { 
				e.printStackTrace();
			}
		return false;
		}
	
	public String login(String uname) {
		try {
			String sql1 = "SELECT fname from users where uname = ?";
			PreparedStatement st1 = con.prepareStatement(sql1);
			st1.setString(1, uname);
			ResultSet rs = st1.executeQuery();
			if(rs.next()) {
				
			String s = rs.getString("fname");
			System.out.println(s+"is fname from db");
			return s;
		}
		}
		catch (SQLException e) { 
			e.printStackTrace();
		}
		return null;
	}
	
	public String fetchUsertype(String uname) {
		try {
			
		String sql2 = "SELECT usertype from users where uname = ?";
		PreparedStatement st3 = con.prepareStatement(sql2);
		st3.setString(1, uname);
		ResultSet rs = st3.executeQuery();
		if(rs.next()) {
			
			String s1 = rs.getString("usertype");
			return s1;		
		}
		}
		
	catch (SQLException e) { 
		e.printStackTrace();
	}
		return null;
	}
	
	public String myHomePage(String utype) {
		String homepage = null;
		if(utype.equals("U")) {
			homepage="userHome?faces-redirect=true";
		}
		
		if(utype.equals("M")) {
			homepage="managerHome?faces-redirect=true";
		}
		
		if(utype.equals("A")) {
			homepage="adminHome?faces-redirect=true";	
		}
		return homepage;
		
		}
	
	public userPOJO getProfileInfo(String sessionUname) {
		
		try {
			String sql4 = "SELECT * from users where uname= ?";
			PreparedStatement st4 = con.prepareStatement(sql4);
			st4.setString(1, sessionUname);
			ResultSet rs = st4.executeQuery();
			if(rs.next()) {
				userPOJO p1=new userPOJO();
				p1.setFname( rs.getString("fname") );
				p1.setLname( rs.getString("lname") );
				p1.setEmail( rs.getString("email") );
				p1.setPhone( rs.getString("phone") );
				p1.setPassword( rs.getString("password") );		
				
				return p1;
			}
			}
		
		catch (SQLException e) { 
			e.printStackTrace();
	}
			return null;
	}

	public void updateProfileInfo(String sessionUname, String sfname,String slname,String semail,String sphone,String spass) {
		
     try {
    		String sql5 = "UPDATE users SET fname= '"+sfname+"', lname= '"+slname+"', email= '"+semail+"', phone= '"+sphone+"', password= '"+spass+"' WHERE uname='"+sessionUname+"'";
			PreparedStatement st5 = con.prepareStatement(sql5);
			System.out.println(sessionUname+"\nblah\n");
			System.out.println(sfname +"blah\n");
			System.out.println(sphone +"blah\n");
			System.out.println(semail +"blah\n");
//	        st5.setString(1, sfname);
//	        st5.setString(2, slname);
//	        st5.setString(3, semail);
//	        st5.setString(4, sphone);
//	        st5.setString(5, spass); 
	        
	        st5.executeUpdate();
	        
//	      if(i == 1) {
//	    	  System.out.println("Update Success");
//	    	  return true;
//	      }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
//     		System.out.println("Update Fail");
//	    	return false;
	}
	
	public List<userPOJO> getMList(String utype) {
		List<userPOJO> userpojo = new ArrayList<userPOJO>();
		try {
			String sql6 = "SELECT * from users where usertype= '"+utype+"'";
			PreparedStatement st4 = con.prepareStatement(sql6);
			ResultSet rs = st4.executeQuery();
			userPOJO manager;
			while(rs.next()) {
				manager = new userPOJO();
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				String uname = rs.getString("uname");
				//CHECK IF UNAME DOESNOT EXIST IN "MANAGER" TABLE
				// only prints the list of managers who are available
				Boolean bool = managerCheck(uname);
				if(!bool) {
					manager.setFname(fname);
					manager.setLname(lname);
					manager.setEmail(email);
					manager.setPhone(phone);
					manager.setUname(uname);
					userpojo.add(manager);
				}			
			}
		}
			catch (SQLException e) { 
				e.printStackTrace();
		}

		return userpojo;		  
	}
	
	public void managerClient(String uname, String clientUname) {
		try {
			String sql7 = "insert into manager(clientname, managername) values (?,?)";
			PreparedStatement st7 = con.prepareStatement(sql7);
			st7.setString(1, clientUname);
			st7.setString(2, uname);
			st7.execute();
			st7.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public boolean managerCheck(String uname) {
		try {
			String sql = "SELECT managername from manager where managername= '"+uname+"' ";
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet rs1 = st.executeQuery();
			// System.out.println(uname + "uname in getMlist function before if");
			while(rs1.next()) {
				String mUname = rs1.getString("managername");
				if (uname.equals(mUname))
					return true;			
		}
      }
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	
	}
	
	public Boolean clientCheck(String uname) {
		try {
			String sql = "SELECT clientname from manager where clientname= '"+uname+"' ";
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				String cUname = rs.getString("clientname");
				if(cUname.equals(uname))
					return true; 
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;	
	}
}
	
		
	

	
	
