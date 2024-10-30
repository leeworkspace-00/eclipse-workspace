<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <%
 int bidx = (int)request.getAttribute("bidx");
 int originbidx = (int)request.getAttribute("originbidx");
 int depth = (int)request.getAttribute("depth");
 int level_ = (int)request.getAttribute("level_");
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글답변</title>
<link href="../board/boardStyle.css" rel="stylesheet">
<script>
function saveBtn()  {
	
	let fm = document.frm;
	
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
	let ans = confirm("답변을 남기시겠습니까?");
	  
	  if (ans == true) {
		  fm.action="<%=request.getContextPath()%>/board/boardReplyAction.aws";
		  fm.method="post";
		  fm.enctype="multipart/form-data";
		  fm.submit();
	  }	  
	  
	  return;
}



</script>

</head>
<body>
<header>
   <h2 class = "mainTitle">답변하기</h2>
</header>
<form name = "frm">
	<input type = "hidden" name = "bidx" value = "<%=bidx%>">
	<input type = "hidden" name = "originbidx" value = "<%=originbidx%>">
	<input type = "hidden" name = "depth" value = "<%=depth%>">
	<input type = "hidden" name = "level_" value = "<%=level_%>">




   <table class = "writeTable">
      <tr>
         <th>제목</th>
         <td><input type = "text" name = "subject"></td>
      </tr>
      <tr>
         <th>내용</th>
         <td><textarea name = "contents" rows="6"></textarea> </td>
      </tr>
      <tr>
         <th>작성자</th>
         <td><input type = "text" name = "writer"></td>
      </tr>
      <tr>
         <th>비밀번호</th>
         <td><input type = "password" name = "password"></td>
      </tr>
      <tr>
         <th>첨부파일</th>
         <td><input type = "file" name = "filename" value = "파일선택"></td>
      </tr>
   
   </table>
   
   <div class = "btnBox">
      <button type="button" class="btn" onclick = "saveBtn();">저장</button>
      <a class="btn aBtn" onclick="history.back();">취소</a>

   </div>



</form>

</body>
</html>