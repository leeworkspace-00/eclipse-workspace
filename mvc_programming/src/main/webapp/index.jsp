<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// 여기서 가야 가상경로 > 진짜 경로 연결해둬서 작동됨 다른곳은 서버내에서 경로 못 찾아서 오류남
String msg = "";
if(session.getAttribute("msg")!=null) {
	msg = (String)session.getAttribute("msg");
	
}
session.setAttribute("msg","");

int midx = 0;
String memberId ="";
String memberName="";
String alt = "";
String logMsg = "";
if (session.getAttribute("midx") != null){ // 세션으로 midx(회원번호 확인) 없으면(null) 있으면(!=null) 로그인이 되었으면 

	midx = (int)session.getAttribute("midx");		// midx에 세션정보 가져와서 담기
	memberId = (String)session.getAttribute("memberid");	// memberId에 세션정보 가져와서 담기 
	memberName = (String)session.getAttribute("memberName");// memberName에 세션 정보 가져와서 담기

	alt = memberName+ "님 로그인 되었습니다.";
	logMsg = "<a href ='"+request.getContextPath()+"/member/memberLogout.aws'>로그아웃</a>";		// 로그인되었다고 알려주고 로그아웃링크 걸어주기

}else {
	alt = "로그인 하세요";
	logMsg = "로그인";
}

%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>인덱스페이지</title>
<link href="./css/style.css" type="text/css" rel="stylesheet">

<script>
<%
	if (!msg.equals("")){
%> 
	alert('<%=msg%>');
<%
	}
%>
</script>
</head>

<body style = "text-align: center;">

<%=alt %>
<%=logMsg %>
<hr>
	<div class="main">환영합니다.메인페이지입니다.</div>
		<div>
			<a href="<%=request.getContextPath() %>/member/memberJoin.aws">
			회원가입 페이지 가기
			</a>
		</div>
		<div>
			<a href="<%=request.getContextPath() %>/member/memberLogin.aws">
			회원로그인 하기
			</a>
		</div>
		<div>
			<a href="<%=request.getContextPath() %>/member/memberlist.aws">
			회원목록보기
			</a>
		</div>
		<div>
			<a href="<%=request.getContextPath() %>/board/boardList.aws">
			게시판목록보기
			</a>
		</div> 
		<div>
			<a href="<%=request.getContextPath() %>/board/boardWrite.aws">
			글쓰기 페이지 가기 
			</a>
		</div> 
	
		

</body>
</html>
