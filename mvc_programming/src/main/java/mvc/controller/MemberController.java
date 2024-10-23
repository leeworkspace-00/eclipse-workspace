package mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mvc.dao.MemberDao;
import mvc.vo.MemberVo;

@WebServlet("/MemberControllor") // 서블릿 : 자바로 만든 웹 페이지이다(접속주소는 : /MemberControllor 이렇게 나타난다.)
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String location; // 멤버변수 초기화(전역선언)>이동할 페이지 담을거임

	public MemberController(String location) {
		this.location = location; // 생성자를 통해서 넘어온 location을 전역 변수에 담아 사용하겟
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		// 넘어온 모든 값은 여기에서 처리해 분기한다 - controller의 역할

		// System.out.println("값이 넘어 오나요?");
		// String uri = request.getRequestURI();// 전체주소를 뽑는 메서드 문자열로 가져옴
		// /mvc_programming/member/memberJoinAction.aws
		// System.out.println("uri" + uri);
		String paramMethod = ""; // 새로 전역변수 설정해서 모든 블록에서 사용할 수 있도록 함 + 전송방식에 따라서 변수에 s f를 담아서 사용할 것
		String url = ""; // 새로 전역변수 설정해서 모든 블록에서 사용할 수 있도록 함

		// String[] location = uri.split("/"); // > split : /로 문자열 나눠서 배열에 담아주는 메서드

		if (location.equals("memberJoinAction.aws")) {
			String memberId = request.getParameter("memberid");
			String memberPwd = request.getParameter("memberpwd");
			String memberPwd2 = request.getParameter("memberpwd2");
			String memberName = request.getParameter("membername");
			String memberEmail = request.getParameter("memberemail");
			String memberPhone = request.getParameter("memberphone");
			String memberAddr = request.getParameter("memberaddr");
			String memberGender = request.getParameter("membergender");
			String memberBirth = request.getParameter("memberbirth");
			String[] memberHobby = request.getParameterValues("memberhobby");
			String memberInHobby = "";
			for (int i = 0; i < memberHobby.length; i++) {
				memberInHobby = memberInHobby + memberHobby[i] + ",";
			} // if 하나 끝

			MemberDao md = new MemberDao();
			int value = md.memberInsert(memberId, memberPwd, memberName, memberGender, memberPhone, memberAddr,
					memberEmail, memberBirth, memberInHobby);

			String msg = "";

			HttpSession session = request.getSession(); // 세션객체 활용

			if (value == 1) { // index.jsp 파일은 web.xml 웹 설정파일에 기본 등록 되어있어서 생략 가능
				msg = "회원가입 되었습니다";
				session.setAttribute("msg", msg);
				url = request.getContextPath() + "/"; // request.getContextPath(): 프로젝트이름
				// response.sendRedirect(pageUrl); // 전송방식 sendRedirect는 요청하면 다시 그 쪽으로 가라고 지시하는
				// 방법

			} else {
				msg = "회원가입 오류발생하였습니다";
				session.setAttribute("msg", msg);
				url = request.getContextPath() + "/member/memberJoin.jsp";
				// response.sendRedirect(pageUrl); // 새롭게 다른쪽으로 가라고 지시하는 메서드 샌드리다이렉트 > "S"
			}

			paramMethod = "S"; // 밑에서 sendRedirect방식으로 처리한다
//			System.out.println("msg는?" + msg);

		} else if (location.equals("memberJoin.aws")) {
			// System.out.println("들어왔나"); 확인 해보는
			url = "/member/memberJoin.jsp";
			paramMethod = "F"; // 하단에서 포워드로 처리

			// RequestDispatcher rd = request.getRequestDispatcher(uri2); > 포워드 방식 : "F"
			// System.out.println("rd객체가 생겼나?"+rd);
			// rd.forward(request, response); // 포워드 방식 : 내부 안에서 받아서 넘겨주겠다는 뜻

		} else if (location.equals("memberLogin.aws")) {
			// System.out.println("들어왔나");
			url = "/member/memberLogin.jsp";
			paramMethod = "F"; // 하단에서 포워드로 처리
			// RequestDispatcher rd = request.getRequestDispatcher(uri2);
			// rd.forward(request, response);

		} else if (location.equals("memberLoginAction.aws")) {
			// System.out.println("memberLoginAction 들어왔나");

			String memberId = request.getParameter("memberid");
			String memberPwd = request.getParameter("memberpwd");

			MemberDao md = new MemberDao();
			MemberVo mv = md.memberLoginCheck(memberId, memberPwd);

			// System.out.println("mv객체가 생겼나요?" + mv);

			if (mv == null) {

				url = request.getContextPath() + "/member/memberLogin.aws";
				paramMethod = "S";

				// response.sendRedirect(request.getContextPath() + "/member/memberLogin.aws");
				// // 데이터 없으면 해당주소로 다시 가세요

			} else {
				// 해당되는 로그인 사용자가 있으면 세션에 정보 담아서 메인으로 보내기
				String mid = mv.getMemberid(); // 아이디꺼내기
				int midx = mv.getMidx(); // 회원번호꺼내기
				String memberName = mv.getMembername(); // 이름꺼내기

				HttpSession session = request.getSession();
				session.setAttribute("midx", mid);
				session.setAttribute("midx", midx);
				session.setAttribute("memberName", memberName);

				url = request.getContextPath() + "/";
				paramMethod = "S";

				// response.sendRedirect(request.getContextPath() + "/");// 로그인되었으면 메인으로 가세요

			}
		} else if (location.equals("memberLogout.aws")) {
			// System.out.println("memberLogout");

			// 세션 삭제
			HttpSession session = request.getSession();
			session.removeAttribute("mid");
			session.removeAttribute("midx");
			session.removeAttribute("memberName");
			session.invalidate(); // 초기화

			url = request.getContextPath() + "/";
			paramMethod = "S"; // 샌드리다이렉트

			// response.sendRedirect(request.getContextPath() + "/"); // 로그인 되었으면 메인으로 가세요

		} else if (location.equals("memberlist.aws")) {
			System.out.println("memberlist.aws");

			MemberDao md = new MemberDao();
			ArrayList<MemberVo> alist = md.memberSelectAll();

			request.setAttribute("alist", alist);

			url = "/member/memberlist.jsp";
			paramMethod = "F";

			// 1. 메서드 불러서 처리하는 코드를 만들어야겠지?
			// 2. 보여줄 페이지를 포워드 방식으로 처리해야겠지? > 공유의 특성을 가진다
			// String uri2 = "/member/memberlist.jsp";
			// RequestDispatcher rd = request.getRequestDispatcher(uri2);
			// rd.forward(request, response);

		} else if (location.equals("memberIdCheck.aws")) {
			System.out.println("memberIdCheck.aws");

			String memberId = request.getParameter("memberid");

			MemberDao mv = new MemberDao();
			int cnt = mv.memberIdCheck(memberId);
			// System.out.println("cnt : " + cnt);

			PrintWriter out = response.getWriter();
			out.println("{\"cnt\":\"" + cnt + "\"}"); // ({\"키값\":\"value값\"}"); > json형식

		}

		// 아래로 if 는 else if 말고 새로운 if
		// 구문임===============================================================================================
		// 전송방식이 sendRedirect이면 S forward 방식이면 F로 리턴받을거임

		if (paramMethod.equals("F")) { // 파라미터값이 F면
			RequestDispatcher rd = request.getRequestDispatcher(url); // 포워드방식으로
			rd.forward(request, response); // 이게 포워드
		} else if (paramMethod.equals("S")) { // 아니면 샌드리다이렉트 방식으로 처리하겠다는
			response.sendRedirect(url); // 이게 샌드리다이렉트
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
