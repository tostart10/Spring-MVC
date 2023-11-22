package com.icia.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.icia.common.util.StringUtil;
import com.icia.web.model.Response;
import com.icia.web.model.User;
import com.icia.web.model.UserTest;
import com.icia.web.service.UserServiceTest;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

@Controller("userLoginTestController")
public class UserLoginTestController {
    private static Logger logger = LoggerFactory.getLogger(UserLoginTestController.class);
    
    @Value("#{env['auth.cookie.name']}")
      private String AUTH_COOKIE_NAME;      //USER_ID
      
      @Autowired
      private UserServiceTest userServiceTest;
      
     @RequestMapping(value="/loginPage/userLogin", method=RequestMethod.POST)
     @ResponseBody
     public Response<Object> login(HttpServletRequest request, HttpServletResponse response)
     {
        String userId = HttpUtil.get(request, "userId");
         String userPwd = HttpUtil.get(request, "userPwd");
         Response<Object> ajaxResponse = new Response<Object>();
         
        logger.debug("111111111111111111111111111111111111111111111");
        logger.debug("userId : " + userId);
        logger.debug("userPwd : " + userPwd);
        logger.debug("111111111111111111111111111111111111111111111");
         
         if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd))
         {
            UserTest user = userServiceTest.userSelectTest(userId);
            
            if(user != null)
            {
               if(StringUtil.equals(user.getUserPwd(), userPwd))
               {
                  // AUTH_COOKIE_NAME: 변수명, CookieUtil.stringToHex(userId):값
                  CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(userId));
                  
                  ajaxResponse.setResponse(0, "Success"); // 로그인 성공
               }
               else
               {
                  ajaxResponse.setResponse(-1, "Passwords do not match"); // 비밀번호 불일치
               }
            }
            else
            {
               ajaxResponse.setResponse(404, "Not Found"); // 사용자 정보 없음 (Not Found)
            }
         }
         else
         {
            ajaxResponse.setResponse(400, "Bad Request"); // 파라미터가 올바르지 않음 (Bad Request)
         }
         
         if(logger.isDebugEnabled())
         {
            logger.debug("[UserController] /login_page/user_login\n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
         
         return ajaxResponse;
         
     }
     @RequestMapping(value="/loginPage/userLoginout", method=RequestMethod.GET)     //로그아웃
        public String loginOut(HttpServletRequest request, HttpServletResponse response)
       {
          if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
          {
             CookieUtil.deleteCookie(request, response,"/", AUTH_COOKIE_NAME);
          }
          
          return "redirect:/";   //재접속 명령 (URL을 다시 가르킴)
       }
     
     @RequestMapping(value="/loginPage/regFormTest", method=RequestMethod.GET)  //회원가입
      public String regFormTest(HttpServletRequest request, HttpServletResponse response)   
      {
         String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
         
         if(!StringUtil.isEmpty(cookieUserId)) //로그인이 되어있을때
         {
            CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
            
            return "redirect:/";
         }
         else
         {
            return "/loginPage/regFormTest";
         }
         
      }
     
     
   //회원 등록
      @RequestMapping(value="/loginPage/regProcTest", method=RequestMethod.POST)
      @ResponseBody
      public Response<Object> regProcTest(HttpServletRequest request, HttpServletResponse response)
      {
         Response<Object> ajaxResponse = new Response<Object>();
         
         String userId = HttpUtil.get(request, "userId");
         String userPwd = HttpUtil.get(request, "userPwd");
         String userName = HttpUtil.get(request, "userName");
         String userEmail = HttpUtil.get(request, "userEmail");
         String userHobby = HttpUtil.get(request, "userHobby");
         
         if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd) && 
            !StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail) && !StringUtil.isEmpty(userHobby))
         {
            if(userServiceTest.userSelectTest(userId) == null)
            {
               //중복아이디가 없을 경우 정상적으로 등록
               UserTest user = new UserTest();
               
               user.setUserId(userId);
               user.setUserPwd(userPwd);
               user.setUserName(userName);
               user.setUserEmail(userEmail);
               user.setUserHobby(userHobby);
               user.setStatus("Y");
               
               if(userServiceTest.userInsertTest(user) > 0)
               {
                  ajaxResponse.setResponse(0, "Success");
               }
               else
               {
                  ajaxResponse.setResponse(500, "Internal Server Error");
               }
            }
            else
            {
               //중복아이디가 있는 경우
               ajaxResponse.setResponse(100, "Duplicate ID");
            }
         }   
         else
         {
            ajaxResponse.setResponse(400, "Bad Request");
         }
         
         if(logger.isDebugEnabled())
         {
            logger.debug("[UserDao]/user/regProcTest response\n" + JsonUtil.toJsonPretty(ajaxResponse));
         }
         
         return ajaxResponse;
      }
      
      @RequestMapping(value="/loginPage/idCheckTest",method=RequestMethod.POST)
       @ResponseBody
       public Response<Object> idCheck(HttpServletRequest request, HttpServletResponse response)
       {
          Response<Object> ajaxResponse = new Response<Object>();
          String userId = HttpUtil.get(request, "userId");
          
          if(!StringUtil.isEmpty(userId))
          {
             if(userServiceTest.userSelectTest(userId) ==    null)
             {
                ajaxResponse.setResponse(0,"Success");
             }
             else
             {
                ajaxResponse.setResponse(100,"Deplicate ID");
             }
          }
          else
          {
             ajaxResponse.setResponse(400,"Bad Request");
          }
          
          if(logger.isDebugEnabled())
          {
             logger.debug("[UserController]/user/idCheck response\n" + JsonUtil.toJsonPretty(ajaxResponse));
          }
          
          
          return ajaxResponse;
       }
      
     //서비스에서 인터테이스(dao)+ .xml를 호출하게 되었음
      
      //톰켓들어오자마자 제일 앞에 잇는 거 = 필터(UTF-8)
      //필터 -> dispacherServelt -> 인터셉터(컨트롤러 호출하기 전)
     
      //게시판 테이블과 게시판 첨부파일 테이블을 나눌꺼임
}