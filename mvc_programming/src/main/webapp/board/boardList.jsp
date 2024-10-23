<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@page import="java.util.*" %>   
 <%@page import="mvc.vo.*" %>  
    
 <%
 ArrayList<BoardVo>alist = (ArrayList<BoardVo>)request.getAttribute("alist"); // 형변환 때문에 경고라인 뜬거 걱정 말기
	//System.out.println("뭐가 나오나?"+alist); > 객체생성된거 확인해보기  인덱스창에서 목록 간다음 콘솔창 확인 
	
	PageMaker pm = (PageMaker)request.getAttribute("pm");	// 여기서 pm 객체 꺼내옴
 %>
  
<!DOCTYPE html>
<html>


<head>
<meta charset="UTF-8">
<title>글목록</title>

<script>
	function postwrite() {
		var link = '<%=request.getContextPath() %>/board/boardWrite.aws';
		
		location.href =link;
		
	
		/* alert("글쓰기버튼을 누르셨습니다"); */
	}
	function searchBtn() {
		alert("검색버튼을 누르셨습니다");
	}
	
	

</script>

</head>


<body style="width: 800px; margin: 0 auto; text-align: center;">

<hr>
<h3>글목록</h3>
<hr>
<div>

	<input type="button" id="searchBtn" onclick="searchBtn();" value="검색" style="background-color:black; color:white; float: right;">
	<input type="text" id="searchBox" value="" style="float: right;">
		<select name="option" style="float: right;">
				<option value="제목" selected>제목</option>
				<option value="내용" >내용</option>
				<option value="작성자">작성자</option>
				<option value="최신순">최신순</option>
		</select>
		
</div>
<p>
	<table style=" width:800px; text-align: center; margin:auto;">

			<tr>
				<th>No</th>
				<th>제목</th>
				<th>작성자</th>
				<th>조회</th>
				<th>추천수</th>
				<th>날짜</th>
				
			</tr>

		<%for(BoardVo bv:alist){ %>
			<tr>
				<td><%=bv.getBidx() %></td>
				<td><a href="<%=request.getContextPath()%>/board/boardContents.aws?bidx=<%=bv.getBidx()%>"><%=bv.getSubject() %></a></td>
				<td><%=bv.getWriter()%></td>
				<td><%=bv.getViewcnt()%></td>
				<td><%=bv.getRecom()%></td>
				<td><%=bv.getWriteday() %></td>
			</tr>
			<%} %>
			
			
	</table>
	<input type = "button" id="postUpdate" value="글쓰기" onclick="postwrite();" style="background-color:black; color:white; float: right;">
	<a href="<%=request.getContextPath()%>/board/boardWrite.aws"></a>
	<br>
</body>



	<div class = "page">
		<ul style = "text-align: center;">	
		<%if(pm.isPrev()==true){ %>	
		<li style = "display:inline;"> 
			<a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getStartPage()-1%>">◀</a> <!-- 이전버튼생성 -->
			<%} %>
			</li>
			
			<% for(int i = pm.getStartPage();i<=pm.getEndPage();i++){ %>
			
			<li style = "display:inline;">
			<input type="button" value = "<%=i%>" onclick="location.href='<%=request.getContextPath() %>/board/boardList.aws?page=<%=i%>' "<%if (i ==pm.getCri().getPage()) {%>
			  style="background-color:black; color:white; display:inline-block;"<%} %>>
			</li>  <!-- 하단 페이지번호생성 -->
			
			<%} %>
					
			<%if(pm.isNext()==true&&pm.getEndPage()>0){%>
			<li style = "display:inline;"><a href="<%=request.getContextPath()%>/board/boardList.aws?page=<%=pm.getEndPage()+1%>">▶</a> <!-- 다음버튼생성 -->
			</li>
			<%} %>
			
	</ul>
</div>

</body>
</html>