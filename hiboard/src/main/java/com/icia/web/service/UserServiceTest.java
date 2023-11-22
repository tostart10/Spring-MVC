package com.icia.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icia.web.dao.UserDaoTest;
import com.icia.web.model.UserTest;


@Service("userServiceTest")
public class UserServiceTest 
{
   private static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
   
   @Autowired
   private UserDaoTest userDaoTest;
   
   public UserTest userSelectTest(String userId)
   {
      UserTest user = null;
      
      try
      {
         user = userDaoTest.userSelectTest(userId);
      }
      catch(Exception e)
      {
         logger.error("[UserService] userSelect Exception", e);
      }
      
      return user;
   }

   
 //사용자 등록
   public int userInsertTest(UserTest user)
   {
      int count = 0;
      
      try
      {
         count = userDaoTest.userInsertTest(user);
      }
      catch(Exception e)
      {
         logger.error("[UserService]userInsertTest Exception" , e);
      }
      
      
      return count;
   }
   
}