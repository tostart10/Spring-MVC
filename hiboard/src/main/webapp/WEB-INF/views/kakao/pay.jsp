<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<link rel="stylesheet" href="/resources/css/progress-bar.css" type="text/css">
<script type="text/javascript">
$(document).ready(function() {
    
   $("#btnPay").on("click", function() {
	  $("#btnPay").prop("disabled", true);
	  
	  //상품코드
	  if($.trim($("#itemCode").val()).lenght <= 0)
	  {
		  alert("상품코드를 입력하세요");
		  $("#itemCode").val("");
		  $("#itemCode").focus();
		  
		  $("#btnPay").prop("disabled", false);
		  return;
	  }
	  
	  //상품명
	  if($.trim($("#itemName").val()).length <= 0)
	  {
		  alert("상품명을 입력하세요");
		  $("#itemName").val("");
		  $("#itemName").focus();
		  
		  $("btnPay").prop("disabled", false);
		  return;
	  }
	  
	  
	  
	  //수량
	  if($.trim($("#quantity").val()).length <= 0)
	  {
		  alert("수량을 입력하세요");
		  $("#quantity").val("");
		  $("#quantity").focus();
		  
		  $("btnPay").prop("disabled", false);
		  return;
	  }
	  
	  
	  if(!icia.common.isNumber($("#quantity").val())) //공통모듈과 다른지 맞는지 확인 (숫자만 입력가능하도록 하는 모듈)
	  {
		  alert("수량은 숫자만 입력가능합니다.");
		  $("#quantity").val("");
		  $("#quantity").focus();
		  
		  $("btnPay").prop("disabled", false);
		  return;
	  }
	  
	  
	  //금액
	  if($.trim($("#totalAmount").val()).length <= 0)
	  {
		  alert("금액을 입력하세요");
		  $("#totalAmount").val("");
		  $("#totalAmount").focus();
		  
		  $("btnPay").prop("disabled", false);
		  return;
	  }
	  
	  if(!icia.common.isNumber($("#totalAmount").val())) //공통모듈과 다른지 맞는지 확인 (숫자만 입력가능하도록 하는 모듈)
	  {
		  alert("금액은 숫자만 입력가능합니다.");
		  $("#totalAmount").val("");
		  $("#totalAmount").focus();
		  
		  $("btnPay").prop("disabled", false);
		  return;
	  }
	  
	  
	  //공통모듈로  ajax통신
	  icia.ajax.post({
		 url:"/kakao/payReady",
		 data:{
			 itemCode:$("#itemCode").val(),
			 itemName:$("#itemName").val(),
			 quantity:$("#quantity").val(),
			 totalAmount:$("#totalAmount").val()
		 },
		 success:function(response)
		 {
			 if(response.code ==0)
			 {
				 //alert("성공");
				 //response.data.orderId에서 3번쨰 에있는 것은 controller에서 json.addProperty("orderId", orderId); 의 "orderId"와 같은거
				 var orderId = response.data.orderId;  //orderId는
				 var tId = response.data.tId;	//tId는 통신을 하기위한 용도  Http프로토콜의 특징: 연결이 지속적이지 않고 끊어지기 떄문에 다시연결하기 위한 값
				 var pcUrl = response.data.pcUrl;  //여기서 카카오페이에서 반환된 데이터를 받는거임 , 팝업을 통해 레이어를 이용하라고 되어있음
				 
				 $("#orderId").val(orderId);
				 $("#tId").val(tId);
				 $("#pcUrl").val(pcUrl);
				 
				 //. URL을 팝업(Popup) 또는 레이어(Layer) 방식으로 띄웁니다.
				 //윈도우 팝업 띄어줌
				 var sin = window.open('', 'kakaoPopUp', 'toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=540,height=700,left=100,top=100');
				 //아래에 있는 form kakaoForm 의 target이"kakaoPopUp"이기떄문에 
				 //두번째 인수는 이름이 같아야하고 첫번쨰 매개변수 ''에 결과갑이 들어간다
				 //빈 페이지를 오픈해라, 카카오 팝업이 뜸 
				 
				 //팝업컨트롤러는 /pay/Ready 컨트롤러를 먼저 하고 그 다음에 실행임
				 
				 $("#kakaoForm").submit();
				 
				 $("btnPay").prop("disabled", false); 
			 }
			 else
			 {
				 alert("오류가 발생하였습니다.");
				 $("btnPay").prop("disabled", false);
			 }
		 },
		 error:function(error)
		 {
			 icia.common.error(error);
			 $("btnPay").prop("disabled", false); //버튼 활성화
		 }
	  });
	  
   });
});

function movePage()
{
   location.href = "/board/list";
}
</script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/navigation.jsp" %>
<div class="container">
   <h2>카카오페이</h2>
   <form name="payForm" id="payForm" method="post">
      상품코드<input type="text" name="itemCode" id="itemCode" maxlength="32" class="form-control mb-2" placeholder="상품코드" value="0123456789" />
      상품명<input type="text" name="itemName" id="itemName" maxlength="50" class="form-control mb-2" placeholder="상품명" value="비타민씨" />
      수량<input type="text" name="quantity" id="quantity" maxlength="3" class="form-control mb-2" placeholder="수량" value="1" />
      금액<input type="text" name="totalAmount" id="totalAmount" maxlength="10" class="form-control mb-2" placeholder="금액" value="30000" />
      <div class="form-group row">
         <div class="col-sm-12">
            <button type="button" id="btnPay" class="btn btn-primary" title="카카오페이">카카오페이</button>
         </div>
      </div>
   </form>
   <form name="kakaoForm" id="kakaoForm" method="post" target="kakaoPopUp" action="/kakao/payPopUp"> <!-- QR코드 팝업으로 띄우겠다는것임 -->
      <input type="hidden" name="orderId" id="orderId" value="" />
      <input type="hidden" name="tId" id="tId" value="" />
      <input type="hidden" name="pcUrl" id="pcUrl" value="" />
   </form>
</div>
</body>
</html>