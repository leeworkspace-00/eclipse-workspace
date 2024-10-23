package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.BoardDao;
import mvc.vo.BoardVo;
import mvc.vo.Criteria;
import mvc.vo.PageMaker;

@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String location; // 멤버변수 초기화(전역선언) > 이동할 페이지 담을거임

	public BoardController(String location) {
		this.location = location; // 생성자를 통해서 넘어온 location을 전역 변수에 담아 사용하겟다
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String paramMethod = ""; // 새로 전역변수 설정해서 모든 블록에서 사용할 수 있도록 함 + 전송방식에 따라서 변수에 s f를 담아서 사용할 것
		String url = ""; // 새로 전역변수 설정해서 모든 블록에서 사용할 수 있도록 함

		if (location.equals("boardList.aws")) { // 가상경로 : 보여짐

			String page = request.getParameter("page"); // 넘겨온 페이지 값
			if (page == null) {
				page = "1";
			}
			int pageInt = Integer.parseInt(page);

			Criteria cri = new Criteria();
			cri.setPage(pageInt);

			PageMaker pm = new PageMaker();
			pm.setCri(cri); // <-- pagemaker 에 Criteria담아서 가지고 다닌다

			BoardDao bd = new BoardDao();
			// 페이징처리하기위해 전체데이터 개수 가져오기
			int boardCnt = bd.boardTotalCount();
			// System.out.println("게시물수는? : " + boardCnt);
			pm.setTotalCount(boardCnt); // <-- pagemaker 에 전체게시물의 개수 담아서 페이지 계산

			ArrayList<BoardVo> alist = bd.boardSelectAll(cri);
			// System.out.println("alist : " + alist); // 객체 주소가 나오면 객체가 생성되었다는 걸 확인 가능
			request.setAttribute("pm", pm); // 포워드 방식으로 넘기기 때문에 공유가 가능하다

			request.setAttribute("alist", alist); // request.setAttribute에 왜 담는데?
			// 이건 화면까지 가져간다는데?

			// 왜 포워드 방식으로 왜보여주는데?
			paramMethod = "F";
			url = "/board/boardList.jsp"; // 실제 내부경로 : 안보여짐

		} else if (location.equals("boardWrite.aws")) { // 이 가상경로가 들어오면 / url에 나타나는 주소
			paramMethod = "F";
			url = "/board/boardWrite.jsp"; // 포워드 방식으로 이 실제 주소로 보냄 이 주소는 url이 보이지 않음
			// url = request.getContextPath() + "/board/boardWrite.jsp"; // 실제 내부경로 : 안보여짐
			// 포워드 방식은 내부 서버에서 공유되기 때문에 request.getContextPath()를 써줄 필요가 없다.

		} else if (location.equals("boardWriteAction.aws")) {
			// System.out.println("boardWriteAction.aws");
			// 1. 파라미터 값을 넘겨 받는다

			// 라이브러리 방식
			// String savePath =
			// "D:\\dev\\eclipse-workspace\\mvc_programming\\src\\main\\webapp\\images"; >
			// 이게 d드라이브 경로 원래 맞춰야하는 경로임 + 깃로컬경로

			// C:\Users\admin\git\mvc_programming\MVC\mvc_programming\src\main\webapp\images
			// >> 이게 지금 저장되고 있는 경로 + 깃연동 경로

			String savePath = "D:\\dev\\eclipse-workspace\\mvc_programming\\src\\main\\webapp\\images";
			int sizeLimit = 15 * 1024 * 1024; // 15메가를 올리겠다(15M)
			String dataType = "UTF-8";
			DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy();

			MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, dataType, policy);

			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");

			HttpSession session = request.getSession(); // 세션객체 부르기

			session.getAttribute("midx");
			int midx = Integer.parseInt(session.getAttribute("midx").toString()); // 로인인 할 때 담았던 midx값을 꺼낸다

			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPassword(password);
			bv.setMidx(midx);

			// 2. DB처리한다

			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(bv);
			if (value == 2) { // 입력성공
				paramMethod = "S"; // sendRedirect방식으로
				url = request.getContextPath() + "/board/boardList.aws";// 로 보낸다

			} else {// 실패했으면
				paramMethod = "S"; // sendRedirect방식
				url = request.getContextPath() + "/board/boardWrite.aws";
				// 3. 처리후 이동한다 sendRedirect방식으로
			}

		} else if (location.equals("boardContents.aws")) { // 제목 클릭하고 들어오면 조회수 1 씩 증가시키는 메서드 호출해서 클릭과 동시에 조회수 올려주자

			// 1. 넘어온 값 담기
			// System.out.println("bidx 넘어왔는지 확인 : " + bidx);
			String bidx = request.getParameter("bidx"); // 통신객체로 넘어온 bidx를 받아서 문자열로 담는다
			int bidxInt = Integer.parseInt(bidx); // 위에서 받아온 문자열 bidx를 숫자형으로 바꿔줘야겠지

			// 2. 처리하기

			BoardDao bd = new BoardDao(); // 객체 생성하고
			// update하는 쿼리 만들자 > 조회수 증가하는 쿼리
			bd.boardViewCntUpdate(bidxInt);
			BoardVo bv = bd.boardSelectOne(bidxInt); // 여기서 메서드 작동하도록 담아주자(해당되는 bidx데이터 가져오기)

			// 화면으로 가져가자
			request.setAttribute("bv", bv); // 같은 영역 내부 안에서 JSP페이지를 보여주자

			// 3. 이동해서 화면 보여주기 > 같은 영역(내부)안에서 jsp 페이지를 보여준다
			paramMethod = "F";
			url = "/board/boardContents.jsp";

		} else if (location.equals("boardModify.aws")) { // 보여지는 가짜경로
			String bidx = request.getParameter("bidx");
			int bidxInt = Integer.parseInt(bidx);

			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);

			request.setAttribute("bv", bv);

			paramMethod = "F"; // 내부에서 처리할거니까 포워드 방식으로
			url = "/board/boardModify.jsp"; // 이쪽으로 보내자 ~ 실제 경로

		} else if (location.equals("boardModifyAction.aws")) { // 이 주소가 오면
			// System.out.println("boardModifyAction.aws");

			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String password = request.getParameter("password");
			String bidx = request.getParameter("bidx");

			int bidxInt = Integer.parseInt(bidx);
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidxInt);

			if (password.equals(bv.getPassword())) { // 가져온 비밀번호랑 같은 경우

				BoardDao bd2 = new BoardDao(); // Dao 다시 생성
				BoardVo bv2 = new BoardVo();
				bv2.setSubject(subject);
				bv2.setContents(contents);
				bv2.setWriter(writer);
				bv2.setPassword(password);
				bv2.setBidx(bidxInt);

				int value = bd2.boardUpdate(bv2); // boardDao에 있는 boardUpdate메서드 실행
				paramMethod = "S";

				if (value == 1) {
					url = request.getContextPath() + "/board/boardContents.aws?bidx=" + bidx; // 수정된 게시물의 내용 보여주기
				} else {
					url = request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx; // 비밀번호가 일치하지 않으면 다시 수정하기
																							// 페이지 보여주기
				}

			} else {
				// 비밀번호가 다르면
				response.setContentType("text/html; charset=UTF-8"); // 응답 콘텐츠 타입 설정
				PrintWriter out = response.getWriter(); // PrintWriter 객체 가져오기

				out.println("<script>");
				out.println("alert('비밀번호가 다릅니다.');");
				out.println(
						"location.href='" + request.getContextPath() + "/board/boardModify.aws?bidx=" + bidx + "';");
				out.println("</script>");
				out.flush();
			}
			// 어떻게 수정됐는지 확인해야하니까 수정된 내용을 보여줘>
			// 글내용 보기화면 > boardContents 여기로
			// 보낸다
			// 보내려면 게시물 번호가 있어야 하니까 bidx 값을 넣어준다

		} else if (location.equals("boardRecom.aws")) { // 추천버튼 누르면
			String bidx = request.getParameter("bidx"); // board의 기본키값인 bidx > 파라미터값 요청해서 가져와 //
														// :request.getParameter("bidx") 이걸 문자열로 담아서 변수로 사용
			int bidxInt = Integer.parseInt(bidx); // 숫자형으로 다시 변경해서 사용

			BoardDao bd = new BoardDao();
			int recom = bd.boardRecomUpdate(bidxInt);

			PrintWriter out = response.getWriter();
			out.println("{\"recom\":\"" + recom + "\"}"); // ({\"키값\":\"value값\"}"); > json형식

			/*
			 * 이 방식으로ㅓ 하면 추천수 + 조회수 같이 증가한다 추천수만 올리고 싶으면 ajax 방식으로 해결하자 paramMethod = "S";
			 * >>컨트롤러에 ajax 구현함 // sendRedirect방식 url = request.getContextPath() +
			 * "/board/boardContents.aws?bidx=" + bidx;
			 */

		}

		if (paramMethod.equals("F")) { // 파라미터값이 F면
			RequestDispatcher rd = request.getRequestDispatcher(url); // 포워드방식으로
			rd.forward(request, response); // 이게 포워드
		} else if (paramMethod.equals("S")) { // 아니면 샌드리다이렉트 방식으로 처리하겠다는
			response.sendRedirect(url); // 이게 샌드리다이렉트(sendRedirect)
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
