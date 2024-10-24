package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;

public class BoardDao {
	private Connection conn; // 전역적으로 사용 연결객체를 여기가(DAO) 데이터 창고니까
	private PreparedStatement pstmt = null;

	public BoardDao() { // 생성자를 만든다 -DB 연결하는 DB conn 객체를 생성하기 위해서 > 생성해야 MySQL에 접속할 수 있으니까

		Dbconn db = new Dbconn(); // 데이터베이스 연결하자
		this.conn = db.getConnection(); //
	}

	public ArrayList<BoardVo> boardSelectAll(Criteria cri) {

		int page = cri.getPage(); // 페이지 번호
		int perPageNum = cri.getPerPageNum(); // 화면 노출개수

		ArrayList<BoardVo> alist = new ArrayList<BoardVo>(); // ArrayList컬랙션 객체에 BoardVo를 담겠다 BoardVo는 컬럼값을 담겠다
		String sql = "SELECT *FROM board where delyn='N' ORDER BY bidx desc,depth LIMIT ?,?"; // board 에 있는 모든 데이터 가져오기
		ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스 (담기)

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, (page - 1) * perPageNum); // 페이지 빼기 1 * 15
			pstmt.setInt(2, perPageNum); // 15개씩 나타내주는 perPageNum

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int bidx = rs.getInt("Bidx");
				// int originbidx = rs.getInt("originbidx");
				// int depth = rs.getInt("depth");
				// int level_ = rs.getInt("level_");
				String subject = rs.getString("Subject");
				// String contents = rs.getString("contents");
				String writer = rs.getString("Writer");
				int recom = rs.getInt("recom");
				int viewcnt = rs.getInt("Viewcnt");
				// String filename = rs.getString("filename");
				String writeday = rs.getString("Writeday");
				// String delyn = rs.getString("delyn");
				// String ip = rs.getString("ip");
				// int midx = rs.getInt("midx");

				BoardVo bv = new BoardVo();
				bv.setBidx(bidx);
				// bv.setOriginbidx(originbidx);
				// bv.setDepth(depth);
				// bv.setLevel_(level_);
				bv.setSubject(subject);
				// bv.setContents(contents);
				bv.setWriter(writer);
				bv.setRecom(recom);
				bv.setViewcnt(viewcnt);
				// bv.setFilename(filename);
				bv.setWriteday(writeday);
				// bv.setDelyn(delyn);
				// bv.setIp(ip);
				// bv.setMidx(midx);

				alist.add(bv); // ArrayList객체에 하나씩 추가하고
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

	// 게시물 전체 개수 구하기
	public int boardTotalCount() {

		int value = 0; // 리턴변수 초기화
		// 1. 쿼리만들기
		String sql = "SELECT count(*) as cnt FROM board where delyn='N'"; // 삭제되지 않은 전체 데이터의 수 뽑는 쿼리
		// 2. conn 객체 안에 있는 구문클래스 꺼내 오기
		// 3. DB컬럼값을 받아오는 전용 클래스ResultSet 호출
		ResultSet rs = null; // ResultSet은 데이터를 그대로 복사하기 때문에 전달이 빠르다

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) { // 커서를 이동시켜서 첫줄로 옮긴다
				value = rs.getInt("cnt"); // 1024개가 담기겠지? 이걸 value에 담아 리턴해서 가져감
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally { // 마지막으로 DB연결 끊고 각객체도 소멸시킨다 > 메모리 차지하지 않기 위함
			try {
				rs.close();
				pstmt.close();
				// conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return value;
	}

	public int boardInsert(BoardVo bv) { // DB에도 파일이름 등등 넣고 싶으면 여기에서 추가해야함 (웹에서 추가했다는 가정)
		int value = 0;

		String subject = bv.getSubject();
		String contents = bv.getContents();
		String writer = bv.getWriter();
		String PASSWORD = bv.getPassword();
		String filename = bv.getFilename();
		int midx = bv.getMidx();

		String sql = "INSERT INTO board (originbidx,depth,level_,subject,contents,writer,PASSWORD,midx,filename) value(null,0,0,?,?,?,?,?,?)";
		// mysql에서 가져온 인서트 구문 담아주기
		// originbidx 의 값은 null임

		String sql2 = "update board set originbidx = (SELECT A.maxbidx from (select max(bidx) as maxbidx from board)A)where bidx= (SELECT A.maxbidx from (select max(bidx) as maxbidx from board)A)";
		// bidx의 최고값으로 originbidx값으로 수정해주기
		// originbidx = null > originbidx = max(bidx)

		try {
			conn.setAutoCommit(false); // 수동커밋으로 변경
			pstmt = conn.prepareStatement(sql); // insert 실행
			pstmt.setString(1, subject);
			pstmt.setString(2, contents);
			pstmt.setString(3, writer);
			pstmt.setString(4, PASSWORD);
			pstmt.setInt(5, midx);
			pstmt.setString(6, filename);
			int exec = pstmt.executeUpdate(); // 실행되면 1 실행안되면 0

			pstmt = conn.prepareStatement(sql2); // update 실행
			int exec2 = pstmt.executeUpdate(); // 실행되면 1 실행안되면 0

			conn.commit(); // 일괄처리 커밋 try하고 마지막에 커밋하도록 코딩

			value = exec + exec2; // 오류 없이 실행된다면 리턴값 2 아니면 다 실패임

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

	// 게시물 내용 가져오는 쿼리 작성하기
	public BoardVo boardSelectOne(int bidx) { // 받아올 게시물 번호 이걸로 뭐할거냐면 게시물 클릭하면>그 게시물 내용 보여줄거야 db에서 가져와서
		// 제목 내용 조회수 작성일 작성자 등등 꺼내와서 세팅해주는 메서드 만들기
		// 1. 형식부터 만든다
		BoardVo bv = null;
		// 2. 사용할 쿼리를 준비한다
		String sql = "SELECT * FROM board where delyn='N' AND bidx = ?";
		ResultSet rs = null;
		try { // 3. 연결객체에서 구문쿼리 실행구문 클래스 불러온다
			pstmt = conn.prepareStatement(sql); // 전역변수로 선언한 PreparedStatement객체로 담음
			pstmt.setInt(1, bidx); // 첫번째 물음표에 숫자형 매개변수 bidx 를 담아서 구문 완성하기
			rs = pstmt.executeQuery(); // 쿼리를 실행해서 결과값을 컬럼전용 클래스인 ResultSet 객체 rs에 담는다

			if (rs.next() == true) { // rs.next() 는 커서를 다음줄로 이동시킨다. 맨처음 커서는 상단에 위치되어 있다.
				// 해당되는 값이 존재한다면 BoardVo객체에 담는다
				// 필요한 값 :제목 내용 작성자 작성일 조회수 추천수 파일이름 꺼내오자 > 꺼내는거는 get
				String subject = rs.getString("subject");
				String contents = rs.getString("contents");
				String writer = rs.getString("writer");
				String writeday = rs.getString("writeday");
				int viewcnt = rs.getInt("viewcnt");
				int recom = rs.getInt("recom");
				String filename = rs.getString("filename");
				int rtnBidx = rs.getInt("bidx"); // 리턴한 bidx = rtnBidx
				int originBidx = rs.getInt("originbidx");
				int depth = rs.getInt("depth");
				int level_ = rs.getInt("level_");
				String password = rs.getString("password");

				bv = new BoardVo(); // 객체 생성해서 지역변수 bv로 담아서 리턴 해서 가져간다
				// 이제 vo에 담자 > 담는거는 set
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
		String sql = "UPDATE board set subject =?, contents=?, writer=?,modifyday=now() where bidx=? and PASSWORD=?";

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
		String sql = "UPDATE board SET viewcnt = viewcnt+1 where bidx=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			value = pstmt.executeUpdate(); // 실행되면 1 안되면 0 리턴

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

		return value;
	}

	public int boardRecomUpdate(int bidx) {
		int value = 0;
		int recom = 0;
		String sql = "UPDATE board SET recom = recom+1 where bidx=?";
		String sql2 = "select recom from board where bidx=?";
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
		String sql = "update board set delyn = 'Y' WHERE bidx = ? and password=?";
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
		// int value = 0;
		int maxbidx = 0;

		String sql = "update board set depth= depth+1 where originbidx =?  and depth > ?";
		String sql2 = "insert into board (originbidx, depth,level_,subject, contents, writer,midx,filename,password) values(?,?,?,?,?,?,?,?,?)";
		String sql3 = "select max(bidx) as maxbidx from board where originbidx=?";
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
			pstmt.setInt(7, bv.getMidx());
			pstmt.setString(8, bv.getFilename());
			pstmt.setString(9, bv.getPassword());

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
