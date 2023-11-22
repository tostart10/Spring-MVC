package com.icia.manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icia.manager.dao.AdminDao;
import com.icia.manager.model.Admin;

//처음 서비스 만들떄 어노테이션 서비스 해줘야함 잊지마!!
@Service("daminService")
public class AdminService
{
	//로그 찍어야되니까 로깅 정의 해줘야함
	private static Logger logger = LoggerFactory.getLogger(AdminService.class);
	
	@Autowired
	private AdminDao adminDao;
	
	//관리자 조회
	public Admin adminSelect(String admId)
	{
		Admin admin = null;
		
		try
		{
			//DAO호출
			admin = adminDao.adminSelect(admId);
		}
		catch(Exception e)
		{
			logger.error("[AdminService] adminSelect Exception", e);
		}
		
		return admin;
	}
	
	
}
