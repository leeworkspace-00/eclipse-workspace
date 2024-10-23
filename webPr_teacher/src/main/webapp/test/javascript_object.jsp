<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자바스크립트 객체 연습</title>
</head>
<body>
	<script>
	//자바스크립트 3가지 객체
	//1. 코어객체 (내장되어있음-사용하기만 하면됨)
	//2. DOM객체 (태그로 표현함)
	//3. BOM객체 (브라우저를 컨트롤할수 있게 만들어진 객체)
	
	//1. 코어객체 
	//1-1 배열객체
	// []표현할수 있고 Array객체로 할수도 있다
	
	//배열객체 생성
	let arrName = ["형찬","은지","이슬","인서"];
	
	let value =  arrName[0];
	document.write("첫번째 사람의 이름은? "+value+"<br>");
	arrName[4] = "경현";
	document.write("다섯번째 사람의 이름은? "+arrName[4]+"<br>");
	arrName[7] = "형구";
	document.write("여섯번째 사람의 이름은? "+arrName[5]+"<br>");
	let intRoom = arrName.length;
	document.write("생성된 총 방의 갯수? "+intRoom+"<br>");
	
	document.write("<br>Array객체로 생성<br>");
	//Array객체로 생성
	let arrDay = new Array("월","화","수","목","금","토","일");
	for(let i=0;i<arrDay.length;i++){
		document.write(arrDay[i]+"<br>");						
	}
	
	//나중에 배열값을 담을수도 있다
	let week = new Array(7);   //방 개수부터 만든다
	week[0] = "월";
	week[1] = "화";
	week[2] = "수";
	week[3] = "목";
	week[4] = "금";
	week[5] = "토";
	week[6] = "일";
	
	
	//몇개를 앞으로 만들지 모르겠다 할때 배열객체 생성
	let day  = new Array();
	day[0] = "월";   				//자동으로 크기가 증가한다 1
	day[1] = "화";					//자동으로 크기가 증가한다 2
	
	
	document.write("배열의 길이를 나타내는 length 프로퍼티<br>");
	let arrlength = day.length;
	document.write("배열의 크기는?"+arrlength+"<br>");
	day.length = 1;
	document.write("1개로 줄였을때 두번째 배열방에 값이 있나요?"+day[1]+"<br>");
	
	
	//문제
	//수학 점수가 90점 영어점수가 80점 국어점수가 75점인 사람의 총합과 평균값을 
	//배열을 통해서 구현하시오
	
	//1.배열선언하고 점수세팅
	//2.반복문으로 합산
	//3.배열길이로 나누기
	
	//첫번째 방식
	/* let a =  new Array(3);
	a[0] = 90;
	a[1] = 80;
	a[2] = 75; */
	
	//두번째방식
	/* let a = [90,80,75]; */
	
	//세번째 방식
	/* let a = new Array(90,80,75); */
	
	//네번째 방식 -크기가 추가할때마다 자동증가
	let a = new Array();   
	a[0] = 90;
	a[1] = 80;
	a[2] = 75;
	
	let sum =0;  //총합담는 변수
	for (let i=0; i<a.length; i++){  //배열값 반복 추출
		sum = sum +a[i];		  //각 값을 총합변수에 누적시킴
	}
	document.write("총합은?"+sum);
	
	document.write("자바스크립트 배열값는 모든 타입의 값을 넣을수 있다");
	
	//let any = new Array(5);   //5개의 배열 방을 만들자
	
	// any[0] = 0;   //숫자담기
	// any[1] = "한글";  //문자열담기
	// any[2] = new Date();  // 객체저장(주소값)
	// any[3] = 함수이름;         // 함수의 주소값 저장    	
	 
	document.write("<br>");
	
	 //Array객체의 메소드 사용하기
	 //concat() 합치기
	 let c = new Array("황","김","박");
	 let d = new Array("최");
	 let e;
	 
	 e = c.concat(d);   //두개 붙이는구나
	 document.write("e값은?"+e+"<br>");
	 
	 e="";
	 e = c.join("--"); //연결하는구나
	 document.write("e값은?"+e+"<br>");
	 
	 e="";
	 e= c.reverse();
	 document.write("e값은?"+e+"<br>");
	 document.write("c값은?"+c+"<br>");
	 
	 e="";
	 e = c.toString();
	 document.write("e값은?"+e+"<br>");
	 
	 e="";
	//c는 박,김,황 들어있음 역순으로
	 e = c.slice(1,3);   //1번 다음 자리부터 3번자리까지
	 document.write("e값은?"+e+"<br>");
	 document.write("c값은?"+c+"<br>");
	 
	 e="";
	 e= c.sort();  // 정렬해준다
	 document.write("e값은?"+e+"<br>");
	 
	 
	
	</script>
</body>
</html>