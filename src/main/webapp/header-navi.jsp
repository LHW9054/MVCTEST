<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ヘッダー</title>
    <link rel="stylesheet" href="style.css"> 
</head>
<body>
    <%
        // 세션에서 사용자 ID 확인
        String userId = (String) session.getAttribute("userId");
        
        // 사용자 ID가 없으면 로그인 페이지로 이동
        if (userId == null) {
            request.setAttribute("errorMsg", "ログインしてください。");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
    %>
    
    <!-- 헤더 내용 -->
    <div class="header-container">
        <div class="header-left">
            <h3>ユーザID ： <%= userId %></h3>  <!-- 세션에서 가져온 사용자 ID -->
        </div>
        <div class="header-center">
            <h1><a href="select.jsp" style="text-decoration: none;">Barça</a></h1>
        </div>
        <div class="header-right">
            <h3><a href="logout-servlet">ログアウト</a></h3>
        </div>
    </div>

</body>
</html>
