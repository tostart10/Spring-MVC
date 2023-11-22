package com.icia.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;

@Repository("hiBoardDao")
public interface HiBoardDao
{
	//xml 수정하면 dao.java도 수정(추가) 해줘야함
	
	//게시물 등록
	public int boardInsert(HiBoard hiboard);
	
	//게시물 첨부파일 등록
	public int boardFileInsert(HiBoardFile hiBoardFile);
	
	//게시물 리스트
	public List<HiBoard> boardList(HiBoard hiBoard);
	
	//총 게시물 수
	public long boardListCount(HiBoard hiBoard);
	
	//게시물 조회
	public HiBoard boardSelect(long hiBbsSeq);
	
	//게시물 첨부파일 조회
	public HiBoardFile boardFileSelect(long hiBbsSeq);
	
	//게시물 조회수 증가
	public int boardReadCntPlus(long hiBbsSeq);
	//boardReadCntPlus에 대한 서비스는 구현하지 않을 거임
	
	
	//게시물 그룹 순서 변경
	public int boardGroupOrderUpdate(HiBoard hiBoard); //얘랑
	
	//게시물 답글 등록
	public int boardReplyInsert(HiBoard hiBoard);  //애는 서비스 다로 구현 안함 -> 트렌잿션 하려고
	
	//게시믈 수정
	public int boardUpdate(HiBoard hiBoard);
	
	//게시물 첨부파일 삭제
	public int boardFileDelete(long hiBbsSeq);
	
	
	//게시물 삭제
	public int boardDelete(long hiBbsSeq);
	
	//게시물 삭제시 답변글수 조회
	public int boardAnswersCount(long hiBbsSeq);  //hiBbsSeq (삭제하려는 게시물의 게시물 번호가 부모번호로 쓰이고 잇는 것이 있는지 확인)
	
	
}
