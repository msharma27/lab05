package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import DAO.userDAO;
import POJO.userPOJO;
@ManagedBean
@SessionScoped 
public class managerListController { 
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{select.fname}")
	private String fname;
	private String l1;

	
public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

public String getL1() {
		return l1;
	}

	public void setL1(String l1) {
		this.l1 = l1;
	}

public managerListController() {
	
}

	public String getList() {
		
	// CHECK IF CLIENT UNAME DOES NOT EXIST IN MANAGER TABLE
	userDAO u = new userDAO();
	String clientUname = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUname");	
	System.out.println(clientUname);
	Boolean bool = u.clientCheck(clientUname);
	System.out.println(bool +  " the value for boolean.. if true: user has manager");
	if(bool) {
		// System.out.println("You are already assigned a manager");
		FacesContext fc= FacesContext.getCurrentInstance();
		FacesMessage fmsg = new FacesMessage("You are already assigned a manager");
		fc.addMessage("managerExists:fname", fmsg);
		return "userHome?faces-redirect=true";
	}
	else {
		List<userPOJO> userpojo = u.getMList("M");
		// System.out.println(l1 + " is the list ");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userpojo",userpojo);
		// System.out.println(l1  + "is the list");
		return "managerClient?faces-redirect=true";
	}
}
	
	public void select(String uname) {
		System.out.println(uname + " from list ");
		String clientUname = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUname");
		System.out.println(clientUname + "from select method");
		userDAO u1 = new userDAO();
		u1.managerClient(uname, clientUname);
		
	}
}


