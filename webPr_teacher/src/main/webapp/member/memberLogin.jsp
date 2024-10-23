<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<HTML>
<HEAD>
<TITLE>로그인</TITLE>
<style>
#parent {
display : table;
padding : 10px;
background-color : darkgray;
margin-top : 30px; 
}
#child {
display : table-cell;
text-align:center;
background-color : white;
}
ul{
	margin : 0px;
	padding : 0px;	
}
li {
	display : inline-block;
	list-style-type: none;
	padding : 0px 5px ;
	margin-top : 20px;
	margin-bottom : 10px;
	margin-left : 20px;
	margin-right : 20px;
	height: 20px;
}

input[type=text]:focus,input[type=password]:focus{
	font-size: 120%;
}

</style>
</HEAD>
 <BODY>
<header>로그인</header>
<nav></nav>
<section>
	<article>
	<div id="parent">
	<div id="child">
	<form name="frm" action=".test0920_result.html" method="post">
	<ul>
	<li>아이디</li>	
	<li><input type="text" name="memberId" maxlength="20"></li>
	<li>비밀번호</li>
	<li><input type="password" name="memberPwd" maxlength="20"></li>
	</ul>
	<ul>
	<li><input type="submit" name="btn" value="로그인하기" style="width:200px;">
	</li>
	</ul>	
</form>
</div>
</div>
</article>	
</section>
<aside>
</aside>
<footer>
made by hji.
</footer>
</BODY>
</HTML>
