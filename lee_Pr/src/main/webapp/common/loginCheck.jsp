<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    // 로그인해야지만 댓글을 쓸 수 있다는 설정 > 로그인 안되어있으면? 로그인 페이지로 가자
    // 공통파일로  사용하자
    if(session.getAttribute("midx")==null){		// 회원번호가 널값이다 ? > 로그인 안했다는 것  로그인하라고 로그인 화면으로 보냄
    out.println("<script>alert('로그인해주세요');location.href='"+request.getContextPath()+"/member/memberLogin.aws'</script>");
   }%> 