package com.icia.manager.model;

import java.io.Serializable;

public class Admin implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String admId;
	private String admPwd;
	private String admName;
	private String status;
	private String regDate;
	
	
	//생성자
	public Admin()
	{
		admId = "";
		admPwd = "";
		admName = "";
		status = "";
		regDate = "";
		
	}


	public String getAdmId() {
		return admId;
	}


	public void setAdmId(String admId) {
		this.admId = admId;
	}


	public String getAdmPwd() {
		return admPwd;
	}


	public void setAdmPwd(String admPwd) {
		this.admPwd = admPwd;
	}


	public String getAdmName() {
		return admName;
	}


	public void setAdmName(String admName) {
		this.admName = admName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRegDate() {
		return regDate;
	}


	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	
	
}
