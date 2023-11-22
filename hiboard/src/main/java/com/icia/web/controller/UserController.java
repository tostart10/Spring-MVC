/**
 * <pre>
 * 프로젝트명 : HiBoard
 * 패키지명   : com.icia.web.controller
 * 파일명     : UserController.java
 * 작성일     : 2021. 1. 20.
 * 작성자     : daekk
 * </pre>
 */
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
import com.icia.web.service.UserService;
import com.icia.web.util.CookieUtil;
import com.icia.web.util.HttpUtil;
import com.icia.web.util.JsonUtil;

/**
 * <pre>
 * 패키지명   : com.icia.web.controller
 * 파일명     : UserController.java
 * 작성일     : 2021. 1. 20.
 * 작성자     : daekk
 * 설명       : 사용자 컨트롤러
 * </pre>
 */
@Controller("userController")
public class UserController
{
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	// 쿠키명
	@Value("#{env['auth.cookie.name']}")
	private String AUTH_COOKIE_NAME;     //USER_ID 랑 같은거임
	
	@Autowired
	private UserService userService;
	
	/**
	 * <pre>
	 * 메소드명   : login
	 * 작성일     : 2021. 1. 21.
	 * 작성자     : daekk
	 * 설명       : 로그인 
	 * </pre>
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return Response<Object>
	 */
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> login(HttpServletRequest request, HttpServletResponse response)
	{
		String userId = HttpUtil.get(request, "userId");
		String userPwd = HttpUtil.get(request, "userPwd");
		Response<Object> ajaxResponse = new Response<Object>();
		
		if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd))
		{
			User user = userService.userSelect(userId);
			
			if(user != null)
			{
				if(StringUtil.equals(user.getUserPwd(), userPwd))
				{
					CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(userId)); // "/"는 root밑에부터 다 적용해 라는 뜻
					// "/" 은 최상의 디렉토리 여서 최상위부터 전부다 적용해라 라는 뜻
					//-1는 사용하는 동안만 쿠키 유효 (브라우저가 살아있는 동안만 쿠키 유효)
					//AUTH_COOKIE_NAME (얘는 변수명)-> @Value("#{env['auth.cookie.name']}")때문에 env.xml에 있는 auth.cookie.name의 값인 userId값이 된다
					//값은 CookieUtil.stringToHex(userId)임  -> 받은값인 userId를 16진수로 바꿔줌 
					//로그인 =쿠키 생성, 로그아웃=쿠키삭제
					
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
			logger.debug("[UserController] /user/login response\n" + JsonUtil.toJsonPretty(ajaxResponse));
		}
		
		return ajaxResponse;
	}
	
	
	//로그아웃 메서드
	@RequestMapping(value="/user/loginOut", method=RequestMethod.GET)
	public String loginOut(HttpServletRequest request, HttpServletResponse response)
	{
		if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null) //위에 private String AUTH_COOKIE_NAME; 로 정의되어 있음
		{
			CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME); //(request, response, 경로, AUTH_COOKIE_NAME)
		}
		return "redirect:/";  //서버에서 재접속하라는 명령(URL을 다시 가리킴)
	}
	
	
	//회원가입
	//다이렉트로 들어오면 쿠키가 잇는지 확인하고 갈지 말지 정한다
	@RequestMapping(value="/user/regForm", method=RequestMethod.GET)  //회원가입페이지를 요청함 -> 리턴타입이 String이여서 @returnbody를 안넣음
	public String regForm(HttpServletRequest request, HttpServletResponse response)
	{
		String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);  //여기서 쿠키값을 헥사값으로 바꿨기 떄문에 쿠키값을 가지고 올떄는 다시 변환해야한다
		
		if(!StringUtil.isEmpty(cookieUserId))
		{//쿠키가 비어있지 않다는것은 로그인 되어 있다는것
			CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);  // "/"는 디렉토리 정보임, "/"가 없으면 같은 경로에 있는 쿠키삭제하는건데 현재경로에는 쿠키가 없어서 삭제가 안되는 거임
			return "redirect:/";  //서버에서 다시 호출 8088엔터 친거랑 똑같음              "/"는 최상위 디렉토리여서 최상위 디렉토리에서 쿠키를 삭제해야 쿠키가 완전히 사라짐
		}
		else
		{
			return "/user/regForm";  ///user/regForm 는 view 밑에 만들어야함 -> servlet-context에서 뷰리졸버 보면 /WEB-INF/view 라고 되어 있어서
		}
		
		
	}
	
	
	//아이디 중복체크
	//서버에서 클라이언트로 응답 데이터를 전송하기 위해서 @ResponseBody 를 사용하여 자바 객체를 HTTP 응답 본문의 객체로 변환 하여 클라이언트로 전송시키는 역할
	//즉, @ResponseBody는 객체를 반환할때만 사용한다 (반환값의 데이터타입이 String, int등 일반 타입이면 안씀)
	@RequestMapping(value="/user/idCheck", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> idCheck(HttpServletRequest request, HttpServletResponse response)
	{
		Response<Object> ajaxResponse = new Response<Object>();  //ajaxResponse라는 변수에 <Object>타입의 Response객체를 담아서 보내기 위함??
		String userId = HttpUtil.get(request, "userId");
		
		if(!StringUtil.isEmpty(userId))
		{
			if(userService.userSelect(userId) == null) //userSelect(userId)는 리턴타입이 user 객체
			{
				//객체가 null 이면 없는거 이므로 사용가능한 아이디
				ajaxResponse.setResponse(0, "Success");  // 0번 : 정상
			}
			else
			{
				ajaxResponse.setResponse(100, "Deplicate ID"); //100: 중복아이디 
			}
		}
		else
		{
			ajaxResponse.setResponse(400, "Bad Request"); //400: 잘못된 통신
		}
		
		//디버깅용 으로 찍음
		if(logger.isDebugEnabled())
		{
			logger.debug("[UserDao]/user/idCheck response\n" + JsonUtil.toJsonPretty(ajaxResponse));
		}
		
		return ajaxResponse;
	}
	
	
	//회원 등록
	@RequestMapping(value="/user/regProc", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> regProc(HttpServletRequest request, HttpServletResponse response)
	{
		Response<Object> ajaxResponse = new Response<Object>();
		
		
		//userPwd  userAddress  userGender userPhoneNaumber userAccount userName userNickName 넣어야함
		String userId = HttpUtil.get(request,"userId"); //사용자 입력값 가져옴?
		String userPwd = HttpUtil.get(request,"userPwd");
		String userName = HttpUtil.get(request,"userName");
		String userEmail = HttpUtil.get(request,"userAddress");
		
		
		
		
		//제이쿼리에서 입력전부 받도록 처리 해줬지만 한번더 입력값 없는게 있는지 확인
		if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd) && 
				!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail)  )
		{
			//넘어온아이디가 중복이 있는지 확인
			if(userService.userSelect(userId) == null)
			{
				//중복아이디가 없을 경우 정상적으로 등록
				//insert 할떄 user객체를 넘기기 떄문에 객체 생성
				User user = new User();
				
				user.setUserId(userId);  //user 객체에 값 세팅
				user.setUserPwd(userPwd);
				user.setUserName(userName);
				user.setUserEmail(userEmail);
				user.setStatus("Y");
				
				//insert 쿼리에 대한 리턴값은 처리 건수 -> 데이터가 있다면 건수가 0보다 큼
				if(userService.userInsert(user) > 0)
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
			//만약 한개라도 값이 입력안됬으면
			ajaxResponse.setResponse(400, "Bad Request");
		}
		
		//디버깅용 어디서 오류나는지 확인 하기 위한 로그찍기
		if(logger.isDebugEnabled())
		{
			logger.debug("[UserController]/user/regProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
		}
		
		return ajaxResponse;
	}
	
	
	//회원정보수정
	@RequestMapping(value="/user/updateForm", method=RequestMethod.GET)
	public String updateForm(ModelMap model, HttpServletRequest request, HttpServletResponse response)
	{
		String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME);
		
		User user = userService.userSelect(cookieUserId);
		
		model.addAttribute("user", user) ;  //첫번째 인수는 jsp에서 사용할 이름 , 두번째 인수는 사용된 객체의 이름   (jsp에서 사용할 이름, 사용된 객체의 이름)
		
		return "/user/updateForm";
	}
	
	//회원정보 수정(ajax 통신용)
	@RequestMapping(value="/user/updateProc", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> updateProc(HttpServletRequest request, HttpServletResponse response)
	{
		String cookieUserId = CookieUtil.getHexValue(request, AUTH_COOKIE_NAME); //쿠키아이디를 헥사코드로 바꿔서 가지고옴
		
		String userPwd = HttpUtil.get(request, "userPwd");
		String userName = HttpUtil.get(request, "userName");
		String userEmail = HttpUtil.get(request, "userEmail");
		
		//Response객체선언  - 아직 ajax 통신이 끝난게 아니므로 ajax통신으로 넘겨주려고
		Response<Object> ajaxResponse = new Response<Object>();
		
		if(!StringUtil.isEmpty(cookieUserId))
		{
			//쿠키있음 =로그인 되어있음 => 쿠키지워
			//업데이트하기 전에 우리 고객인지 먼저확인 -> URL에 다이렉트로 치고들어올수도 있으니가보안 문제로
			User user = userService.userSelect(cookieUserId);
			
			if(user != null)
			{
				//고객정보가 있으면 업데이터 정상처리
				//사용자 페이지에서 확인 했지만 다시한번더 확인
				if(!StringUtil.isEmpty(userPwd) && 
						!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail))
				{
					//User객체가 가지고 있는 값은 DB에 있는 값이므로 사용자가 입력한 값으로 다시 넣어줌
					user.setUserPwd(userPwd);  //위에서 User user = userService.userSelect(cookieUserId);로 User객체를 이미 선언했기 떄문에 가능
					user.setUserName(userName);
					user.setUserEmail(userEmail);
					
					if(userService.userUpdate(user) > 0)
					{
						ajaxResponse.setResponse(0, "Success");
					}
					else
					{
						//실패 - 하다가 난 오류 이기 떄문에 500
						ajaxResponse.setResponse(500, "Internal Server error");
					}
					
				}
				else
				{
					//입력 파라미터가 올바르지 않을 경우
					//입력값이 하나도 비어있음
					ajaxResponse.setResponse(400, "Bad Request");
				}
				
			}
			else
			{
				//사용자 정보 없을 경우 -> 쿠키 날리기
				CookieUtil.deleteCookie(request, response, "/", AUTH_COOKIE_NAME);
				ajaxResponse.setResponse(404, "Not Found");
			}
			
		}
		else
		{
			//비어있음 ==로그인 안되어있음
			ajaxResponse.setResponse(400, "Bad Request");
		}
		
		//디버깅용 어디서 오류나는지 확인 하기 위한 로그찍기 -> ajax 통신에만 넣음
		if(logger.isDebugEnabled())
		{
			logger.debug("[UserController]/user/updateProc response\n" + JsonUtil.toJsonPretty(ajaxResponse));
		}
		
		
		return ajaxResponse;
		
	}
	
	
	//M =비즈니스 로직 (테이블과 관련), view = .jsp, c =Controller
	//헥사코드로 바꿔서 넣는 이유는 보안상의 이유로 하는것
	//.xml을 수정하면 반드시 WAS 내렸다 올려야함
	
	
	
}
