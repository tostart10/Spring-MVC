<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>

<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style>
body {
  /* padding-top: 40px; */
  padding-bottom: 40px;
  /* background-color: #eee; */
}

.form-signin {
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .form-signin-heading,
.form-signin .checkbox {
  margin-bottom: 10px;
}
.form-signin .checkbox {
  font-weight: 400;
}
.form-signin .form-control {
  position: relative;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="text"] {
  margin-bottom: 5px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	$("#userId").focus();
	
	$("#userId").on("keypress", function(e){
		
		if(e.which == 13)
		{	
			fn_loginCheckTest();
		}
		
	});
	
	$("#userPwd").on("keypress", function(e){
		
		if(e.which == 13)
		{	
			fn_loginCheckTest();
		}
		
	});
		
	//로그인 버튼 선택시
	$("#btnLogin").on("click", function() {
		
		fn_loginCheckTest();
		
	});
	
	//회원가입 버튼 선택시
	$("#btnReg").on("click", function() {
		
		location.href = "/loginPage/regFormTest";   
		
	});
	
	
	
});


function fn_loginCheckTest()
{
	if($.trim($("#userId").val()).length <= 0)
	{
		alert("ID can not be empty.");
		$("#userId").focus();
		return;
	}
	
	if($.trim($("#userPwd").val()).length <= 0)
	{
		alert("Password can not be empty.");
		$("#userPwd").focus();
		return;
	}
	
	
	
	$.ajax({
		type:"POST",
		url:"/loginPage/userLogin",
		data:{
			 userId:$("#userId").val(),
	         userPwd:$("#userPwd").val()
		},
		datatype:"JSON",
		beforeSend:function(xhr){
			xhr.setRequestHeader("AJAX", "true");
		},
		success:function(responseTest){
			
			if(!icia.common.isEmpty(responseTest))
			{
				var code = icia.common.objectValue(responseTest, "code", -500);
				
				if(code == 0)
	            {
	               alert("sign in Success!!");
	               location.href = "/start"; 
	            }
	            else
	            {
	               if(code == -1)
	               {
	                  alert("Password is not valid.");
	                  $("#userPwd").focus();
	               }
	               else if(code == 404)
	               {
	                  alert("아이디와 일치하는 사용자 정보가 없습니다.");
	                  $("#userId").focus();
	               }
	               else if(code == 400)
	               {
	                  alert("파라미터 값이 올바르지 않습니다.");
	                  $("#userId").focus();
	               }
	               else
	               {
	                  alert("An error has occurred.");
	                  $("#userId").focus();
	               }
	            }
				
				
			}
			else
			{
				alert("An error has occurred.");
	            $("#userId").focus();
			}
			
		},
		complete:function(data)
	    {
	       icia.common.log(data);
	    },
        error:function(xhr, status, error)
	    {
	      icia.common.error(error);
     }
	      
	});
}

</script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/navigationTest.jsp" %> 
<h1>팀플테스트</h1>
<div class="container">

   <form class="form-signin">
       <h2 class="form-signin-heading m-b3">login</h2>
      <label for="userId" class="sr-only">ID</label>
      <input type="text" id="userId" name="userId" class="form-control" maxlength="20" placeholder="아이디">
      <label for="userPwd" class="sr-only">Password</label>
      <input type="password" id="userPwd" name="userPwd" class="form-control" maxlength="20" placeholder="비밀번호">
        
      <button type="button" id="btnLogin" class="btn btn-lg btn-primary btn-block">login</button>
       <button type="button" id="btnReg" class="btn btn-lg btn-primary btn-block">sign up</button>
   </form>
</div>
</body>
</html>