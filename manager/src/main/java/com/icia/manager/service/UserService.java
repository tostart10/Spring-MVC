package com.icia.manager.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.icia.manager.dao.UserDao;
import com.icia.manager.model.User;



@Service("userService")
public class UserService 
{
	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	//사용자 리스트
	
	public List<User> userList(User user)
	{
		List<User> list = null;
		
		try
		{
			list = userDao.userList(user);
		}
		catch(Exception e)
		{
			logger.error("[UserService] userList Exception", e);
		}
		
		return list;
	}
	
	
	//사용자수 조회 userlistCount
	public int userListCount(User user)
	{
		int count = 0;
		
		try
		{
			count = userDao.userListCount(user);
		}
		catch(Exception e)
		{
			logger.error("[UserService] userListCount Exception", e);
		}
		
		return count;
	}
	
	
	
	//사용자 조회
	public User userSelect(String userId)
	{
		User user = null;
		
		//유일하게 서비스에서 try-catch 하지 않은것 -> 트렌젝셔널
		
		//Dao호출할꺼니까 try-catch 해줘야함 -> dao.java에서 @Repository를 하는 과정에서 예외가 발생 -> 예외가 발생하는 것을 호출하는 곳에서 처리해줘야함 -> 그래서
		//Dao.java를 호출하는 서비스에서 try-catch 해준거임
		try
		{
			user= userDao.userSelect(userId);
		}
		catch(Exception e)
		{
			logger.error("[UserService] userSelect Exception", e);
		}
		return user;
	}
	
	
	//사용자 수정
	public int userUpdate(User user)
	{
		int count = 0;
		
		try
		{
			count = userDao.userUpdate(user);
		}
		catch(Exception e)
		{
			logger.error("[UserService] userUpdate Exception ", e);
		}
		
		return count;
	}
	
	
}
