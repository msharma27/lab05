package controllers;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import DAO.userDAO;
import POJO.userPOJO;

@ManagedBean
@SessionScoped 
public class profileController {
	private String sessionUname;
	private String sfname;
	private String slname;
	private String semail;
	private String sphone;
	private String spass;
	
	
public String getSfname() {
		return sfname;
	}
	public void setSfname(String sfname) {
		this.sfname = sfname;
	}
	public String getSlname() {
		return slname;
	}
	public void setSlname(String slname) {
		this.slname = slname;
	}
	public String getSemail() {
		return semail;
	}
	public void setSemail(String semail) {
		this.semail = semail;
	}
	public String getSphone() {
		return sphone;
	}
	public void setSphone(String sphone) {
		this.sphone = sphone;
	}
	public String getSpass() {
		return spass;
	}
	public void setSpass(String spass) {
		this.spass = spass;
	}
public String getSessionUname() {
		return sessionUname;
	}
	public void setSessionUname(String sessionUname) {
		this.sessionUname = sessionUname;
	}
	
public profileController() {
}	
    userDAO u2= new userDAO();
    userPOJO p2= new userPOJO();
    
	
    public String getProfile() {
		// HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String sUname = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUname");
		// System.out.println(sUname + " is the session uname from profile controller ");

		p2 = u2.getProfileInfo(sUname);
		this.sfname= p2.getFname();
		this.slname= p2.getLname();
		this.semail= p2.getEmail();
		this.sphone= p2.getPhone();
		this.spass= p2.getPassword();
		// System.out.println(p2.getEmail() + " is email from pojo ");
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sfname",sfname);
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("slname",slname);
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("semail",semail);
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sphone",sphone);
//		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("spass",spass);
		System.out.println(sfname+" sfname from login after clicking submit");
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session",session.fname);
		return "userProfile?faces-redirect=true"; 
	}
	
	public String updateProfile() {
		String s= getSfname();
		System.out.println(s +" ashasdasdasjdajsd");
		String sUname1 = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUname");
//		System.out.println(sUname1 + " is the session uname from profile controller- update method");
//		System.out.println(sfname + " is the sfname from profile controller- update ");
		u2.updateProfileInfo(sUname1, getSfname(), getSlname(), getSemail(), getSphone(), getSpass());
		return "userProfile?faces-redirect=true"; 
	}
}

