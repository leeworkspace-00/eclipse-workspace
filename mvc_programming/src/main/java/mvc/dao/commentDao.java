package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mvc.dbcon.Dbconn;
import mvc.vo.commentVo;

public class commentDao {
	private Connection conn; // 전역적으로 사용 연결객체를 여기가(DAO) 데이터 창고니까
	private PreparedStatement pstmt = null;

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
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int cidx = rs.getInt("cidx");
				String ccontents = rs.getString("ccontents");
				String cwriter = rs.getString("cwriter");
				String writeday = rs.getString("writeday");

				commentVo cv = new commentVo();
				cv.setCcontents(ccontents);
				cv.setCwriter(cwriter);
				cv.setWriteday(writeday);
				// cv.setCidx(cidx);
				// cv.setBidx(bidx);

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
		String csubject = cv.getCsubject();
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

		return value;
	}

	public int commentDelete(int bidx, String password) {
		int value = 0;

		return value;

	}
}

/*
 * int page = scri.getPage(); // 페이지 번호 int perPageNum = scri.getPerPageNum();
 * // 화면 노출개수
 * 
 * String str = ""; String keyword = scri.getKeyword(); String searchType =
 * scri.getSearchType();
 * 
 * // 키워드가 존재한다면(없지 않으면) like 구문을 활용해서 위에 나타나도록 하는 검색기능 if
 * (!scri.getKeyword().equals("")) { str = "and " + searchType +
 * " like concat('%','" + keyword + "','%')"; // concat('첫번째 //
 * 붙일내용','"+keyword+"','마지막에 붙일내용')
 * 
 * }
 * 
 * ArrayList<BoardVo> alist = new ArrayList<BoardVo>(); // ArrayList컬랙션 객체에
 * BoardVo를 담겠다 BoardVo는 컬럼값을 담겠다 String sql =
 * "SELECT *FROM board where delyn='N' " + str +
 * " ORDER BY originbidx desc,depth asc LIMIT ?,?"; // board // 에 // 있는
 * ResultSet rs = null; // db에서 결과 데이터를 받아오는 전용 클래스 (담기) // 모든 데이터 // 가져오기 //
 * str > 검색어임 없으면 없는대로 넘어가겠지? // 있으면 검색어 포함되서 나옴
 * 
 * try { pstmt = conn.prepareStatement(sql); pstmt.setInt(1, (page - 1) *
 * perPageNum); // 페이지 빼기 1 * 15 pstmt.setInt(2, perPageNum); // 15개씩 나타내주는
 * perPageNum rs = pstmt.executeQuery();
 * 
 * while (rs.next()) { int bidx = rs.getInt("bidx"); String subject =
 * rs.getString("subject"); String contents = rs.getString("contents"); String
 * writer = rs.getString("writer"); int viewcnt = rs.getInt("viewcnt"); int
 * recom = rs.getInt("recom"); String writeday = rs.getString("writeday"); //
 * int originbidx = rs.getInt("originbidx"); // int depth = rs.getInt("depth");
 * int level_ = rs.getInt("level_"); // String filename =
 * rs.getString("filename");
 * 
 * // String delyn = rs.getString("delyn"); // String ip = rs.getString("ip");
 * // int midx = rs.getInt("midx");
 * 
 * BoardVo bv = new BoardVo(); bv.setBidx(bidx); bv.setSubject(subject);
 * bv.setContents(contents); bv.setWriter(writer); bv.setViewcnt(viewcnt);
 * bv.setRecom(recom); bv.setWriteday(writeday); // bv.setDepth(depth);
 * bv.setLevel_(level_);
 * 
 * alist.add(bv); // ArrayList객체에 하나씩 추가하고 // bv.setOriginbidx(originbidx);
 * 
 * // bv.setFilename(filename);
 * 
 * // bv.setDelyn(delyn); // bv.setIp(ip); // bv.setMidx(midx);
 * 
 * }
 * 
 * } catch (SQLException e) { e.printStackTrace(); } finally { try { rs.close();
 * pstmt.close(); conn.close(); } catch (SQLException e) { e.printStackTrace();
 * } }
 * 
 * return alist; }
 */

// 게시물 전체 개수 구하기
/* public int boardTotalCount(searchCriteria scri) { */
/*
 * 
 * String str = ""; String keyword = scri.getKeyword(); String searchType =
 * scri.getSearchType();
 * 
 * // 키워드가 존재한다면(없지 않으면) like 구문을 활용해서 위에 나타나도록 하는 검색기능 if
 * (!scri.getKeyword().equals("")) { str = "and " + searchType +
 * " like concat('%','" + keyword + "','%')"; // concat('첫번째 //
 * 붙일내용','"+keyword+"','마지막에 붙일내용') }
 * 
 * int value = 0; // 리턴변수 초기화 // 1. 쿼리만들기 String sql =
 * "SELECT count(*) as cnt FROM board where delyn='N' " + str + ""; // 삭제되지 않은
 * 전체 데이터의 수 뽑는 쿼리 // 2. conn 객체 안에 있는 구문클래스 꺼내 오기 // 3. DB컬럼값을 받아오는 전용
 * 클래스ResultSet 호출 ResultSet rs = null; // ResultSet은 데이터를 그대로 복사하기 때문에 전달이 빠르다
 * 
 * try { pstmt = conn.prepareStatement(sql);
 * 
 * rs = pstmt.executeQuery();
 * 
 * if (rs.next()) { // 커서를 이동시켜서 첫줄로 옮긴다 value = rs.getInt("cnt"); // 1024개가
 * 담기겠지? 이걸 value에 담아 리턴해서 가져감 }
 * 
 * } catch (SQLException e) { e.printStackTrace(); } finally { // 마지막으로 DB연결 끊고
 * 각객체도 소멸시킨다 > 메모리 차지하지 않기 위함 try { rs.close(); pstmt.close(); // conn.close();
 * } catch (SQLException e) { e.printStackTrace(); }
 * 
 * } return value; }
 */

// DB에도 파일이름 등등 넣고 싶으면 여기에서 추가해야함 (웹에서 추가했다는 가정)
/*
 * int value = 0;
 * 
 * String subject = bv.getSubject(); String contents = bv.getContents(); String
 * writer = bv.getWriter(); String PASSWORD = bv.getPassword(); String filename
 * = bv.getFilename(); int midx = bv.getMidx(); String ip = bv.getIp();
 * 
 * String sql =
 * "INSERT INTO board (originbidx,depth,level_,subject,contents,writer,PASSWORD,midx,filename,ip) value(null,0,0,?,?,?,?,?,?,?)"
 * ; // mysql에서 가져온 인서트 구문 담아주기 // originbidx 의 값은 null임
 * 
 * String sql2 =
 * "update board set originbidx = (SELECT A.maxbidx from (select max(bidx) as maxbidx from board)A)where bidx= (SELECT A.maxbidx from (select max(bidx) as maxbidx from board)A)"
 * ; // bidx의 최고값으로 originbidx값으로 수정해주기 // originbidx = null > originbidx =
 * max(bidx)
 * 
 * try { conn.setAutoCommit(false); // 수동커밋으로 변경 pstmt =
 * conn.prepareStatement(sql); // insert 실행 pstmt.setString(1, subject);
 * pstmt.setString(2, contents); pstmt.setString(3, writer); pstmt.setString(4,
 * PASSWORD); pstmt.setInt(5, midx); pstmt.setString(6, filename);
 * pstmt.setString(7, ip); int exec = pstmt.executeUpdate(); // 실행되면 1 실행안되면 0
 * 
 * pstmt = conn.prepareStatement(sql2); // update 실행 int exec2 =
 * pstmt.executeUpdate(); // 실행되면 1 실행안되면 0
 * 
 * conn.commit(); // 일괄처리 커밋 try하고 마지막에 커밋하도록 코딩
 * 
 * value = exec + exec2; // 오류 없이 실행된다면 리턴값 2 아니면 다 실패임 > 더하면2 성공하면 2 리턴되야함
 * 
 * } catch (SQLException e) { try { conn.rollback(); // 실행 중 오류 발생시 원래대로 복원 >
 * 롤백처리하도록 코딩 } catch (SQLException e1) { e1.printStackTrace(); }
 * 
 * e.printStackTrace(); } finally { try { pstmt.close(); conn.close(); } catch
 * (SQLException e) { e.printStackTrace(); } }
 * 
 * return value; // 성공하면 2 리턴
 */
