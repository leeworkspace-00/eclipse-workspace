<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="mvc.vo.BoardVo"%>
   <%
   BoardVo bv = (BoardVo)request.getAttribute("bv");			// 강제 형변환으로 양쪽의 타입을 맞춰주자
   %> 
   <%
    
    if(session.getAttribute("midx")==null){		// 회원번호가 널값이다 ? > 로그인 안했다는 것  로그인하라고 로그인 화면으로 보냄
    out.println("<script>alert('로그인해주세요');location.href='"+request.getContextPath()+"/member/memberLogin.aws'</script>");
    	
   }%>    
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>

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
		}
		let ans=confirm("저장하시겠습니까?");
		if(ans==true) {
			
			fm.action="<%=request.getContextPath()%>/board/boardWriteAction.aws";
			fm.method="post";
			fm.enctype="multipart/form-data";		// 문자를 넘길때 어떤 형태로 넘길건지 지정한다
			fm.submit();
		}
		return;
		}
		
		
		
		
/* 		else{
			alert("글쓰기 성공");
		} */
		
/* 	function deleteBtn() {
		alert("취소버튼을 누르셨습니다");
		
	} */
		



</script>




</head>
<body style="width:800px;">
<h3>글쓰기</h3>
<hr>
<form name = "frm">
	제목<input type="text" name="subject">
	<hr>
	내용<input type="text" name="contents"  style="width:700px;height:250px; display:inline-block;">
	<hr>
	작성자<input type="text" name="writer" style="width:90px;height:40px; display:inline-block;">
	<hr>
	비밀번호<input type="password" name = "password">
	<hr>
	첨부파일
	<input type="button" name="filename" value="파일선택" onclick ="">선택된 파일 없음
	<hr>
	<input type="button" id="save" value="저장" onclick="saveBtn();" style="float: right;">
	<input type="button" id="cancel" value="취소" onclick="history.back();" style="float: right;">

</form>
</body>
</html>