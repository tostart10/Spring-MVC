<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<link rel="stylesheet" href="/resources/css/progress-bar.css" type="text/css">
<script type="text/javascript">
function kakaoPayResult(pgToken)
{
	$("#pgToken").val(pgToken);
	
	document.kakaoForm.action = "/kakao/payResult"; //이렇게 호출하면 2차 리퀘스트를 하게 됨
	document.kakaoForm.submit();
	
}
</script>

</head>
<body>
<!-- iframe에서 pcUrl값으로 QR코드 뿌려주는 거임  여기서 큐알코드 페이지가 찍히는 거임 -->
<!-- 여기가 response라고 보면됨 -->
<!-- 그리고 여기서 리스폰스 2번쨰를 해서 리다이렉트 시키게됨 -->
<iframe width="100%" height="650" src="${pcUrl}" frameborder="0" allowfullscreen=""></iframe> <!-- iframe이 중요한거임 카카오 페이세서 보내준 url을  iframe이 가지고 있는거 +큐알코드 보여줌-->
<form name="kakaoForm" id="kakaoForm" method="post">
	<input type="hidden" name="orderId" id="orderId" value="${orderId}"/>
	<input type="hidden" name="tId" id="tId" value="${tId}"/>
	<input type="hidden" name="userId" id="userId" value="${userId}"/>
	<input type="hidden" name="pgToken" id="pgToken" value=""/>
	
	
</form>
</body>
</html>