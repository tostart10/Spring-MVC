<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
    
   $("#userId").focus();
   
   $("#btnReg").on("click", function() {
	//공백체크
	var emptCheck = /\s/g;
	//영문 대소문자, 숫자로만 이루어진 4~12자리 정규식
	var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
	
	if($.trim($("#userId").val()).length <=0 )
	{
		alert("사용자 아이디를 입력하세요.");
		$("#userId").val("");
		$("#userId").focus();
		return;
	}
	
	if(emptCheck.test($("#userId").val()))  //공백이 있으면 안되므로 공백정규표현식과 맞냐를 해줌 -> 공백이면 경고창 띄우기
	{
		alert("사용자 아이디는 공백을 포함할 수 없습니다.");
		$("#userId").focus();
		return;
	}
	
	if(!idPwCheck.test($("#userId").val())) //정규 표현식과 맞지 않은경우에 경고창 띄어주기 위해서  !를 해줌
	{
		alert("사용자 아이디는 4~12자의 영문 대소문자와 숫자로만 입력하세요.");
		$("#userId").focus();
		return;
	}
	
	//비밀번호 처리
	if($.trim($("#userPwd1").val()).length <=0)
	{
		alert("비밀번호를 입력하세요.");
		$("#userPwd1").val("");
		$("#userPwd1").focus();
		return;
	}
	
	if(!idPwCheck.test($("#userPwd1").val()))
	{
		alert("영문 대소문자와 숫자로 4~12자리 입니다.");
		$("#userPwd1").val("");
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
	
	//이메일 값 검사
	if(!fn_validateEmail($("#userEmail").val()))
	{
		alert("사용자 이메일 형식이 올바르지 않습니다.");
		$("#userEmail").focus();
		return;
	}
	
	
	//혼자 한거 -> 이메일 안쓰면 안됨
	if($.trim($("#userEmail").val()).length <=0)
	{
		alert("이메일 입력안하면 안됨!!");
		$("#userEmail").focus();
		return;
		
	}
	
	//hidden 타입인 userPwd에 비밀번호값을 넣어서 서버로 보내기 위함
	$("#userPwd").val($("#userPwd1").val()); 
	
	
	//아이디 중복 체크  ajax
	$.ajax({
		type:"POST",
		url:"/user/idCheck",
		data:{
			userId:$("#userId").val()  //아이디 중복처리만 하는거기 때문에 id값만 보내면됨
		},
		datatype:"JSON",
		beforeSend:function(xhr){
			xhr.setRequestHeader("AJAX", true);
		},
		success:function(response) //객체로 받을 거기 때문에 (response) 처럼 변수명만 써주면됨 -> 자바스크립트는 어떤 데이터 타입이든 전부 받을 수 있기 때문에 변수명만 써주면됨
		{							//이때 변수명은 어떤 것이든 상관이 없음
			if(response.code == 0)
			{
				//alert("아이디 중복 없음 가입 가능");
				fn_userReg();
			}
			else if(response.code == 100)
			{
				alert("중복된 아이디 입니다.");
				$("#userId").focus();
			}
			else if(response.code == 400)
			{
				alert("파라미터 값이 올바르지 않습니다.");
				$("#userId").focus();
			}
			else
			{
				alert("오류가 발생하였습니다");
				$("#userId").focus();
			}
		},
		error:function(xhr, status, error)
		{
			icia.common.error(error);
		}
	});
	
	
   });
});


//ajax를 바깥에 했다는 것은 현재페이지 바뀌지 않고 하겠다는뜻
//실질적 회원가입은 이 함수가함

//userId:$("#userId").val()에서   userId는 서버에서 읽는 값이고,   $("#userId").val()는 브라우저에서 읽는 값임
function fn_userReg()
{
   $.ajax({
	type:"POST",
	url:"/user/regProc",
	data:{
		userId:$("#userId").val(),   
   		userPwd:$("#userPwd").val(),
   		userName:$("#userName").val(),
   		userEmail:$("#userEmail").val()
	},
	datatype:"JSON",
	beforeSend:function(xhr)
	{
		xhr.setRequestHeader("AJAX", "ture");	//ajax통신이라는 requestHeader 정보에 삽입
		//requestHeader에 ajax관련 정보들을 담아서 보내라는 뜻임
	},
	success:function(response)
	{
		//성공
		if(response.code == 0)
		{
			alert("회원가입이 되었습니다.");
			location.href = "/";  
		}
		else if(response.code == 100)
		{
			alert("회원아이디가 중복되었습니다.");
			$("#userId").focus();
		}
		else if(response.code == 400)
		{
			alert("파라미터값이 올바르지 않습니다.");
			$("#userId").focus();
		}
		else if(response.code == 500)
		{
			alert("회원 가입중 오류가 발생하였습니다.");
			$("#userId").focus();
		}
		else
		{
			alert("회원 가입중 오류가 발생하였습니다.");
			$("#userId").focus();
		}
	},
	error:function(xhr, status, error)
	{
		icia.common.error(error);
	}
   });
}

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
       <h1>회원가입</h1>
    </div>
    <div class="row mt-2">
        <div class="col-12">
            <form id="regForm" method="post">
                <div class="form-group">
                    <label for="username">사용자 아이디</label>
                    <input type="text" class="form-control" id="userId" name="userId" placeholder="사용자 아이디" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userPwd1">비밀번호</label>
                    <input type="password" class="form-control" id="userPwd1" name="userPwd1" placeholder="비밀번호" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userPwd2">비밀번호 확인</label>
                    <input type="password" class="form-control" id="userPwd2" name="userPwd2" placeholder="비밀번호 확인" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userName">사용자 이름</label>
                    <input type="text" class="form-control" id="userName" name="userName" placeholder="사용자 이름" maxlength="15" />
                </div>
                <div class="form-group">
                    <label for="userEmail">사용자 이메일</label>
                    <input type="text" class="form-control" id="userEmail" name="userEmail" placeholder="사용자 이메일" maxlength="30" />
                </div>
                <input type="hidden" id="userPwd" name="userPwd" value="" />
                <button type="button" id="btnReg" class="btn btn-primary">등록</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>