package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.dao.commentDao;
import mvc.vo.commentVo;

@WebServlet("/CommentController")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String location; // 멤버변수 초기화(전역선언) > 이동할 페이지 담을거임

	public CommentController(String location) {
		this.location = location; // 생성자를 통해서 넘어온 location을 전역 변수에 담아 사용하겟다
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (location.equals("commentList.aws")) {// 댓글
			String bidx = request.getParameter("bidx");
			commentDao cd = new commentDao();
			ArrayList<commentVo> alist = cd.commentSelectAll(Integer.parseInt(bidx));

			int cidx = 0;
			String cwriter = "";
			String ccontents = "";
			String writeday = "";
			String delyn = "";
			String str = "";
			int midx = 0;
			for (int i = 0; i < alist.size(); i++) { // 있는 만큼 반복할거임

				cidx = alist.get(i).getCidx();
				cwriter = alist.get(i).getCwriter();
				ccontents = alist.get(i).getCcontents();
				writeday = alist.get(i).getWriteday();
				delyn = alist.get(i).getDelyn();
				midx = alist.get(i).getMidx();

				String cma = "";
				if (i == alist.size() - 1) { // -1은 컴퓨터에서 없다는 의미임
					cma = "";
				} else {
					cma = ",";
				} // json파일 마지막 콤마 빼기 조건문 (데이터 없으면 마지막에는 , 빼고 출력하라)

				str = str + "{ \"cidx\" : \"" + cidx + " \", \"cwriter\" : \"" + cwriter + "\", \"ccontents\":\""
						+ ccontents + "\",\"writeday\":\"" + writeday + "\",\"delyn\":\"" + delyn + "\",\"midx\":\""
						+ midx + "\" }" + cma;

			}

			PrintWriter out = response.getWriter();
			out.println("[" + str + "]"); // 대괄호 안에 담아야함

		} else if (location.equals("commentWriteAction.aws")) {
			String cwriter = request.getParameter("cwriter");
			String ccontents = request.getParameter("ccontents");
			String bidx = request.getParameter("bidx");
			String midx = request.getParameter("midx");

			commentVo cv = new commentVo();

			cv.setCwriter(cwriter);
			cv.setCcontents(ccontents);
			cv.setBidx(Integer.parseInt(bidx));
			cv.setMidx(Integer.parseInt(midx));

			// Comment 객체생성
			commentDao cd = new commentDao();

			int value = cd.commentInsert(cv);
			System.out.println("value" + value);

			PrintWriter out = response.getWriter();
			String str = "{\"value\":\"" + value + "\"}";
			out.println(str);

			/*
			 * String cwriter = request.getParameter("cwriter"); String ccontents =
			 * request.getParameter("ccontents"); String bidx =
			 * request.getParameter("bidx"); String midx = request.getParameter("midx");
			 * 
			 * commentVo cv = new commentVo(); cv.setWriteday(cwriter);
			 * cv.setCcontents(ccontents); cv.setBidx(Integer.parseInt(bidx));
			 * cv.setMidx(Integer.parseInt(midx)); // 댓글 쓸때 필요한 정보 : 작성자 내용 게시판번호 회원번호
			 * 
			 * commentDao cd = new commentDao(); // comment 객체 생성 int value =
			 * cd.commentInsert(cv); // 삽입 메서드의 리턴값이 1이면 메서드가 동작하도록 ~
			 * System.out.println("value" + value); PrintWriter out = response.getWriter();
			 * String str = "{\"value\":\"" + value + "\"}"; // value 라는 이름으로 1 또는 0인 리턴값을
			 * 출력함 json형식으로 out.println(str); // json 형식으로 웹에 표시해보기
			 */
		} else if (location.equals("commentDeleteAction.aws")) { // 댓글 삭제동작

			String cidx = request.getParameter("cidx");
			System.out.println("cidx 값 확인 : " + cidx); // 000
			// delyn 을 n > y로 업데이트하는 메서드를 만들어서 호출
			commentDao cd = new commentDao(); // comment 객체 생성
			int value = cd.commentDelete(Integer.parseInt(cidx));
			System.out.println("value" + value);

			// 그리고 나서 화면에 실행성공여부를 json파일로 보여준다
			PrintWriter out = response.getWriter();
			String str = "{\"value\":\"" + value + "\"}"; // value 라는 이름으로 1 또는 0인 리턴값을 출력함 json형식으로
			out.println(str);

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
