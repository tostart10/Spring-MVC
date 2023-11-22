<%@page import="com.icia.web.util.HttpUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>


<%
   if(com.icia.web.util.CookieUtil.getCookie(request, (String)request.getAttribute("AUTH_COOKIE_NAME")) != null) //!=null은 쿠키가 있다는 뜻
   {
      
%>
<nav class="navbar navbar-expand-sm bg-secondary navbar-dark mb-3"> 
   <ul class="navbar-nav"> 
       <li class="nav-item"> 
         <a class="nav-link" href="/loginPage/loginOut"> logout</a> 
       </li> 
       <li class="nav-item"> 
         <a class="nav-link" href="/loginPage/updateForm">Edit INFO</a> 
       </li> 
       <li class="nav-item"> 
         <a class="nav-link" href="/board/list">board</a> 
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
         <a class="nav-link" href="/start.jsp">login</a> 
       </li> 
       <li class="nav-item"> 
         <a class="nav-link" href="/loginPage/regFormTest">sign up</a> 
       </li> 
       
  </ul> 
</nav>

<%
   }
%>