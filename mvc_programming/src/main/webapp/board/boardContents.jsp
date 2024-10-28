<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="mvc.vo.BoardVo"%>
    <%@ include file ="/common/loginCheck.jsp"%>	

   <%
   BoardVo bv = (BoardVo)request.getAttribute("bv");			// 강제 형변환으로 양쪽의 타입을 맞춰주자
   String memberName =""; 
   if(session.getAttribute("memberName")!=null) {
	   memberName = (String)session.getAttribute("memberName");
   }
  
   %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글내용</title>
<link href="../board/boardStyle.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-latest.min.js"></script> <!-- mvc jquery-CDN주소 추가  -->
<script>
// 유효성검사 지움

	$.boardCommentList = function() {		// jquery 함수 만드는 문법 
		alert("확인용");
		$.ajax({	// 댓글쓰기 버튼
			type :"get",	//	전송방식 : get방식으로 전송하겠다 선언
			url : "<%=request.getContextPath()%>/comment/commentList.aws?bidx=<%=bv.getBidx()%>",
			dataType : "json",	
				success : function(result) {		// 결과가 넘어와서 성공한 경우 받는 영역
					alert("전송성공테스트");
	
			},
			error : function(result){  //결과가 실패했을때 받는 영역
				alert("전송실패 테스트");	
			}
	
		});
		
		
		
		
	}
$(document).ready(function(){
	alert("dddddz");
	$.boardCommentList();		// 시작하자마자 동작해야함
	
	$("#btn").click(function() {
			//alert("추천버튼 클릭확인");		
		$.ajax({
			type :"get",	//	전송방식 : get방식으로 전송하겠다 선언
			url : "<%=request.getContextPath()%>/board/boardRecom.aws?bidx=<%=bv.getBidx()%>",
			dataType : "json",		//json : 문서에서 {"키값":"value값","키값2:"value값2"}
			success : function(result) {		// 결과가 넘어와서 성공한 경우 받는 영역
			//data : {"recom":recom},		// get방식으로 전송하기 때문에  데이터 타입은 필요 없어요
			var str = "추천:"+result.recom+"";
				$("#btn").val(str);

	
			},
			error : function(){  //결과가 실패했을때 받는 영역
				alert("전송실패 테스트");	
			}
	
		});
		

	});
		
	$("#cmtBtn").click(function() {
		
		let loginCheck = "<%=session.getAttribute("midx")%>";
		if(loginCheck ==""||loginCheck==null||loginCheck=="null")  {
			alert=("로그인해주세요");			
			return;
		}

		let cwriter =$("#cwriter").val();	// 값 꺼내서 담기 
		let ccontents =$("#ccontents").val();
		
		if(ccontents=="") {
			alert("내용을 입력해주세요");
			$("#ccontents").focus();
			return;
		}	else if(cwriter =="" ) {
			alert("작성자를 입력해주세요");			
			$("#cwriter").focus();
			return;

			
		}
		
		 $.ajax({	// 댓글쓰기 버튼
			type :"post",	//	전송방식 : get방식으로 전송하겠다 선언
			url : "<%=request.getContextPath()%>/comment/commentWriteAction.aws",
			
			
			data : {"cwriter":cwriter,
				"ccontents":ccontents,
				"bidx":<%=bv.getBidx()%>,
				"midx":<%=session.getAttribute("midx")%>
				},
				dataType : "json",		//json : 문서에서 {"키값":"value값","키값2:"value값2"}
				
				success : function(result) {		// 결과가 넘어와서 성공한 경우 받는 영역
			var str = "추천:"+result.recom+")";
				$("#btn").val(str);

	
			},
			error : function(){  //결과가 실패했을때 받는 영역
				alert("전송실패 테스트");	
			}
	
		});

	}); 
	
});


</script>
</head>
<body>
<header>
	<h2 class = "mainTitle">글내용</h2>
</header>

<article class = "detailContents">
	<h2 class = "contentTitle"><%=bv.getSubject() %> (조회수:<%=bv.getViewcnt() %>)
	<input type = "button" id = "btn" value = "추천(<%=bv.getRecom() %>)">
	</h2>
	
	<p class="write"><%=bv.getWriter() %> (<%=bv.getWriteday() %>)</p>
	<hr>
	<div class="content">
		<%=bv.getContents() %>	
	</div>
	
	<% if (bv.getFilename() == null || bv.getFilename().equals("") ) {}else{ %>	
	<img src="<%=request.getContextPath() %>/images/<%=bv.getFilename() %>">	
	<p>
	<a href="<%=request.getContextPath() %>/board/boardDownload.aws?filename=<%=bv.getFilename() %>" class="fileDown">	
	첨부파일 다운로드</a>
	</p>	
	<%} %>

</article>



<div class = "btnBox">
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardModify.aws?bidx=<%=bv.getBidx()%>">수정</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardDelete.aws?bidx=<%=bv.getBidx()%>">삭제</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardReply.aws?bidx=<%=bv.getBidx()%>">답변</a>
	<a class="btn aBtn" href="<%=request.getContextPath() %>/board/boardList.aws">목록</a>
	<%-- <input type="button" name="list" value = "목록" onclick = "location.href='<%=request.getContextPath()%>/board/boardList.aws?bidx=<%=bv.getBidx()%>'"> --%>
</div>
	
<article class ="commentContents">
	<form name = "frm">
		<p class="commentWriter">
		<input type ="text" id = "cwriter" name="cwriter" value = "<%=memberName%>" readonly="readonly" style="width:100px;border:0px;">
		</p>
		<input type="text" id="ccontents"  name="ccontents">
		<button type="button" id="cmtBtn" class="cmtBtn">댓글쓰기</button>
		</form>
	



<table class="replyTable">
		<tr>
			<th>번호</th>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>DEL</th>
		</tr>
		<tr>
			<td>1</td>
			<td>홍길동</td>
			<td class="content">댓글입니다</td>
			<td>2024-10-18</td>
			<td>N</td>
		</tr>
	</table>
		</article>



</body>
</html>