<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%
   // GNB 번호 (사용자관리)
   request.setAttribute("_gnbNo", 1);
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style>
*, ::after, ::before {
   box-sizing: unset;
}
.table-hover th, td{
   border: 1px solid #c4c2c2;
   text-align: center;
}
</style>
<script type="text/javascript">
$("document").ready(function(){
   
   $("a[name='userUpdate']").colorbox({
      iframe:true, 
      innerWidth:1235,
      innerHeight:400,
      scrolling:false,
      onComplete:function()
      {
         $("#colorbox").css("width", "1235px");
         $("#colorbox").css("height", "400px");
         $("#colorbox").css("border-radius", "10px");
      }      
   });
});

function fn_search()
{
   document.searchForm.curPage.value = "1";
   document.searchForm.action = "/user/list";
   document.searchForm.submit();
}

function fn_paging(curPage)
{
   document.searchForm.curPage.value = curPage;
   document.searchForm.action = "/user/list";
   document.searchForm.submit();
}

function fn_pageInit()
{
   $("#searchType option:eq(0)").prop("selected", true);
   $("#searchValue").val("");
   
   fn_search();      
}
</script>
</head>
<body id="school_list">
<%@ include file="/WEB-INF/views/include/admin_gnb.jsp" %>
   <div id="school_list" style="width:90%; margin:auto; margin-top:5rem;">
      <div class="mnb" style="display:flex; margin-bottom:0.8rem;">
         <h2 style="margin-right:auto; color: #525252;">회원 리스트</h2>
         <form class="d-flex" name="searchForm" id="searchForm" method="post" style="place-content: flex-end;">
            <select id="status" name="status" style="font-size: 1rem; width: 6rem; height: 3rem;">
               <option value="">상태</option>
               <option value="Y" <c:if test="${status == 'Y'}">selected</c:if>>정상</option>
               <option value="N" <c:if test="${status == 'N'}">selected</c:if>>정지</option>
            </select>
            <select id="searchType" name="searchType" style="font-size: 1rem; width: 8rem; height: 3rem; margin-left:.5rem; ">
               <option value="">검색타입</option>
               <option value="1" <c:if test="${searchType == '1'}">selected</c:if>>회원아이디</option>
               <option value="2" <c:if test="${searchType == '2'}">selected</c:if>>회원명</option>
            </select>
            <input name="searchValue" id="searchValue" class="form-control me-sm-2" style="width:15rem; margin-left:.5rem;" type="text" value="${searchValue}">
            <a class="btn my-2 my-sm-0" href="javascript:void(0)" onclick="fn_search()" style="width:7rem; margin-left:.5rem; background-color: rgb(239, 239, 239); border-color:rgb(118, 118, 118);">조회</a>
            <input type="hidden" name="curPage" value="${curPage}" />
         </form>
      </div>
      <div class="school_list_excel">
         <table class="table table-hover" style="border:1px solid #c4c2c2;">
            <thead style="border-bottom: 1px solid #c4c2c2;">
            <tr class="table-thead-main">
               <th scope="col" style="width:15%;">아이디</th>
               <th scope="col">이름</th>
               <th scope="col">이메일</th>
               <th scope="col">상태</th>
               <th scope="col">등록일</th>
            </tr>
            </thead>
            <tbody>

      <c:if test="${!empty list}">
         <c:forEach items="${list}" var="user" varStatus="status">
            <tr>
                <th scope="row" class="table-thead-sub" style="border: 1px solid #c4c2c2;"><a href="/user/update?userId=${user.userId}" name="userUpdate">${user.userId}</a></th>  <!-- /user/update 컨트롤러로 이동 -->
                <td>${user.userName}</td>
                <td>${user.userEmail}</td>
                <td><c:if test="${user.status == 'Y'}">정상</c:if><c:if test="${user.status == 'N'}">취소</c:if></td>
                <td>${user.regDate}</td>
            </tr>
         </c:forEach>
      </c:if>
         
      <c:if test="${empty list}">
            <tr>
                <td colspan="5">등록된 회원정보가 없습니다.</td>
            </tr>   
      </c:if>
         
            </tbody>
         </table>
         <div class="paging-right" style="float:right;">
            <!-- 페이징 샘플 시작 -->
            <c:if test="${!empty paging}">
               <!--  이전 블럭 시작 -->
               <c:if test="${paging.prevBlockPage gt 0}">
                  <a href="javascript:void(0)"  class="btn2 btn-primary" onclick="fn_paging(${paging.prevBlockPage})"  title="이전 블럭">&laquo;</a>
               </c:if>
               <!--  이전 블럭 종료 -->
               <span>
               <!-- 페이지 시작 -->
               <c:forEach var="i" begin="${paging.startPage}" end="${paging.endPage }">
                  <c:choose>
                     <c:when test="${i ne curPage}">
                        <a href="javascript:void(0)" class="btn2 btn-primary" onclick="fn_paging(${i})" style="font-size:14px;">${i}</a>
                     </c:when>
                     <c:otherwise>
                        <h class="btn2 btn-primary" style="font-size:14px; font-weight:bold;">${i}</h>
                     </c:otherwise>
                  </c:choose>
               </c:forEach>
               <!-- 페이지 종료 -->
               </span>
               <!--  다음 블럭 시작 -->
               <c:if test="${paging.nextBlockPage gt 0}">
                  <a href="javascript:void(0)" class="btn2 btn-primary" onclick="fn_paging(${paging.nextBlockPage})" title="다음 블럭">&raquo;</a>
               </c:if>
               <!--  다음 블럭 종료 -->
            </c:if>
            <!-- 페이징 샘플 종료 -->
         </div>
      </div>
      
   </div>
</body>
</html>




