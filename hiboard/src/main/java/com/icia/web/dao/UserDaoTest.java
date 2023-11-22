package com.icia.web.dao;

import org.springframework.stereotype.Repository;

import com.icia.web.model.UserTest;

@Repository("userDaoTest")
public interface UserDaoTest
{

   public UserTest userSelectTest(String userId);

   public int userInsertTest(UserTest user);
   
}  