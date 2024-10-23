<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>    

<%!
	//어디서나 접근가능 public,   리턴값타입은 숫자형int = 메소드타입과 같음,  각 매개변수(파라미터변수-전달변수)
	public int memberInsert(Connection conn, String memberId, String memberPwd,
			String memberName,String memberGender,String memberBirth,
			String memberAddr, String memberPhone,String	memberEmail,String memberInHobby){
	
	int value=0;   //메소드 지역변수  결과값을 담는다
	String sql ="";
	PreparedStatement pstmt = null;   //쿼리 구문클래스 선언
	try{
		
		   sql ="insert into member(memberid,memberpwd,membername," 
		    		+ "membergender,memberbirth,memberaddr,memberphone,"
		    		+ "memberemail,memberhobby) values(?,?,?,?,?,?,?,?,?)";    
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setString(1, memberId);              //문자형 메소드 사용
		    pstmt.setString(2, memberPwd); 			 //문자형 메소드 사용  숫자형 setInt(번호,값);
		    pstmt.setString(3, memberName);
		    pstmt.setString(4, memberGender);
		    pstmt.setString(5, memberBirth);
		    pstmt.setString(6, memberAddr);
		    pstmt.setString(7, memberPhone);
		    pstmt.setString(8, memberEmail);
		    pstmt.setString(9, memberInHobby);
		    value = pstmt.executeUpdate();      //구문객체 실행하면 성공시 1 실패시 0리턴		
		
	}catch(Exception e){
		e.printStackTrace();		
	}finally{                    //try를 했던 catch를 했던 꼭 실행해야하는 영역
		//객체 사라지게하고
		//db연결 끊기		
		try{
			pstmt.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	return value;
}
%>    
    