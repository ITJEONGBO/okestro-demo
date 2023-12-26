<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IT CLOUD | 로그인</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>

    <script type="text/javascript">

        $(function(){
            $("#loginBtn").click(function() {
                var id = $("#id").val();
                var password = $("#password").val();

                if(id=="" || password==""){
                    alert("아이디/비밀번호를 적어주세요");
                }else {
                    $("#loginForm").submit();
                }
            })
        })
        /*function btn() {
            alert($("#id").val());
        }*/
    </script>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f4f4f4;
        }

        .login-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .login-container h2 {
            text-align: center;
            color: #333;
        }

        .login-form {
            display: flex;
            flex-direction: column;
            margin-top: 20px;
        }

        .login-form label {
            margin-bottom: 8px;
            color: #555;
        }

        .login-form input {
            padding: 10px;
            margin-bottom: 16px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }

        .login-form button {
            padding: 12px;
            background-color: #7CE193;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .login-form button:hover {
            background-color: #555;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>로그인</h2>
    <form class="login-form" name="loginForm" id="loginForm" method="post" action="/login_check">
        <label for="id">사용자명:</label>
        <input type="text" id="id" name="id" required>

        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required>

        <%--submit은 고쳐야됨--%>
        <button type="button" id="loginBtn">로그인</button>
        <br>
        <a href="/dashboard">dashboard</a>
    </form>

    <br><br><br>
    <div>
        <label><a href="#void">인증서 다운로드</a> </label>
    </div>
</div>

</body>
</html>
