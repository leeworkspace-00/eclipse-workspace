<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%String bidx = (String)request.getAttribute("bidx"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글삭제</title>
<link href="../board/boardStyle.css" rel="stylesheet">

<script>
function deleteBtn() {
	let fm = document.frm;

	
	if(fm.password.value=="") {
		alert("비밀번호란이 공란입니다");
		fm.password.focus();
		return;
	}
	
	let ans=confirm("삭제하시겠습니까?");	// 함수의 값을 참과 거짓 true false로 나눈다 
	if(ans==true) {
		fm.action="<%=request.getContextPath()%>/board/boardDeleteAction.aws";
		fm.method="post";
		fm.submit();
	} 
	return;
	}
</script>
</head>
<body>
<header>
   <h2 class = "mainTitle">글삭제</h2>
</header>

<form name="frm">
<input type = "hidden" name = "bidx" value = "<%=bidx%>">
<table class = "writeTable">
      <tr>
         <th>비밀번호</th>
         <td><input type="password" name="password"></td>
      </tr>
      
   </table>
   
   <div class="btnBox">
      <button type="button" class="btn" onclick="deleteBtn();">삭제</button>
      <a class="btn aBtn" href="#"  onclick="history.back();">취소</a>
   </div>

</form>

</body>
</html>