package com.icia.web.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.icia.common.util.FileUtil;
import com.icia.web.dao.HiBoardDao;
import com.icia.web.model.HiBoard;
import com.icia.web.model.HiBoardFile;

@Service("hiBoardService")
public class HiBoardService 
{
   private static Logger logger = LoggerFactory.getLogger(HiBoardService.class);
   
 //파일 저장 경로
 	@Value("#{env['upload.save.dir']}")
 	private String UPLOAD_SAVE_DIR;//짝꿍 맺어줌, 업로드 저장 디렉토리임
   
   @Autowired
   private HiBoardDao hiBoardDao;
   
   
   //@Transactional는 오라클에서 커밋,롤백개념
   
   //게시판 등록
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardInsert(HiBoard hiBoard) throws Exception
   {	//Propagation.REQUIRED: 트렌젝션이 있으면 그 트렌젝션에서 실행
	   //없으면 새로운 트랜젝션을 실행(기본설정)
	   ///트렌젝션은 일의 끝나는점을 정하는것
      int count = 0;
      
      count = hiBoardDao.boardInsert(hiBoard);
      
      if(count > 0 && hiBoard.getHiBoardFile() != null)
      {
    	  HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();  //HiBoard 객체의 주소를 HiBoardFile에 대입 바라본다 -> 대입했으니까 같은곳 바라봄
    	  hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
    	  hiBoardFile.setFileSeq((short)1);  //원래는 hiBoardFile의 setHiBbsSeq의MAX값을 구해서 +1을 해야함
    	  //게시물 하나에 첨부파일 하나만 넣을거라서 ((short)1)로 해준거임
    	  
    	  /*위 로직을 아래와 같이 변경가능
    	   * 위는 객체 생성이 아님!!! 왜?  => new 가 아니까
    	   * HiBoardFile형의 참조형 변수 hiBoardFile만 생성해서 hiBoard.getHiBoardFile();의 주소값만 대입해서 같은 곳을 바라보게 만든것임
    	   * hiboard.getHIBoardFile().setHiBbsSeq(hiBoard.getHiBbsSeq());
    	   * hiboard.getHIBoardFile().setFileSeq((short)1);
    	   */
    	  
    	  hiBoardDao.boardFileInsert(hiBoardFile);
    	  
    	  //이렇게 변경가능
    	  // hiBoardDao.boardFileInsert(getHiBoardFile())
      }
      
      
      return count;  //건수만 만 넘어감 null일경우 if문 안타서
   }
   
   
   //게시물 리스트
   //추상메서드를 콜함
   public List<HiBoard> boardList(HiBoard hiBoard)
   {
	   List<HiBoard> list = null;
	   
	   
	   //개발에서 비즈니스 로직 = 쿼리문
	   //비즈니스로직은 주로 service에서 해주고 있음
	   //MVC왜 씀?? => 유지보수를 쉽게하기 위해서
	   try //Dao에서 Repository Annotation 떄문에 오류날수 있기 떄문에
	   {
		   list = hiBoardDao.boardList(hiBoard);
		   
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardList Exception", e);
	   }
	   return list;
   }
   
   
   
   //총 게시물 수 (조회)
   public long boardListCount(HiBoard hiBoard)
   {
	   long count = 0;
	   
	   try 
	   {
		   count = hiBoardDao.boardListCount(hiBoard);
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardListCount Exception", e);
	   }
	   
	   return count;
   }
   
   
   //게시물 조회
   public HiBoard boardSelect(long hiBbsSeq)
   {
	   HiBoard hiBoard = null;
	   
	   try
	   {
		   hiBoard = hiBoardDao.boardSelect(hiBbsSeq);
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardSelect Exception", e);
	   }
	   
	   return hiBoard;
   }
   
   
   
   //첨부파일 조회
   public HiBoardFile boardFileSelect(long hiBbsSeq)
   {
	   HiBoardFile hiBoardFile = null;
	   
	   try 
	   {
		   hiBoardFile = hiBoardDao.boardFileSelect(hiBbsSeq);
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardFileSelect Exception", e);
	   }
	   
	   return hiBoardFile;
   }
   
   
   //게시물 보기(첨부파일 포함, 조회수 증가 포함)
   public HiBoard boardView(long hiBbsSeq)
   {
	   HiBoard hiBoard =null;
	   
	   try 
	   {
		   //서비스에서 서비스 호출할 필요없이 바로 Dao를 호출한다
		   hiBoard = hiBoardDao.boardSelect(hiBbsSeq);
		   
		   if(hiBoard != null)
		   {
			   //조회수 증가
			   hiBoardDao.boardReadCntPlus(hiBbsSeq); //처리 안해줘서 건수 안받을 거임 (xml에 쿼리 있음)
			   
			   HiBoardFile hiBoardFile = hiBoardDao.boardFileSelect(hiBbsSeq);
			   
			   if(hiBoardFile != null)
			   {
				   hiBoard.setHiBoardFile(hiBoardFile); //주소값 대입
			   }   
		   }
		   //여기 원래 else없는 거임 걱정 ㄴㄴ
		   
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardView Exception", e);
	   }
	   
	   return hiBoard;
   }
   
   
   //게시물 답글 등록
   @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
   public int boardReplyInsert(HiBoard hiBoard) throws Exception
   {
	   int count = 0;
	   
	   //update먼저 해주고 insert
	   hiBoardDao.boardGroupOrderUpdate(hiBoard);
	   
	   count = hiBoardDao.boardReplyInsert(hiBoard); //답글에 대한 인서트
	   
	   //게시물 정상등록 되고 나면 첨부파일이 존재하면 첨부파일도 등록
	   if(count > 0 && hiBoard.getHiBoardFile() != null)
	   {
		   //서비스에서 seq랑 설정
		   HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
		   hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
		   hiBoardFile.setFileSeq((short)1);
		   
		   hiBoardDao.boardFileInsert(hiBoardFile); //같은 주소이기 때문에 ()에 hiBoard.getHiBoardFile() 해도 같은거임
		   
	   }
	   
	   
	   return count;		   
   }
   
   
   
   //게시물 수정폼 조회(첨부파일 포함)
   //컨트롤러 boardView는 조회수 때문에 사용할 수 없어서
   public HiBoard boardViewUpdate(long hiBbsSeq)
   {
	   HiBoard hiBoard = null;
	   
	   
	   //DAo.java 호출하려면 어노테이션 레파지토리 때문에 해줘야함
	   //Dao.java는 인터페이스여서 @Repository를 하는 과정에서 예외가 발생하므로, Service를 호출하는 쪽인 Controller에서 예외처리를 해줘야함
	   //게시물 있는지, 게시물이 있다면 게시물에 첨부파일이 있는지 확인 해줘야함
	   try 
	   {
		   hiBoard = hiBoardDao.boardSelect(hiBbsSeq);  
		   
		   if(hiBoard != null)
		   {
			   HiBoardFile hiBoardFile = hiBoardDao.boardFileSelect(hiBbsSeq);
			   
			   if(hiBoardFile != null)
			   {
				   hiBoard.setHiBoardFile(hiBoardFile);
			   }
		   }
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardViewUpdate Exception", e);
	   }
	   
	   return hiBoard;
   }
   
   
   
   //게시물 수정
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public int boardUpdate(HiBoard hiBoard) throws Exception
   {
	   int count = hiBoardDao.boardUpdate(hiBoard);
	   
	   //게시물 수정하면 첨부파일은 기존에 잇는거 지우고 새로 올라감
	   
	   if(count > 0 && hiBoard.getHiBoardFile() != null)
	   {
		   //파일정보 읽어오기
		   HiBoardFile delHiBoardFile = hiBoardDao.boardFileSelect(hiBoard.getHiBbsSeq());
		   
		   //기존파일이있으면 삭제 
		   if(delHiBoardFile != null)
		   {
			   //첨부파일있으면 삭제
			   FileUtil.deleteFile(UPLOAD_SAVE_DIR + FileUtil.getFileSeparator() + delHiBoardFile.getFileName());  //delHiBoardFile.getFileName() 하면 파일 삭제됨
			   //어느디렉토리의 어떤 파일을 지울거니?
			   //getFileSeparator는 현재 OS한테 알려줌?요청?  윈도우는\, 맥은/ (디렉토리 나타내는 코드 가져옴)
			   //delHiBoardFile.getFileName()는 파일 == UUID값
			   
			   //테이블도 삭제해줘야함
			   hiBoardDao.boardFileDelete(hiBoard.getHiBbsSeq());
			   
		   }
		   
		 //hiBoardFile다시 생성
		   HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
		   hiBoardFile.setHiBbsSeq(hiBoard.getHiBbsSeq());
		   hiBoardFile.setFileSeq((short)1);
		   
		   //hiBoardFile다시 생성
		   hiBoardDao.boardFileInsert(hiBoard.getHiBoardFile());
		   
		   
		   
		   
	   }
	   
	   
	   return count;
   }
   
   
   
   
   //게시물 삭제 시 답변 글 수 조회
   public int boardAnswersCount(long hiBbsSeq)
   {
	   int count = 0;
	   
	   try
	   {
		   count = hiBoardDao.boardAnswersCount(hiBbsSeq);
	   }
	   catch(Exception e)
	   {
		   logger.error("[HiBoardService] boardAnswersCount Exception", e);
	   }
	   
	   return count;
   }
   
   
   
 //게시물 삭제(첨부파일이 있으면 함께 삭제)
 	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
 	public int boardDelete(long hiBbsSeq) throws Exception
 	{
 		int count = 0;
 		
 		HiBoard hiBoard = boardViewUpdate(hiBbsSeq);
 		//이거 순서
 		//1. HiBoard 방을 만듦
 		//2. boardViewUpdate() 메서드실행
 		//3. 메서드실행 결과를 담은 주소값을 참조형 변수 hiBoard에 대입
 		
 		//있을 때만 진행
 		if(hiBoard != null)
 		{
 			count = hiBoardDao.boardDelete(hiBbsSeq);   //HIBoard 테이블 에서 삭제
 			
 			if(count > 0)
 			{
 				HiBoardFile hiBoardFile = hiBoard.getHiBoardFile();
 				
 				if(hiBoardFile != null)//첨부파일 존재함
 				{
 					
 					if(hiBoardDao.boardFileDelete(hiBbsSeq) > 0)  //HiBoardFile 테이블에서 삭제
 					{
 						//첨부파일 삭제
 						FileUtil.deleteFile(UPLOAD_SAVE_DIR + FileUtil.getFileSeparator() + hiBoardFile.getFileName());
 						//UPLOAD_SAVE_DIR: 경로
 						//FileUtil.getFileSeparator() : 운영체제에 따른 디렉토리 (윈도우는 \,  리눅스+맥 : / )
 						//
 					}
 				}
 			}
 		}
 		
 		return count;
 	}
   
}