<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Join Us!!</title>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
    var check = 0;
      $("#userId").focus();
     
      $("#btnLoginCheck").on("click",function(){
         userIdCheck(check); 
      });
      
         $("#btnReg").on("click", function() {
            //공백체크
            var emptCheck = /\s/g;
            //영문 대소문자, 숫자로만 이루어진 4~12자리 정규식
            var idPwCheck = /^[a-zA-Z0-9]{4,12}$/;
            
            if($.trim($("#userId").val()).length <=0 )
            {
               alert("Please input ID.");
               $("#userId").val("");
               $("#userId").focus();
               return;
            }
            
            if(emptCheck.test($("#userId").val()))  //공백이 있으면 안되므로 공백정규표현식과 맞냐를 해줌 -> 공백이면 경고창 띄우기
            {
               alert("ID can not be empty.");
               $("#userId").focus();
               return;
            }
            
            if(!idPwCheck.test($("#userId").val())) //정규 표현식과 맞지 않은경우에 경고창 띄어주기 위해서  !를 해줌
            {
               alert("Enter your user ID only in 4~12 characters of uppercase and lowercase letters and numbers.");
               $("#userId").focus();
               return;
            }
            
            //비밀번호 처리
            if($.trim($("#userPwd1").val()).length <=0)
            {
               alert("PassWord can not be empty.");
               $("#userPwd1").val("");
               $("#userPwd1").focus();
               return;
            }
            
            if(!idPwCheck.test($("#userPwd1").val()))
            {
               alert("The password is 4~12 characters in English case and numbers.");
               $("#userPwd1").val("");
               return;
            }
            
            if($("#userPwd1").val() != $("#userPwd2").val())
            {
               alert("The passwords don't match.");
               $("#userPwd2").focus();
               return;
            }
            
            if($.trim($("#userName").val()).length <= 0) //사용자 이름의 길이는 상관없음 한글자라도 입력만 하면 됨
            {
               alert("User Name can not be empty.");
               $("#userName").val("");
               $("#userName").focus();
               return;
            }
            
            
            
            
            if(!fn_validateEmail($("#userEmail").val()))
            {
               alert("User email format is not valid.");
               $("#userEmail").focus();
               return;
            }
            
            
            
            
            if($.trim($("#userEmail").val()).length <=0)
            {
               alert("Email can not be empty");
               $("#userEmail").focus();
               return;
               
            }
            
            
            if($.trim($("#userHobby").val()).length <= 0) 
            {
               alert("User Name can not be empty.");
               $("#userName").val("");
               $("#userName").focus();
               return;
            }
            
            
            
            if($("#userHobby").val() >= 5)
            {
               alert("Enter a number between 1 and 4 ");
               $("#userHobby").val("");
               $("#userHobby").focus();
               return;
            }
            
            
            $("#userPwd").val($("#userPwd1").val()); 
            fn_userReg();
      
      
      
      });
   });


   function fn_userReg()
   {
      $.ajax({
      type:"POST",
      url:"/loginPage/regProcTest",
      data:{
         userId:$("#userId").val(),   
            userPwd:$("#userPwd").val(),
            userName:$("#userName").val(),
            userEmail:$("#userEmail").val(),
            userHobby:$("#userHobby").val()
      },
      datatype:"JSON",
      beforeSend:function(xhr)
      {
         xhr.setRequestHeader("AJAX", "ture");   

      },
      success:function(response)
      {
         //성공
         if(response.code == 0)
         {
            alert("sign up Successed!!  Welcome ^^ ");
            location.href = "/";  
         }
         else if(response.code == 100)
         {
            alert("This is a duplicate ID.");
            //Parameter value is not correct
            $("#userId").focus();
         }
         else if(response.code == 400)
         {
            alert("The parameter value is incorrect.");
            $("#userId").focus();
         }
         else if(response.code == 500)
         {
            alert("An error occurred during membership registration.");
            $("#userId").focus();
         }
         else
         {
            alert("An error occurred during membership registration.");
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

   function userIdCheck(check)
   {
      $.ajax({
            type:"POST",
            url:"/loginPage/idCheckTest",
            data:{
               userId:$("#userId").val()
            },
            datatype:"JSON",
            beforeSend:function(xhr)
            {
               xhr.setRequestHeader("AJAX","true");
            },
            success:function(response)
            {
               if(response.code == 0)
               {
                  alert("아이디 중복 없음 가입 가능");
                  check = 1 ;
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
                  alert("오류가 발생하였습니다.");
                  $("#userId").focus();
               }   
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
<%@ include file="/WEB-INF/views/include/navigation.jsp" %>
<div class="container">
    <div class="row mt-5">
       <h1>sign up</h1>
    </div>
    <div class="row mt-2">
        <div class="col-12">
            <form id="regForm" method="post">
                <div class="form-group">
                    <label for="username">USER ID</label>
                      <button type="button" id="btnLoginCheck" class="btn btn-primary">중복체크</button>
                    <input type="text" class="form-control" id="userId" name="userId" placeholder="사용자 아이디" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userPwd1">Password</label>
                    <input type="password" class="form-control" id="userPwd1" name="userPwd1" placeholder="비밀번호" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userPwd2">Password check</label>
                    <input type="password" class="form-control" id="userPwd2" name="userPwd2" placeholder="비밀번호 확인" maxlength="12" />
                </div>
                <div class="form-group">
                    <label for="userName">Name</label>
                    <input type="text" class="form-control" id="userName" name="userName" placeholder="사용자 이름" maxlength="15" />
                </div>
                <div class="form-group">
                    <label for="userEmail">Email</label>
                    <input type="text" class="form-control" id="userEmail" name="userEmail" placeholder="사용자 이메일" maxlength="30" />
                </div>
                
                <div class="form-group">
                    <label for="hobby">Hobby</label>
                    <input type="text" class="form-control" id="userHobby" name="userHobby" placeholder="취미" maxlength="30" />
                </div>
                
                <input type="hidden" id="userPwd" name="userPwd" value="" />
                <button type="button" id="btnReg" class="btn btn-primary">sign</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>