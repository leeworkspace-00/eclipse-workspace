package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.commentVo;

public class commentDao { // 댓글데이터 메서드는 전체가져오기, 댓글 삽입하기, 댓글 삭제하기 3가지
	private Connection conn; // 전역적으로 사용 연결객체를 여기가(DAO) 데이터 창고니까
	private PreparedStatement pstmt;

	public commentDao() { // 생성자를 만든다 -DB 연결하는 DB conn 객체를 생성하기 위해서 > 생성해야 MySQL에 접속할 수 있으니까

		Dbconn db = new Dbconn(); // 데이터베이스 연결하자
		this.conn = db.getConnection(); //
	}

	public ArrayList<commentVo> commentSelectAll(int bidx) {
		ArrayList<commentVo> alist = new ArrayList<commentVo>();

		String sql = "SELECT *FROM comment where delyn='N' and bidx=? ORDER BY cidx desc";
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery(); // 쿼리 실행하라

			while (rs.next()) {
				int cidx = rs.getInt("cidx");
				String ccontents = rs.getString("ccontents");
				String cwriter = rs.getString("cwriter");
				String writeday = rs.getString("writeday");
				String delyn = rs.getString("delyn");
				int midx = rs.getInt("midx");

				commentVo cv = new commentVo();
				cv.setCidx(cidx);
				cv.setCcontents(ccontents);
				cv.setCwriter(cwriter);
				cv.setWriteday(writeday);
				cv.setDelyn(delyn);
				cv.setMidx(midx); // 가져오는 이유는 삭제버튼은 작성자만 보여야 하기 때문에 !!

				alist.add(cv); // ArrayList객체에 하나씩 추가하고

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

		return alist;

	}

	public int commentInsert(commentVo cv) {
		int value = 0;

		String cwriter = cv.getCwriter();
		String ccontents = cv.getCcontents();
		// String csubject = cv.getCsubject();
		int bidx = cv.getBidx();
		int midx = cv.getMidx();
		String cip = cv.getCip();

		String sql = "insert into comment(csubject,ccontents,cwriter,bidx,midx,cip) value(null,?,?,?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ccontents);
			pstmt.setString(2, cwriter);
			pstmt.setInt(3, bidx);
			pstmt.setInt(4, midx);
			pstmt.setString(5, cip);
			value = pstmt.executeUpdate(); // 실행되면 1 안되면 0

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { // 각 객체도 소멸시키고 DB연결 끊는다
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return value; // 성공하면 1 실패하면 0 리턴됨
	}

	public int commentDelete(int cidx) {
		int value = 0;
		String sql = "update comment set delyn='Y' where cidx=?"; // 삭제여부값을 y로 업데이트

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cidx);
			value = pstmt.executeUpdate(); // 성공하면 1 실패하면 0
			System.out.println("value 값 확인 : " + value);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return value;

	}
}
