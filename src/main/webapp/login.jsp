<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<link rel="stylesheet" href="style.css">
</head>
<style>
    body {
        overflow-y: hidden;
    }
</style>
<body style="background-image: url('./resource/image/wallpaper.png');background-size: cover;">

<div style="text-align: center;">
    <h2 style="color: red; font-size: 60px; display: inline-block; margin-bottom:0px; margin-top: 10px;">Barça</h2><br>
    <h2 style="color: blue; font-size: 30px; display: inline-block;margin-top:0px;">オンラインショップ</h2><br>
    <h2 style="color: yellow; font-size: 20px; display: inline-block;margin-top:0px;">LogIn</h2>
</div>

<div class="focus_mid">
    <!-- 로그인 폼 -->
    <form action="login-servlet" method="post" class="form-container">
        <label for="userId">ユーザID</label>
        <input type="text" id="userId" name="userId" required><br>
        <label for="password">パスワード</label>
        <input type="password" id="password" name="userpw" required><br><br>
	<div style="display:flex; justify-content: space-between;">
        <!-- 버튼 컨테이너 -->
        <div class="button-container">
            <input type="submit" value="ログイン" class="button">
            <!-- 회원가입 폼 (GET 방식) -->
        </div>
    </form>
    <form action="join.jsp" method="get">
		<input type="submit" value="会員登録" class="button">
    </form>
	</div>
</div>
    
    <% 
    String loginError = (String) session.getAttribute("loginError");
    if (loginError != null) {
	%>
        <script>
            alert("<%= loginError %>");
        </script>
	<%
        // 세션에서 메시지를 삭제
        session.removeAttribute("loginError");
 	   }
	%>

</body>
</html>
