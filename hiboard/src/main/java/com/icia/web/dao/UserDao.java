/**
 * <pre>
 * 프로젝트명 : HiBoard
 * 패키지명   : com.icia.web.dao
 * 파일명     : UserDao.java
 * 작성일     : 2021. 1. 19.
 * 작성자     : daekk
 * </pre>
 */
package com.icia.web.dao;

import org.springframework.stereotype.Repository;

import com.icia.web.model.User;

/**
 * <pre>
 * 패키지명   : com.icia.web.dao
 * 파일명     : UserDao.java
 * 작성일     : 2021. 1. 19.
 * 작성자     : daekk
 * 설명       : 사용자 DAO
 * </pre>
 */
@Repository("userDao")   //DB연결시 예외발생함
public interface UserDao
{
	
	/**
	 * <pre>
	 * 메소드명   : userSelect
	 * 작성일     : 2021. 1. 12.
	 * 작성자     : daekk
	 * 설명       : 사용자 조회
	 * </pre>
	 * @param userId 사용자 아이디
	 * @return  com.icia.web.model.User
	 */
	public User userSelect(String userId);
	
	//사용자 등록
	public int userInsert(User user);
	
	//사용자 정보 수정
	public int userUpdate(User user);  //USer객체를 매개변수로 넘김, 리턴타입은 int  - 매개변수 타입과 린턴타입에 대한 정보가 있어야 MyBatis에서 조합을 해줄수 있으므로 명시해야함 +자바문법임
	//여기서 
	
	//Dao.java 와 .xml 은 짝꿍임!!! 여기 수정하면 .xml도 수정해주기!
	
	
	
	
}
