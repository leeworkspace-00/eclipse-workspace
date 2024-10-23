package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.MemberVo;

public class MemberDao { // Dao : 데이터 엑세스 오브젝트 데이터 연결 객체

	// MVC방식으로 가기 전 model1방식이다
	// 객체들을 만들어 놓고 사용

	private Connection conn; // 전역변수로 사용 : 페이지 어느곳에서든 호출해 사용할 수 있다
	private PreparedStatement pstmt = null; // 쿼리 구문클래스 선언
	// 생성자를 통해서 db에 연결-메서드 사용

	public MemberDao() {
		Dbconn dbconn = new Dbconn(); // DB객체 생성
		conn = dbconn.getConnection(); // 메서드 호출해서 연결객체를 가져온다

	}

	// 어디서나 접근가능 public, 리턴값타입은 숫자형int = 메소드타입과 같음, 각 매개변수(파라미터변수-전달변수)
	public int memberInsert(String memberId, String memberPwd, String memberName, String memberGender,
			String memberBirth, String memberAddr, String memberPhone, String memberEmail, String memberInHobby) {
		// private PreparedStatement pstmt = null;
		int value = 0; // 메소드 지역변수 결과값을 담는다
		String sql = "";

		try {

			sql = "insert into member(memberid,memberpwd,membername,"
					+ "membergender,memberbirth,memberaddr,memberphone,"
					+ "memberemail,memberhobby) values(?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId); // 문자형 메소드 사용
			pstmt.setString(2, memberPwd); // 문자형 메소드 사용 숫자형 setInt(번호,값);
			pstmt.setString(3, memberName);
			pstmt.setString(4, memberGender);
			pstmt.setString(5, memberBirth);
			pstmt.setString(6, memberAddr);
			pstmt.setString(7, memberPhone);
			pstmt.setString(8, memberEmail);
			pstmt.setString(9, memberInHobby);
			value = pstmt.executeUpdate(); // 구문객체 실행하면 성공시 1 실패시 0리턴

		} catch (Exception e) {
			e.printStackTrace();
		} finally { // try를 했던 catch를 했던 꼭 실행해야하는 영역
			// 객체 사라지게하고
			// db연결 끊기
			try {
				pstmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return value;
	}

//회원정보를 담아오는 메서드  > 이거 가져가서 아이디 중복체크도 할거임 커스터마이징
	public MemberVo memberLoginCheck(String memberId, String memberPwd) { // 리턴값 타입은 mv
		MemberVo mv = null;
		// int value = 0;
		String sql = "SELECT * from member where memberid = ? and memberpwd= ?"; // mysql에서 쿼리문 가져와서 문자열 변수에 담는다
		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPwd);
			rs = pstmt.executeQuery();

			if (rs.next() == true) {// 커서가 이동해서 데이터 값이 있으면 =if(rs.next()와 같은 의미
				String memberid = rs.getString("memberid"); // 결과값에서 아이디를 뽑는다
				int midx = rs.getInt("midx");// 결과값에서 회원번호를 뽑는다
				String membername = rs.getString("membername");

				mv = new MemberVo(); // 화면에 가지고 갈 데이터를 담을 vo객체를 생성한다
				mv.setMemberid(memberid); // 아이디 데이터를 옮겨 담는다
				mv.setMidx(midx); // 회원번호를 옮겨 담는다
				mv.setMembername(membername);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return mv;
	}

	public ArrayList<MemberVo> memberSelectAll() { // 회원정보를 다 가져올 메서드

		ArrayList<MemberVo> alist = new ArrayList<MemberVo>();
		String sql = "SELECT * FROM member WHERE delyn = 'N' ORDER BY midx desc;";
		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) { // 커서가 다음으로 이동해서 첫글이 있느냐 물어보고 있으면 true 진행 rs: Result Set > RS
				int midx = rs.getInt("midx");
				String memberId = rs.getString("memberid");
				String memberName = rs.getString("membername");
				String memberGender = rs.getString("membergender");
				String writeday = rs.getString("writeday");

				MemberVo mv = new MemberVo(); // 첫행부터 mv에 옮겨담기
				mv.setMemberid(memberId);
				mv.setMembername(memberName);
				mv.setMembergender(memberGender);
				mv.setWriteday(writeday);

				alist.add(mv); // ArrayList객체에 하나씩 추가하고
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return alist; // 리턴
	}

	public int memberIdCheck(String memberId) { // 리턴값 타입은 mv
		MemberVo mv = null;

		String sql = "SELECT COUNT(*) as cnt from member where memberid = ?"; // mysql에서 쿼리문 가져와서 문자열 변수에 담는다
		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스
		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();

			if (rs.next() == true) {

				cnt = rs.getInt("cnt");

			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cnt;
	}

}
