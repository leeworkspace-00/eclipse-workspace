<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="mvc.vo.BoardVo"%>
    <%@ include file ="/common/loginCheck.jsp"%>		
   <%
   BoardVo bv = (BoardVo)request.getAttribute("bv");			// 강제 형변환으로 양쪽의 타입을 맞춰주자
   %> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link href="../board/boardStyle.css" rel="stylesheet">
<script>
	function saveBtn()  {
		
		var fm = document.frm;
		
		if(fm.subject.value=="") {
			alert("제목을 입력해주세요")
			fm.subject.focus();
			return;
		}else if(fm.contents.value==""){
			alert("내용을 입력해주세요")
			fm.contents.focus();
			return;
		}else if(fm.writer.value=="") {
			alert("작성자를을 입력해주세요")
			fm.writer.focus();
			return;
		}else if(fm.password.value=="") {
			alert("비밀번호를 입력해주세요")
			fm.password.focus();
			return;
		}else {
			let ans=confirm("저장하시겠습니까?");	// 함수의 값을 참과 거짓 true false로 나눈다 
			if(ans==true) {
				fm.action="<%=request.getContextPath()%>/board/boardWriteAction.aws";
				fm.method="post";
				fm.enctype="multipart/form-data";		// 문자를 넘길때 어떤 형태로 넘길건지 지정한다
				fm.submit();
				return;
			}
			
		}
	}	
		
		
	
</script>

</head>
<body>

<header>
	<h2 class = "mainTitle">글쓰기</h2>
</header>

<form name = "frm">

<table class = "writeTable">
<tr>
	<th>제목</th> <!-- 테이블 헤드 표 첫줄들어가는 자리 -->
	<td><input type="text" name="subject"></td>
</tr>
<tr>
	<th>내용</th> <!-- 테이블 헤드 표 첫줄들어가는 자리 -->
	<td><textarea name="contents" rows="6"></textarea></td>
</tr>
<tr>
	<th>작성자</th> <!-- 테이블 헤드 표 첫줄들어가는 자리 -->
	<td><input type="text" name="writer"></td>
</tr>
<tr>
	<th>비밀번호</th> <!-- 테이블 헤드 표 첫줄들어가는 자리 -->
	<td><input type="password" name="password"></td>
</tr>
<tr>
	<th>첨부파일</th> <!-- 테이블 헤드 표 첫줄들어가는 자리 -->
	<td><input type="file" name="filename"></td>
</tr>

</table>
	<div class = "btnBox">
		<button type="button" class="btn" onclick="check();">저장</button>
		<a class="btn aBtn" onclick="history.back();">취소</a>

	</div>
</form>
</body>
</html>