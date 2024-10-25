<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@page import="java.util.*" %>   
 <%@page import="mvc.vo.*" %>    
 <%
 ArrayList<BoardVo>alist = (ArrayList<BoardVo>)request.getAttribute("alist"); // 형변환 때문에 경고라인 뜬거 걱정 말기
	//System.out.println("뭐가 나오나?"+alist); > 객체생성된거 확인해보기  인덱스창에서 목록 간다음 콘솔창 확인 	
	PageMaker pm = (PageMaker)request.getAttribute("pm");	// 여기서 pm 객체 꺼내옴
	String keyword= pm.getScri().getKeyword();
	String searchType = pm.getScri().getSearchType();	
	String param = "keyword="+keyword+"&searchType="+searchType+"";		// 파라미터 변수에 검색이랑 검색종류 가지고 다닐거임	
	int totalCount = pm.getTotalCount();
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글목록</title>
<link href="../board/boardStyle.css" rel="stylesheet">
<script>
	function postwrite() {
		var link = '<%=request.getContextPath() %>/board/boardWrite.aws';		
		location.href =link;	
	}
</script>
</head>

<body style="width: 800px; margin: 0 auto;">
<h2>글목록</h2>
<hr>
<form class="search" name="frm" action="<%=request.getContextPath() %>/board/boardList.aws" method="get">
		<select name="searchType">
			<option value="subject">제목</option>
			<option value="writer">작성자</option>
		</select>
		<input type="text" name="keyword">
		<button  type="submit" class="btn">검색</button>
	</form>



<!-- <div>

	<input type="button" id="searchBtn" onclick="searchBtn();" value="검색" style="background-color:black; color:white; float: right;">
		<select name="searchType" style="float: right;">
				<option value="subject" selected>제목</option>
				<option value="contents" >내용</option>
				<option value="writer">작성자</option>
				<option value="최신순">최신순</option>
		</select>

		
</div> -->
<p>
	<table class = "listTable" >

			<tr>
				<th>No</th>					
				<th>제목</th>				
				<th>작성자</th>				
				<th>조회</th>				
				<th>추천수</th>				
				<th>날짜</th>				
			</tr>
		
		
		<%
		
		int num = totalCount-(pm.getScri().getPage()-1)*pm.getScri().getPerPageNum();	// 게시물 번호를 적용시키는 변수 
		

		for(BoardVo bv:alist){
			String lvlStr = "";
			
			for(int i = 1; i<=bv.getLevel_(); i++) {
				lvlStr = lvlStr+"&nbsp;&nbsp;";		// 옆으로 두칸 띄우고 다음 문자 연결하기 css 문법
				if(i==bv.getLevel_()){
					lvlStr =lvlStr + "ㄴ";	
				}
			}
				
			%>
			<tr>
			
				<td><%=num%></td>
				<td>
				<%=lvlStr %>
					<a href="<%=request.getContextPath()%>/board/boardContents.aws?bidx=<%=bv.getBidx()%>"><%=bv.getSubject() %></a>
				</td>
				<td><%=bv.getWriter()%></td>
				<td><%=bv.getViewcnt()%></td>
				<td><%=bv.getRecom()%></td>
				<td><%=bv.getWriteday() %></td>
			</tr>			
			<%			
		num = num-1; // 하나씩 빼 > 왜?
		} %>						
	</table>
	<input type = "button" id="postUpdate" value="글쓰기" onclick="postwrite();" style="background-color:black; color:white; float: right;">
	<a href="<%=request.getContextPath()%>/board/boardWrite.aws"></a>
	<br>
</body>



	<div class = "page">
		<ul style = "text-align: center;">	
		<%if(pm.isPrev()==true){ %>	
		<li style = "display:inline;"> 
			<a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getStartPage()-1%>&<%=param%>">◀</a> <!-- 이전버튼생성 -->
			<%} %>
			</li>
			
			<% for(int i = pm.getStartPage();i<=pm.getEndPage();i++){ %>
			
			<li style = "display:inline;">
			<input type="button" value = "<%=i%>" onclick="location.href='<%=request.getContextPath() %>/board/boardList.aws?page=<%=i%>&<%=param%>' "<%if (i ==pm.getScri().getPage()) {%>
			  style="background-color:black; color:white; display:inline-block;"<%} %>>
			</li>  <!-- 하단 페이지번호생성 -->
			
			<%} %>
					
			<%if(pm.isNext()==true&&pm.getEndPage()>0){%>
			<li style = "display:inline;"><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getEndPage()+1%>&<%=param%>">▶</a> <!-- 다음버튼생성 -->
			</li>
			<%} %>
			
	</ul>
</div>

</body>
</html>