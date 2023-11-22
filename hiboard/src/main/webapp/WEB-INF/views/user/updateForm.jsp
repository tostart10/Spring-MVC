<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {

   $("#btnUpdate").on("click", function() {
      
      // 모든 공백 체크 정규식
      var emptCheck = /\s/g;
      // 영문 대소문자, 숫자로만 이루어진 4~12자리 정규식
      var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
            
	  if($.trim($("#userPwd1").val()).length <= 0)
	  {
		  alert("비밀번호를 입력하세요.");
		  $("#userPwd1").val("");
		  $("#userPwd1").focus();
		  return;
	  }
	  
	  if(emptCheck.test($("#userPwd1").val()))
      {
		  alert("비밀번호는 공백을 포함할 수 없습니다.");
		  $("#userPwd1").focus();
		  return;
      }
	  
	  if(!idPwCheck.test($("#userPwd1").val()))
	  {
		  alert("비밀번호는 영문대소문자와 숫자로 4~12자리 입니다.");
		  $("#userPwd1").focus();
		  return;
	  }
	  
	  if($("#userPwd1").val() != $("#userPwd2").val())
	  {
		  alert("비밀번호가 일치하지 않습니다.");
		  $("#userPwd2").focus();
		  return;
	  }
	  
	  if($.trim($("#userName").val()).length <= 0) //사용자 이름의 길이는 상관없음 한글자라도 입력만 하면 됨
	  {
			alert("사용자 이름을 입력하세요.");
			$("#userName").val("");
			$("#userName").focus();
			return;
	  }
	  
	  if(!fn_validateEmail($("#userEmail").val()))
	  {
		  alert("사용자 이메일 형식이 올바르지 않습니다.");
		  $("#userEmail").focus();
		  return;
	  }
		  
	  
	  $("#userPwd").val($("#userPwd1").val());
	  
		  
      $.ajax({
		type:"POST",
		url:"/user/updateProc", //컨트롤러에 있는 user Mapping value 값 넣어야함
		data:{
			userPwd:$("#userPwd").val(),
			userName:$("#userName").val(),
			userEmail:$("#userEmail").val()
			
			//여기서 System.out.println() 해서 뭐가 값이 안넘어가는지 알 수 있음
		},
		datatype:"JSON",
		beforeSend:function(xhr)
		{
			xhr.setRequestHeader("AJAX", "true");
		},
		success:function(response)
		{
			if(response.code == 0)
			{
				alert("회원 정보가 수정되었습니다.");
				location.href = "/user/updateForm"; //회원정보 수정페이지로 이동
				//root로 가고 싶으면 "/" 하면 됨
			}
			
			else if(response.code == 400)
			{
				alert("파라미터 값이 올바르지 않습니다.");
				$("#userPwd1").focus();
			}
			else if(response.code == 404)
			{
				alert("회원정보가 존재하지 않습니다.");
				location.href = "/";   //첫페이지로 이동
			}
			else if(response.code == 500)
			{
				alert("회원정보 수정 중 오류가 발생하였습니다.");
				$("#userPwd1").focus();
			}
			else
			{
				alert("회원정보 수정 중 오류 발생");
			}
			
		},
		error:function(xhr, status, error)
		{
			icia.common.error(error);
		}
		
      });
   });
});

function fn_validateEmail(value)
{
   var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
   
   return emailReg.test(value);
}
</script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/navigation.jsp" %>
<div class="container">
    <div class="row mt-5">
       <h1>회원정보수정</h1>
    </div>
    <div class="row mt-2">
        <div class="col-12">
            <form>
                <div class="form-group">
                    <label for="username">사용자 아이디</label>
                   ${user.userId}
                </div>
                <div class="form-group">
                    <label for="username">비밀번호</label>
                    <input type="password" class="form-control" id="userPwd1" name="userPwd1" value="${user.userPwd}" placeholder="비밀번호" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="username">비밀번호 확인</label>
                    <input type="password" class="form-control" id="userPwd2" name="userPwd2" value="${user.userPwd}" placeholder="비밀번호 확인" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="username">사용자 이름</label>
                    <input type="text" class="form-control" id="userName" name="userName" value="${user.userName}" placeholder="사용자 이름" maxlength="15" />
                </div>
                <div class="form-group">
                    <label for="username">사용자 이메일</label>
                    <input type="text" class="form-control" id="userEmail" name="userEmail" value="${user.userEmail}" placeholder="사용자 이메일" maxlength="30" />
                </div>
                <input type="hidden" id="userId" name="userId" value="${user.userId}" />
                <input type="hidden" id="userPwd" name="userPwd" value="" />
                <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>   