package com.icia.manager.model;

import java.io.Serializable;

public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	//모델에 있는 애들 이름은 보통 테이블이름과 동일하게 만듦
	//모델 패키지 밑에 있는 것들은 전부 테이블과 1:1임!!
	
	//인스턴스 변수
	private String userId;
	private String userPwd;
	private String userName;
	private String userEmail;
	private String status;
	private String regDate;
	
	private int startRow;       //시작 rownum
	private int endRow;       //끝 rownum
	
	
	//생성자
	public User()
	{
		userId ="";
		userPwd ="";
		userName ="";
		userEmail ="";
		status ="";
		regDate ="";
		
		startRow = 0;
		endRow = 0;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserPwd() {
		return userPwd;
	}


	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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


	public int getStartRow() {
		return startRow;
	}


	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}


	public int getEndRow() {
		return endRow;
	}


	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
	
	
}
