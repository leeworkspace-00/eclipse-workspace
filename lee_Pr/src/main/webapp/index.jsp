<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>가상경로 페이지</title>
</head>
<body style = "text-align: center;">
<header>
	<h2>가상경로</h2>
</header>



<div>
	<a href = "<%=request.getContextPath() %>/board/boardList.aws">게시판가기</a>
</div>
<div>
	<a href = "<%=request.getContextPath() %>/board/boardWrite.aws">쓰기</a>
</div>
<div>
	<a href = "<%=request.getContextPath() %>/board/boardDelete.aws">삭제</a>
</div>


</body>
</html>