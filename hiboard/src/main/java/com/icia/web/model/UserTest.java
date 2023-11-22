/**
 * <pre>
 * 프로젝트명 : BasicBoard
 * 패키지명   : com.icia.web.model
 * 파일명     : UserTest.java
 * 작성일     : 2021. 1. 12.
 * 작성자     : daekk
 * </pre>
 */
package com.icia.web.model;

import java.io.Serializable;

/**
 * <pre>
 * 패키지명   : com.icia.web.model
 * 파일명     : UserTest.java
 * 작성일     : 2021. 1. 12.
 * 작성자     : daekk
 * 설명       : 사용자 모델
 * </pre>
 */
public class UserTest implements Serializable
{
   private static final long serialVersionUID = 8638989512396268543L;
   
   private String userId;      //사용자 아이디
   private String userPwd;      //비밀번호
   private String userName;   //사용자 명
   private String userEmail;   //사용자 이메일
   private String userHobby;      //취미
   private String status;      //상태 ("Y":사용, "N":정지)
   private String regDate;      //등록일
   /**
    * 생성자 
    */
   public UserTest()
   {
      userId = "";
      userPwd = "";
      userName = "";
      userEmail = "";
      userHobby = "";
      status = "";
      regDate = "";
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
   public String getUserHobby() {
      return userHobby;
   }
   public void setUserHobby(String userHobby) {
      this.userHobby = userHobby;
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