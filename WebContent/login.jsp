<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<body>
<!-- login_action에 있는 로직이 이제 LoginServlet으로 이동.. 
	 로그인 성공--login_ok.jsp로 이동
	 로그인 실패--login_fail.jsp로 이동
-->
<form action="front.do" name="loginForm" onsubmit="return checkLogin()">
<input type="hidden" name="command" value="login"/>
	아이디 : <input type="text" name="id" required="required"><br>
	패스워드 : <input type="password" name="password" required="required"><br>
	<input type="submit" value="전송">
</form>
</body>
</html>











