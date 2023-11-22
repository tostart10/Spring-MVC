/**
 * <pre>
 * 프로젝트명 : Manager
 * 패키지명   : com.icia.manager.controller
 * 파일명     : IndexController.java
 * 작성일     : 2021. 7. 30.
 * 작성자     : daekk
 * </pre>
 */
package com.icia.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icia.common.util.CookieUtil;
import com.icia.common.util.HttpUtil;
import com.icia.common.util.JsonUtil;
import com.icia.common.util.StringUtil;
import com.icia.manager.model.Admin;
import com.icia.manager.model.Response;
import com.icia.manager.service.AdminService;


/**
 * <pre>
 * 패키지명   : com.icia.manager.controller
 * 파일명     : IndexController.java
 * 작성일     : 2021. 7. 30.
 * 작성자     : daekk
 * 설명       : 인덱스 컨트롤러
 * </pre>
 */
@Controller("indexController")
public class IndexController
{
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private AdminService adminSevice;
	
	// 쿠키명
	@Value("#{env['auth.cookie.name']}")
	private String AUTH_COOKIE_NAME;
	
	/**
	 * <pre>
	 * 메소드명   : index
	 * 작성일     : 2021. 7. 30.
	 * 작성자     : daekk
	 * 설명       : 인덱스
	 * </pre>
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String
	 */
	@RequestMapping(value="/index")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response)
	{
		if(CookieUtil.getCookie(request, AUTH_COOKIE_NAME) != null)
		{
			return "redirect:/user/list";
		}
		else
		{
			return "/index"; //web-inf 밑에 index.jsp로 감
		}
	}
	
	
	
	//관리자 로그인
	@RequestMapping(value="/loginProc", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> loginProc(HttpServletRequest request, HttpServletResponse response)
	{
		Response<Object> res = new Response<Object>();
		
		String admId = HttpUtil.get(request, "admId");
		String admPwd = HttpUtil.get(request, "admPwd");
		
		if(!StringUtil.isEmpty(admPwd) && !StringUtil.isEmpty(admId))  //여기서 isEmpty는 자바이기 떄문에 icia.common.util.StringUtil에 있는것임
		{
			Admin admin = adminSevice.adminSelect(admId);   //서비스에 있는 메서드 호출하려면 @Autowired 꼭 선언해줘야함!!
			
			if(admin != null)
			{
				//비밀번호 체크
				if(StringUtil.equals(admin.getAdmPwd(), admPwd))
				{
					if(StringUtil.equals(admin.getStatus(), "Y"))
					{
						CookieUtil.addCookie(response, "/", -1, AUTH_COOKIE_NAME, CookieUtil.stringToHex(admId)); //로그인 성공했으면 쿠키 추가
						res.setResponse(0, "success");
						
					}
					else
					{
						res.setResponse(404, "not found");
					}
				}
				else
				{
					res.setResponse(-1, "password do not match");
				}
			}
			else
			{
				res.setResponse(404, "not found");   //관리자 정보 없음
			}
			
		}
		else
		{
			res.setResponse(400, "bad request");
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("[IndexController] /locProc response\n" + JsonUtil.toJsonPretty(res));
		}
		
		return res;
	}
	
	

}
