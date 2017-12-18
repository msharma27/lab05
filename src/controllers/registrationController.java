/**
 * 
 */
package controllers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import DAO.*;
import POJO.*;

// import POJO.User;

/**
 * @author mshar
 *
 */
@ManagedBean(name="registrationController")
@SessionScoped
public class registrationController {
	private String fname;
	private String lname;
	private String email;
	private String phone;
	private String usertype;
	private String uname;
	private String password;
	private int fees;
	private int status;
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getFees() {
		return fees;
	}
	public void setFees(int fees) {
		this.fees = fees;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
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
	
	public registrationController() {
		
	}
	userDAO u=new userDAO();
	public String registerUser() {
		String usertype = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("hidden1");
		String status = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("hidden2");
		System.out.println(usertype+" is the type of user");
		boolean b=u.userExist(this.uname);
		
		if(b==true)
		{
			System.out.println("User already exist1!!!\n");
			// message on front end
			FacesContext fc= FacesContext.getCurrentInstance();
			FacesMessage fmsg = new FacesMessage("User already exists");
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User already exista"));
			fc.addMessage("invalidUsername:uname", fmsg);
			//System.out.println("User already exists. Please Register with different details! ");
		}
		else {
			u.register(this.fname,this.lname,this.email,this.phone,usertype, this.fees, this.uname,this.password,status);	
			u.managerfees(this.fees, this.uname, usertype);
		return "login";
	}
	return null;
	}
	
	public void fees() {
		userDAO u1=new userDAO();
		u1.managerfees(this.fees, this.uname, usertype);
	}
	
	
}
