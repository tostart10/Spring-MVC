package com.icia.manager.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icia.common.util.HttpUtil;
import com.icia.common.util.JsonUtil;
import com.icia.common.util.StringUtil;
import com.icia.manager.model.Paging;
import com.icia.manager.model.Response;
import com.icia.manager.model.User;
import com.icia.manager.service.UserService;

@Controller("userController")
public class UserController 
{
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	//서비스 호출하려면 꼭 @Autowired 해줘야함!!
	@Autowired
	private UserService userService;
	
	private static final int LIST_COUNT = 10;
	private static final int PAGE_COUNT = 10;
	
	
	@RequestMapping(value="/user/list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response)
	{
		//사용자에서는 리스트로 게시물들을 봤지만
		//관리자에서는 리스트로 사용자들의 정보를 나열해서 볼거임
		
		
		//상태(Y:정상, N:정지)
		String status = HttpUtil.get(request, "status");// 이름을 status로 해줄거임
		//검색타입(1:회원아이디, 2:회원명)
		String searchType = HttpUtil.get(request, "searchType");
		//검색값
		String searchValue = HttpUtil.get(request, "searchValue");
		//현재페이지 번호
		int curPage = HttpUtil.get(request, "curPage",1);
		
		//총 조회수
		int totalCount = 0;
		//페이징 객체
		Paging paging = null;  //모델 밑에 있는 paging 객체임
		
		List<User> list = null;
		
		//USER 객체 선언
		User param = new User();  //조회용
		
	
		param.setStatus(status);
		
		if(!StringUtil.isEmpty(searchType) && !StringUtil.isEmpty(searchValue))
		{
			if(StringUtil.equals(searchType, "1")) //searchType 1:아이디
			{
				param.setUserId(searchValue);
				
				
			}
			else if(StringUtil.equals(searchType, "2"))
			{
				param.setUserName(searchValue);
				
			}
			else
			{
				searchType = "";
				searchValue = "";
			}
		}
		else
		{
			searchType= ""; //어차피 비어있지만 model에 addAttribute하려고
			searchValue = "";
		}
			
		
		totalCount = userService.userListCount(param); //여기를 10으로 하드코딩하면 페이지가 10으로 고정됨
		
		
		if(totalCount > 0)
		{
			paging = new Paging("/user/list", totalCount, LIST_COUNT, PAGE_COUNT, curPage, "curPage");
			
			param.setStartRow(paging.getStartRow());
			param.setEndRow(paging.getEndRow());

			list = userService.userList(param);
			
		}
		
		model.addAttribute("list", list);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("searchType", searchType);
		model.addAttribute("status", status);
		model.addAttribute("curPage", curPage);
		model.addAttribute("paging", paging);
		
		
		
		return "/user/list";
	}
	
	
	//회원조회 수정 (list.jsp에서 /user/update)
	//HTML 에서 get방식으로 보내줬음
	@RequestMapping(value="/user/update")
	public String update(Model model, HttpServletRequest request, HttpServletResponse response)  //model은 데이터와 페이지가 분리되어 있어서 페이지(.jsp)에 데이터를 뿌려주려면 model 객체를 써야함 
	{
		//회원아이디
		String userId = HttpUtil.get(request, "userId");
		
		if(!StringUtil.isEmpty(userId))
		{
			User user = userService.userSelect(userId);
			
			if(user != null)  //쿼리에서 사용자를 조회해서 user객체에 담아왔을 때 null이면 조회된 사용자가 없는것이고 != null이면 사용자가 조회된것임
			{
				model.addAttribute("user", user);
			}
		}
		
		return "/user/update";   
	}
	
	
	
	//회원 정보 수정
	@RequestMapping(value="/user/updateProc", method=RequestMethod.POST)
	@ResponseBody
	public Response<Object> updateProc(HttpServletRequest request, HttpServletResponse response)
	{
		Response<Object> res = new Response<Object>();
		
		//update.jsp의 var formData 의 값들을 가지고 오는것임
		//userId:$("#userId").val() 처럼 있으면  앞에있는 userId: 를 가지고 오는 것임
		String userId = HttpUtil.get(request, "userId");
		String userPwd = HttpUtil.get(request, "userPwd");
		String userName = HttpUtil.get(request, "userName");
		String userEmail = HttpUtil.get(request, "userEmail");
		String status = HttpUtil.get(request, "status");
		
		if(!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(userPwd) && !StringUtil.isEmpty(userName) && !StringUtil.isEmpty(userEmail)
				&& !StringUtil.isEmpty(status))
		{
			//다이렉트로 치고들어왔을 떄를 대비해서 다시 아이디 체크하는 것임 -> 500번 오류 방지
			User user = userService.userSelect(userId);
			//이 user의 
			
			if(user != null) 
			{
				user.setUserPwd(userPwd); //원래있던값을 사용자가 입력한 내용으로 바꿔줌
				user.setUserName(userName);
				user.setUserEmail(userEmail);
				user.setStatus(status);
				
				if(userService.userUpdate(user) > 0) // 서비스의 처리 결과가 int이기 때문에 0과 비교가능
				{
					res.setResponse(0, "Success");
				}
				else
				{
					//뭔가 오류
					res.setResponse(-1, "fail");
				}
			}
			else
			{
				//user가 없는 것임
				res.setResponse(404, "Not Found"); //사용자 정보 없음
			}
			
		}
		else
		{
			//파라미터 값이 잘못됨
			res.setResponse(400, "Bad request");
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("[UserController] /user/updateProc response\n" + JsonUtil.toJsonPretty(res));
		}
		
		
		return res;
	}
	
	
	
}
