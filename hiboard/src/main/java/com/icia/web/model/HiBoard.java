package com.icia.web.model;

import java.io.Serializable;

public class HiBoard implements Serializable
{
	private static final long serialVersionUID = 1L ;
	
	//게시판 등록 위해서 만든 클래스
	
	//인스턴스 변수
	private long hiBbsSeq;				//게시물 번호
	private String userId;				//사용자 아이디
	private long hiBbsGroup;			//게시물 그룹번호
	private int hiBbsOrder;				//게시물 그룹내 순서
	private int hiBbsIndent;			//게시물 들여쓰기
	private String hiBbsTitle;			//게시물 제목
	private String hiBbsContent;		//게시물 내용
	private int hiBbsReadCnt;			//게시물 조회수
	private long hiBbsParent;			//부모 게시물 번호
	private String regDate;				//등록일
	
	private String userName;			//사용자 이름
	private String userEmail;			//사용자 이메일
	
	private long startRow;				//시작 rownum
	private long endRow;				//끝 rownum
	
	private String searchType;			//검색타입(1:이름, 2:제목, 3:내용)
	private String searchValue; 		//검색값
	
	private HiBoardFile hiBoardFile;		//첨부 파일
	
	
	
	//디폴트 생성자
	public HiBoard()
	{
		hiBbsSeq = 0;
		userId = "";		
		hiBbsGroup = 0;
		hiBbsOrder = 0;			//게시물의 hiBbsOrder,hiBbsIndent, hiBbsParent 는 0이어야함
		hiBbsIndent = 0;
		hiBbsTitle = "";
		hiBbsContent = "";
		hiBbsReadCnt = 0;
		hiBbsParent = 0;
		regDate = "";
		
		userName = "";
		userEmail = "";
		
		startRow =0;
		endRow=0;
		
		searchType ="";
		searchValue="";
		
		
		hiBoardFile = null;  //객체라서 defualt로 null
		
		
	}



	public long getStartRow() {
		return startRow;
	}



	public void setStartRow(long startRow) {
		this.startRow = startRow;
	}



	public long getEndRow() {
		return endRow;
	}



	public void setEndRow(long endRow) {
		this.endRow = endRow;
	}



	public String getSearchType() {
		return searchType;
	}



	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}



	public String getSearchValue() {
		return searchValue;
	}



	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}



	public long getHiBbsSeq() {
		return hiBbsSeq;
	}



	public void setHiBbsSeq(long hiBbsSeq) {
		this.hiBbsSeq = hiBbsSeq;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public long getHiBbsGroup() {
		return hiBbsGroup;
	}



	public void setHiBbsGroup(long hiBbsGroup) {
		this.hiBbsGroup = hiBbsGroup;
	}



	public int getHiBbsOrder() {
		return hiBbsOrder;
	}



	public void setHiBbsOrder(int hiBbsOrder) {
		this.hiBbsOrder = hiBbsOrder;
	}



	public int getHiBbsIndent() {
		return hiBbsIndent;
	}



	public void setHiBbsIndent(int hiBbsIndent) {
		this.hiBbsIndent = hiBbsIndent;
	}



	public String getHiBbsTitle() {
		return hiBbsTitle;
	}



	public void setHiBbsTitle(String hiBbsTitle) {
		this.hiBbsTitle = hiBbsTitle;
	}



	public String getHiBbsContent() {
		return hiBbsContent;
	}



	public void setHiBbsContent(String hiBbsContent) {
		this.hiBbsContent = hiBbsContent;
	}



	public int getHiBbsReadCnt() {
		return hiBbsReadCnt;
	}



	public void setHiBbsReadCnt(int hiBbsReadCnt) {
		this.hiBbsReadCnt = hiBbsReadCnt;
	}



	public long getHiBbsParent() {
		return hiBbsParent;
	}



	public void setHiBbsParent(long hiBbsParent) {
		this.hiBbsParent = hiBbsParent;
	}



	public String getRegDate() {
		return regDate;
	}



	public void setRegDate(String regDate) {
		this.regDate = regDate;
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



	public HiBoardFile getHiBoardFile() {
		return hiBoardFile;
	}



	public void setHiBoardFile(HiBoardFile hiBoardFile) {
		this.hiBoardFile = hiBoardFile;
	}
	
	
	
	
	
}
