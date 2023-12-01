<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Website</title>
    <style>
        body {
            display: flex;
            margin: 0;
            padding: 0;
            height: 100vh;
        }

        #menu {
            width: 20%;
            background-color: #f1f1f1;
            padding: 20px;
        }

        #dashboard {
            flex: 1;
            padding: 20px;
        }
    </style>
</head>
<body>

<div id="menu">
    <h2>메뉴</h2>
    <ul>
        <li><a href="#section1">섹션 1</a></li>
        <li><a href="#section2">섹션 2</a></li>
        <!-- 다른 메뉴 아이템 추가 -->
    </ul>
</div>

<div id="dashboard">
    <h1>대시보드</h1>
    <!-- 대시보드 컨텐츠 추가 -->
    <%-- 여기에 동적인 JSP 코드 추가 가능 --%>
</div>

</body>
</html>