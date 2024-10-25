package mvc.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FrontController") // @WebServlet : 프론트 컨트롤러 주소값이 오면 아래의 내용 처리함
@MultipartConfig( // 멀티파일을 설정한다
		fileSizeThreshold = 1024 * 1024 * 1, // 1mb
		maxFileSize = 1024 * 1024 * 10, // 10mb
		maxRequestSize = 1024 * 1024 * 15, // 15mb
		location = "D:/dev/temp"// 임시로 보관하는 위치 (물리적으로 만들어놔야 한다: dev폴더로 가서 새폴더> 이름 지정하고 만들어주기)
)

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8"); // 한글파일도 깨지지 않게 올라가게 하기 위한 설정

		String uri = request.getRequestURI(); // 전체주소 가져오기
		// /member/memberJoinAction.aws
		String[] entity = uri.split("/"); // split으로 자르기 > 그리고 배열형태로 만들어준다

		if (entity[1].equals("member")) { // 잘라온 주소의 1번째 값이 member인지 확인하고 멤버면

			MemberController mc = new MemberController(entity[2]);
			mc.doGet(request, response); // 이쪽으로 보내!

		} else if (entity[1].equals("board")) {// 잘라온 주소의 1번째 값이 board인지 확인한다 보드면
			BoardController bc = new BoardController(entity[2]);
			bc.doGet(request, response); // 이쪽으로 보내! // >> 여기 디버깅

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response); // >> 여기 디버깅
	}

}
