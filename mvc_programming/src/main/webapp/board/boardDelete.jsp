<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%String bidx = (String)request.getAttribute("bidx"); %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글삭제</title>

<script>
function saveBtn() {
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
<h3>글삭제</h3>
<hr>
<form name="frm">
<input type = "hidden" name = "bidx" value = "<%=bidx%>">
<p style="text-align: center;">비밀번호<input type="password" name="password" style="width:100px; height:25px;"></p>
</form>
	<hr>
	<input type="button" value="저장" onclick="saveBtn();" style="float: right;">
	<input type="button" value="취소" onclick="history.back();"  style="float: right;">



</body>
</html>