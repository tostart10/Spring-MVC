<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<link rel="stylesheet" href="/resources/css/progress-bar.css" type="text/css">
<script type="text/javascript">
$(document).ready(function(){
	parent.kakaoPayResult("${pgToken}");  //parent는 payPopUo.jsp이고 parent 입장에서 open은 
	
	
});
</script>
</head>
<body>

</body>
</html>