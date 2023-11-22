<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%
//request.getAttribute : 이전 다른 jsp나 servlet에서 설정된 매개변수값을 가져옴
//request.getAttribute("AUTH_COOKIE_NAME") 이거는 클라이언트 값이 아닌 env에 있는 값을 가져오는 것임
//request.getAttribute()는 리턴타입이 object임 -> 적절한 형태로 바꾸라는 뜻임
	if(com.icia.web.util.CookieUtil.getCookie(request, (String)request.getAttribute("AUTH_COOKIE_NAME")) != null) //!=null은 쿠키가 있다는 뜻
	{
		
%>
<nav class="navbar navbar-expand-sm bg-secondary navbar-dark mb-3"> 
	<ul class="navbar-nav"> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/user/loginOut"> 로그아웃</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/user/updateForm">회원정보수정</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/board/list">게시판</a> 
	    </li>
	      <li class="nav-item"> 
	      <a class="nav-link" href="/kakao/pay">카카오페이</a> 
	    </li>
  </ul> 
</nav>
<%
	}
	else
	{
%>
<nav class="navbar navbar-expand-sm bg-secondary navbar-dark mb-3"> 
	<ul class="navbar-nav"> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/"> 로그인</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/user/regForm">회원가입</a> 
	    </li> 
	    <li class="nav-item"> 
	      <a class="nav-link" href="/index2">로그인2</a> 
	    </li>
  </ul> 
</nav>

<%
	}
%>
