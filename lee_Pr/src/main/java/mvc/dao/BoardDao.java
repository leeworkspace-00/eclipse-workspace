package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.searchCriteria;

public class BoardDao {
	private Connection conn; // 전역적으로 사용 연결객체를 여기가(DAO) 데이터 창고니까
	private PreparedStatement pstmt = null;

	public BoardDao() { // 생성자를 만든다 -DB 연결하는 DB conn 객체를 생성하기 위해서 > 생성해야 MySQL에 접속할 수 있으니까

		Dbconn db = new Dbconn(); // 데이터베이스 연결하자
		this.conn = db.getConnection(); //
	}

	public int boardInsert(BoardVo bv) { // DB에도 파일이름 등등 넣고 싶으면 여기에서 추가해야함 (웹에서 추가했다는 가정)
		int value = 0;

		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String password = bv.getPassword();
		String filename = bv.getFilename();
		String ip = bv.getIp();

		String sql = "INSERT INTO BOARD_AWS (originbidx,depth,level_,subject,contents,writer,password,filename,ip) value(null,0,0,?,?,?,?,?,?)";
		// mysql에서 가져온 인서트 구문 담아주기
		// originbidx 의 값은 null임

		String sql2 = "update BOARD_AWS set originbidx = (SELECT A.maxbidx from (select max(bidx) as maxbidx from BOARD_AWS)A)where bidx= (SELECT A.maxbidx from (select max(bidx) as maxbidx from BOARD_AWS)A)";
		// bidx의 최고값으로 originbidx값으로 수정해주기
		// originbidx = null > originbidx = max(bidx)

		try {
			conn.setAutoCommit(false); // 수동커밋으로 변경
			pstmt = conn.prepareStatement(sql); // insert 실행
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, password);
			pstmt.setString(5, filename);
			pstmt.setString(6, ip);
			int exec = pstmt.executeUpdate(); // 실행되면 1 실행안되면 0

			pstmt = conn.prepareStatement(sql2); // update 실행
			int exec2 = pstmt.executeUpdate(); // 실행되면 1 실행안되면 0

			conn.commit(); // 일괄처리 커밋 try하고 마지막에 커밋하도록 코딩

			value = exec + exec2; // 오류 없이 실행된다면 리턴값 2 아니면 다 실패임 > 더하면2 성공하면 2 리턴되야함

		} catch (SQLException e) {
			try {
				conn.rollback(); // 실행 중 오류 발생시 원래대로 복원 > 롤백처리하도록 코딩
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return value; // 성공하면 2 리턴
	}

	public ArrayList<BoardVo> boardSelectAll(searchCriteria scri) {
		int page = scri.getPage(); // 페이지 번호
		int perPageNum = scri.getPerPageNum(); // 화면 노출개수

		String str = "";
		String keyword = scri.getKeyword();
		String searchType = scri.getSearchType();

		// 키워드가 존재한다면(없지 않으면) like 구문을 활용해서 위에 나타나도록 하는 검색기능
		if (!scri.getKeyword().equals("")) {
			str = "and " + searchType + " like concat('%','" + keyword + "','%')"; // concat('첫번째
																					// 붙일내용','"+keyword+"','마지막에 붙일내용')

		}

		ArrayList<BoardVo> alist = new ArrayList<BoardVo>(); // ArrayList컬랙션 객체에 BoardVo를 담겠다 BoardVo는 컬럼값을 담겠다
		String sql = "SELECT * FROM board_aws where delyn='N' " + str
				+ " ORDER BY originbidx desc, depth asc LIMIT ?,?"; // board

		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스 (담기) // 모든 데이터
								// 가져오기
		// str > 검색어임 없으면 없는대로 넘어가겠지?
		// 있으면 검색어 포함되서 나옴

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (page - 1) * perPageNum); // 페이지 빼기 1 * 15
			pstmt.setInt(2, perPageNum); // 15개씩 나타내주는 perPageNum
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int bidx = rs.getInt("bidx");
				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String writeday = rs.getString("writeday");
				int level_ = rs.getInt("level_");

				BoardVo bv = new BoardVo();
				bv.setBidx(bidx);
				bv.setSubject(subject);
				bv.setContents(contents);
				bv.setWriter(writer);
				bv.setViewcnt(viewcnt);
				bv.setRecom(recom);
				bv.setWriteday(writeday);

				bv.setLevel_(level_);

				alist.add(bv);

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

	public int boardTotalCount(searchCriteria scri) {

		String str = "";
		String keyword = scri.getKeyword();
		String searchType = scri.getSearchType();

		if (!scri.getKeyword().equals("")) {
			str = "and " + searchType + " like concat('%','" + keyword + "','%')"; // concat('첫번째
																					// 붙일내용','"+keyword+"','마지막에 붙일내용')
		}
		int value = 0;

		String sql = "SELECT count(*) as cnt FROM BOARD_AWS where delyn='N' " + str + ""; // 삭제되지 않은 전체 데이터의 수 뽑는 쿼리
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				value = rs.getInt("cnt");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return value;
	}

	public BoardVo boardSelectOne(int bidx) { // 받아올 게시물 번호 이걸로 뭐할거냐면 게시물 클릭하면>그 게시물 내용 보여줄거야 db에서 가져와서
		// 제목 내용 조회수 작성일 작성자 등등 꺼내와서 세팅해주는 메서드 만들기
		// 1. 형식부터 만든다
		BoardVo bv = null;
		// 2. 사용할 쿼리를 준비한다
		String sql = "SELECT * FROM BOARD_AWS where delyn='N' AND bidx = ?";
		ResultSet rs = null;
		try { // 3. 연결객체에서 구문쿼리 실행구문 클래스 불러온다
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();

			if (rs.next() == true) {

				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				String writeday = rs.getString("writeday");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String filename = rs.getString("filename");
				int rtnBidx = rs.getInt("bidx");
				int originBidx = rs.getInt("originbidx");
				int depth = rs.getInt("depth");
				int level_ = rs.getInt("level_");
				String password = rs.getString("password");

				bv = new BoardVo();

				bv.setSubject(subject);
				bv.setContents(contents);
				bv.setWriter(writer);
				bv.setWriteday(writeday);
				bv.setViewcnt(viewcnt);
				bv.setRecom(recom);
				bv.setFilename(filename);
				bv.setBidx(rtnBidx);
				bv.setOriginbidx(originBidx);
				bv.setDepth(depth);
				bv.setLevel_(level_);
				bv.setPassword(password);
			}

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

		return bv;
	}

	public int boardUpdate(BoardVo bv) { // 게시물 수정하는 메서드 생성
		int value = 0;

		// 쿼리 작성하고 가져와 문자열 sql에 담기
		String sql = "UPDATE BOARD_AWS set subject =?, contents=?, writer=?,modifyday=now() where bidx=? and password=?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bv.getSubject());
			pstmt.setString(2, bv.getContents());
			pstmt.setString(3, bv.getWriter());
			pstmt.setInt(4, bv.getBidx());
			pstmt.setString(5, bv.getPassword());

			value = pstmt.executeUpdate(); // 실행되면 1 실행안되면 0

		} catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return value;
	}

	public int boardViewCntUpdate(int bidx) {
		int value = 0;
		String sql = "UPDATE BOARD_AWS SET viewcnt = viewcnt+1 where bidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				pstmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return value;
	}

	public int boardRecomUpdate(int bidx) {
		int value = 0;
		int recom = 0;
		String sql = "UPDATE BOARD_AWS SET recom = recom+1 where bidx=?";
		String sql2 = "select recom from BOARD_AWS where bidx=?";
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate(); // 실행되면 1 안되면 0 리턴 업데이트 = +1 해준다
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery(); // 쿼리 실행 하라는 뜻 결과값을 담아라~

			if (rs.next() == true) {
				recom = rs.getInt("recom"); // rs 가 값이 있으면 추천수 꺼낼거임
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				// conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return recom; // 리턴값 추천수
	}

	public int boardDelete(int bidx, String password) {
		int value = 0;
		String sql = "update BOARD_AWS set delyn = 'Y' WHERE bidx = ? and password=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			pstmt.setString(2, password);
			value = pstmt.executeUpdate(); // 실행되면 1 안되면 0리턴

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

	public int boardReply(BoardVo bv) {

		int maxbidx = 0;

		String sql = "update board_aws set depth= depth+1 where originbidx =?  and depth > ?";
		String sql2 = "insert into board_aws (originbidx, depth,level_,subject, contents, writer,filename,password,ip) values(?,?,?,?,?,?,?,?,?)";
		String sql3 = "select max(bidx) as maxbidx from board_aws where originbidx=?";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth());

			int exec = pstmt.executeUpdate();
			// 실행되면 1 아니면 0

			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, bv.getOriginbidx());
			pstmt.setInt(2, bv.getDepth() + 1);
			pstmt.setInt(3, bv.getLevel_() + 1);
			pstmt.setString(4, bv.getSubject());
			pstmt.setString(5, bv.getContents());
			pstmt.setString(6, bv.getWriter());
			pstmt.setString(7, bv.getFilename());
			pstmt.setString(8, bv.getPassword());
			pstmt.setString(9, bv.getIp());
			int exec2 = pstmt.executeUpdate(); // 실행되면 1 아니면 0

			ResultSet rs = null;
			pstmt = conn.prepareStatement(sql3);
			pstmt.setInt(1, bv.getOriginbidx());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				maxbidx = rs.getInt("maxbidx");
			}

			conn.commit();

			// value = exec + exec2;

		} catch (SQLException e) {
			try {
				conn.rollback(); // 실행 중 오류 발생시 원래대로 복원 > 롤백처리하도록 코딩
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return maxbidx; // 성공하면 2 리턴
	}

}
