package com.icia.manager.dao;

import org.springframework.stereotype.Repository;

import com.icia.manager.model.Admin;

//DAO는 무조건 @Repository해야함!! 까먹지마

@Repository("adminDao")
public interface AdminDao 
{
	//관리자 조회
	public Admin adminSelect(String admId);  //select이니까 관리자이름(string)을 매개변수로 받고 select결과를 resultMAp으로 객체타입으로 리턴한다
	
	
}
