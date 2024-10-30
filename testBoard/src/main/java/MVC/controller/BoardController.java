package MVC.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import MVC.dao.BoardDao;
import MVC.vo.BoardVo;
import MVC.vo.PageMaker;
import MVC.vo.searchCriteria;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

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

		if (location.equals("boardList.aws")) {

			String page = request.getParameter("page"); // 넘겨온 페이지 값
			if (page == null) {
				page = "1";
			}
			int pageInt = Integer.parseInt(page);

			searchCriteria scri = new searchCriteria();

			scri.setPage(pageInt); // 페이지에 필요한 변수 담기

			PageMaker pm = new PageMaker();
			pm.setScri(scri); // <----- PageMaker에 SearchCriteria 담아서 가지고 다닌다

			BoardDao bd = new BoardDao();
			// 페이징처리하기위해 전체데이터 개수 가져오기
			int boardCnt = bd.boardTotalCount(scri);

			pm.setTotalCount(boardCnt);
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);

			request.setAttribute("alist", alist); // request.setAttribute에 왜 담는데?
			request.setAttribute("pm", pm); // 포워드 방식으로 넘기기 때문에 공유가 가능하다

			paramMethod = "F";
			url = "/board/boardList.jsp"; // 실제 내부경로 : 안보여짐

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

	public String getFileName(Part filePart) {

		for (String filePartData : filePart.getHeader("Content-Disposition").split(";")) {
			System.out.println(filePartData);

			if (filePartData.trim().startsWith("filename")) {
				return filePartData.substring(filePartData.indexOf("=") + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	public String getUserIp(HttpServletRequest request) throws Exception { // 클라이언트 ip 주소 뽑는 메서드

		String ip = null;

		ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();

		}

		return ip;
	}

}
