package MVC.vo;

public class Criteria { // 페이징 기준이 되는 객체들 담아두는 클래스 생성
	private int page = 1; // 페이지 번호를 담는 변수
	private int perPageNum = 15; // 화면에 나타나는 게시물 리스트의 수 num/page

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}

}
