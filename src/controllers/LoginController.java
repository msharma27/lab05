package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import DAO.userDAO;
import POJO.userPOJO;

/**
 * @author mshar
 *
 */
@ManagedBean(name="loginController")
@RequestScoped
public class LoginController {
	private String fname;
	private String uname;
	private String password;
	private String sessionFname;
	private String sessionUname;
	private String mStatus;
	
	public String getSessionFname() {
		return sessionFname;
	}
	public void setSessionFname(String sessionFname) {
		this.sessionFname = sessionFname;
	}
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public String getSessionUname() {
		return sessionUname;
	}
	public void setSessionUname(String sessionUname) {
		this.sessionUname = sessionUname;
	}
	
	
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	public LoginController() {
		
	}	
		userDAO u1= new userDAO();
		public String loginUser() {
			boolean b = u1.validlogin(this.uname, this.password);
		try {
			
			if(b==true)
			{
				System.out.println("Details Valid. User present");
				System.out.println(this.uname);
				String usertype = u1.fetchUsertype(this.uname);
				
				// check if (usertype == m){
				// get status of manager
				// if (mstatus !=1) {
				// your account registration is pending
				// send him to login page again
				// no need of else 
				//
				System.out.println(usertype + "is the usertype from logincontroller");
				this.sessionFname=u1.login(this.uname);
				this.sessionUname=this.uname;
				// System.out.println(sessionFname + "from login controller");
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionFname",sessionFname);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionUname",sessionUname);
				String home = u1.myHomePage(usertype);
				// System.out.println(sessionUname + "is the session username from login controller");
				System.out.println(home);
				return home;		
			}
			else {
				FacesContext fc= FacesContext.getCurrentInstance();
				FacesMessage fmsg = new FacesMessage("Invalid Username or password");
				//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User already exista"));
				fc.addMessage("invalidUser:uname", fmsg);
				//return "login?faces-redirect=true";
				System.out.println("Print this if user is invalid");
			}
		}
		 catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} 
//		finally {
//			try {
//				con.close();
//			} catch (SQLException e) {
//			}
//		}
		return null;
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		System.out.println("This is login page after logout button is clicked");
		return "login?faces-redirect=true";
	}
	
	public String checkStatus() {
		userDAO u2= new userDAO();
		List<String> l =  u2.checkMStatus("M");
		// System.out.println(mStatus   + "is the status");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("l",l);
		return "adminHome?faces-redirect=true";
	}
}
