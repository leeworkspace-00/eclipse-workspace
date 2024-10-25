package mvc.vo;

public class searchCriteria extends Criteria { // 검색기능에 사용할 기준이될 클래스
	// Criteria이거 상속 받아서 사용할거임

	private String searchType;
	private String keyword;

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
