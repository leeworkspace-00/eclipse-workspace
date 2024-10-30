package mvc.dbcon;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconn { // 데이터베이스 컨넥션

	private Connection conn; // 멤버변수는 선언만해도 자동 초기화
	private String url = "jdbc:mysql://127.0.0.1/aws0822?serverTimezone=UTC";
	private String user = "root";
	private String password = "1234";

	public Connection getConnection() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("객체생성확인>>>>>" + conn);

		return conn; // 연결객체가 생겨났을 떄의 객체정보를 담고 있는 객체참조 변수
						// 리턴값이 null이면 연결이 되지 않았다는 뜻
	}

	// System.out.println("conn:"+conn);

}
