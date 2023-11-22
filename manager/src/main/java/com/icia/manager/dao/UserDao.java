package com.icia.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.icia.manager.model.User;

//인터페이스는 꼭 @Repository 정의 해야함!! 잊지마!!
@Repository("userDao")
public interface UserDao 
{
	//사용자 리스트
	public List<User> userList(User user);
	
	public int userListCount(User user);
	
	//사용자 조회
	public User userSelect(String userId); //관리자가 검색하는 특정 아이디를 입력받는것이므로 매개변수가 userID
	
	//사용자 수정
	public int userUpdate(User user);
}
