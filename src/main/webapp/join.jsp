<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会員登録</title>
    <link rel="stylesheet" href="style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body style="background-image: url('./resource/image/wallpaper.png');background-size: cover;">

<div style="text-align: center;">
    <h2 style="color: red; font-size: 60px; display: inline-block; margin-bottom: 0px; margin-top: 10px;">Barça</h2><br>
    <h2 style="color: blue; font-size: 30px; display: inline-block; margin-top: 0px;">オンラインショップ</h2><br>
    <h2 style="color: yellow; font-size: 20px; display: inline-block; margin-top: 0px;">会員登録</h2>
</div>

<div class="focus_mid">
    <form id="joinForm" method="post" action="/MVCTEST/join-servlet">
        <!-- 아이디 입력 & 중복 확인 버튼 -->
        <label for="userId">使用するID</label>
		<div style="display: flex; align-items: center; gap: 5px;">
		    <input type="text" id="userId" name="userId" required style="height: 40px;">
		    <button type="button" onclick="checkId()" class="button" style="height: 40px; margin-bottom: 15px;">IDcheck</button>
		</div>

        <span id="idCheckResult" style="font-weight: bold;"></span><br><br>
        <!-- 비밀번호 입력 필드 -->
        <label for="password">パスワード</label>
        <input type="password" id="password" name="userpw" required oninput="checkPasswordMatch()"><br>

        <label for="confirmPassword">パスワード確認</label>
        <input type="password" id="confirmPassword" required oninput="checkPasswordMatch()">
        <span id="passwordMatchResult" style="font-weight: bold;"></span><br>
        <br>
        <!-- 이름 입력 필드 -->
        <label for="name">名前</label>
        <input type="text" id="name" name="username" required><br>

        <!-- 이메일 입력 필드 -->
        <label for="email">メールアドレス</label><br><br>
        <input type="email" id="email" name="email" required><br><br>

        <!-- 전화번호 입력 필드 -->
        <label for="phonenum">電話番号</label>
        <input type="text" id="phonenum" name="phone" required><br>

        <!-- 버튼 영역 -->
        <div style="display: flex; justify-content: space-between; width: 100%; gap: 30px;">
			<button type="button" onclick="location.href='login.jsp'" class="button">Back</button>
			<button type="submit" id="registerButton" class="button" disabled>会員登録</button>
		</div>

    </form>
</div>

<script>
    var isIdChecked = false;  // ID重複確認 確認可否
    var isPasswordValid = false;  // パスワード 確認可否

    // ID重複確認
    function checkId() {
        var userId = $("#userId").val();
        if (userId === "") {
            alert("IDを入力してください。");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/MVCTEST/join-servlet",
            data: { userId: userId, checkIdOnly: true },
            success: function(response) {
                var resultSpan = $("#idCheckResult");
                if (response === "available") {
                    resultSpan.text("使用可能").css("color", "blue").show();
                    isIdChecked = true;
                } else {
                    resultSpan.text("使用不可").css("color", "red").show();
                    isIdChecked = false;
                }
                updateRegisterButton();
            },
            error: function() {
                alert("エラーが発生しました。");
            }
        });
    }

    // パスワード確認(一致するかチェック)
    function checkPasswordMatch() {
        var password = $("#password").val();
        var confirmPassword = $("#confirmPassword").val();
        var resultSpan = $("#passwordMatchResult");

        if (password.length > 0 && confirmPassword.length > 0) {
            if (password === confirmPassword) {
                resultSpan.text("一致").css("color", "blue").show();
                isPasswordValid = true;
            } else {
                resultSpan.text("不一致").css("color", "red").show();
                isPasswordValid = false;
            }
        } else {
            resultSpan.text("").hide();
            isPasswordValid = false;
        }
        updateRegisterButton();
    }

    // 会員登録ボタン活性化可否チェック
    function updateRegisterButton() {
        var registerButton = $("#registerButton");
        if (isIdChecked && isPasswordValid) {
            registerButton.prop("disabled", false);
        } else {
            registerButton.prop("disabled", true);
        }
    }

    // 会員登録ボタンをクリックすると最終確認できます
    $("#joinForm").submit(function(event) {
        if (!isIdChecked) {
            alert("IDの重複確認をしてください。");
            event.preventDefault(); 
        } else if (!isPasswordValid) {
            alert("パスワードが一致していません。");
            event.preventDefault();
        }
    });
</script>

</body>
</html>
