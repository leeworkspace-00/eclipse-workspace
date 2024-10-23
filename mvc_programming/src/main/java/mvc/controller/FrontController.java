package mvc.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FrontController")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String uri = request.getRequestURI(); // 전체주소 가져오기
		// /member/memberJoinAction.aws
		String[] entity = uri.split("/"); // split으로 자르기 > 그리고 배열형태로 만들어준다

		if (entity[1].equals("member")) { // 잘라온 주소의 1번째 값이 member인지 확인하고 멤버면

			MemberController mc = new MemberController(entity[2]);
			mc.doGet(request, response); // 이쪽으로 보내!

		} else if (entity[1].equals("board")) {// 잘라온 주소의 1번째 값이 board인지 확인한다 보드면
			BoardController bc = new BoardController(entity[2]);
			bc.doGet(request, response); // 이쪽으로 보내!

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
