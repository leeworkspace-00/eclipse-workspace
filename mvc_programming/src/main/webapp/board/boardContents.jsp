<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="mvc.vo.BoardVo"%>
   <%
   BoardVo bv = (BoardVo)request.getAttribute("bv");			// 강제 형변환으로 양쪽의 타입을 맞춰주자
   %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script> <!-- mvc jquery-CDN주소 추가  -->
<script>




$(document).ready(function(){
	
	$("#btn").click(function() {
			//alert("추천버튼 클릭확인");		
		$.ajax({
			type :"get",	//	전송방식 : get방식으로 전송하겠다 선언
			url : "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>",
			dataType : "json",		//json : 문서에서 {"키값":"value값","키값2:"value값2"}
			//data : {"recom":recom},		// get방식으로 전송하기 때문에  데이터 타입은 필요 없어요
			success : function(result) {		// 결과가 넘어와서 성공한 경우 받는 영역
				//alert("전송성공테스트");
			var str = "추천:"+result.recom+"";
				$("#btn").val(str);
			//alert("길이는? : " + result.length);
			//alert("cnt 값은? : " + reault.cnt); > 0이면 중복되는 데이터가 없다는 의미  0이외에 다른 숫자 : 중복된다는 의미
			
		/* 	if(result.recom==0) {
				alert("사용할 수 있는 아이디입니다."); */
				
			/* }else{
				alert("사용할 수 없는 아이디 입니다.")
				$("#memberid").val("");	// 입력한 아이디 지우기
				
			} */
	
			},
			error : function(result){  //결과가 실패했을때 받는 영역
				alert("전송실패 테스트");	
			}
	
		});
		

	});
		
		
});




</script>


</head>
<body>
<h3>글내용</h3>

<hr>
<div style="width:800px;height:400px">
	<div>
	글제목 :  <%=bv.getSubject() %> <a style = "float:right;">(조회수 : <%=bv.getViewcnt() %>)</a>
	<input type = "button" id = "btn" value = "추천:<%=bv.getRecom()%>">
	</div>
	<hr style="width:800px;">
	<div >
	글내용 : <%=bv.getContents() %>
	<br>
	<a style = "float:right;"> 작성자 : <%=bv.getWriter() %></a>
	<br>
	<a style = "float:right;"> 작성일 : <%=bv.getWriteday() %></a>
	<br>
	<a style = "float:right;"> 첨부파일 : <%=bv.getFilename() %></a>
	<br>
	</div>
	<hr style="width:800px;">

	

</div>


<div style="float: right;">
	<input type="button" name="update" value = "수정" onclick = "location.href='<%=request.getContextPath()%>/board/boardModify.aws?bidx=<%=bv.getBidx()%>'">
	<input type="button" name="delete" value = "삭제">
	<input type="button" name="answer" value = "답변">
	<input type="button" name="list" value = "목록" onclick = "location.href='<%=request.getContextPath()%>/board/boardList.aws?bidx=<%=bv.getBidx()%>'">
	<div>
		<input type="text" id="reply">
		<input type="button" value="댓글쓰기">
	</div>

</div>






<table name="reply" style=" width:800px; text-align: center;">	
		<thead>
			<tr>
				<th>번호</th>
				<th>작성자</th>
				<th>내용</th>
				<th>날짜</th>
				<th>DEL</th>
			</tr>
		</thead>
		</table>

</body>
</html>