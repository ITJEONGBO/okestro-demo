<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>It Cloud | 대시보드</title>
    <style>
        body {
            margin: 0;
        }

        header {
            background-color: #59cd74;
            color: #fff;
            padding: 5px;
            /*text-align: center;*/
        }

        ul {
            list-style-type: none;
            margin: 0;
            padding: 0;
            width: 15%;
            background-color: #f1f1f1;
            position: fixed;
            height: 100%;
            overflow: auto;
        }

        li a {
            display: block;
            color: #000;
            padding: 8px 16px;
            text-decoration: none;
        }

        li a.side{
            text-align: right;
            size: 10px;
        }

        li a.active {
            background-color: #7CE193;
            color: white;
        }

        #act{
            background-color: #b1f1bf;
            color: black;
        }

        li a:hover:not(.active) {
            background-color: #555;
            color: white;
        }

        .dashboard-container {
            display: flex;
            justify-content: space-around;
            margin: 20px;
        }

        .widget {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            flex: 1;
            margin: 0 10px;
        }

        .widget h2 {
            color: #333;
        }

        .widget p {
            color: #555;
        }

        td{
            border: #dddddd 1px solid;
        }

    </style>

</head>
<body>

<header>
    <h1>&nbsp;&nbsp; It Cloud</h1>
</header>

<ul>
    <li><a href="/dashboard">대시보드</a></li>
    <li><a href="/computing/vms">컴퓨팅</a></li>
        <li><a class="side" href="/computing/vms">가상머신</a></li>
        <li><a class="side" href="/computing/templates">템플릿</a></li>
        <li><a class="side" href="/computing/hosts">호스트</a></li>
        <li><a class="side" href="/computing/clusters">클러스터</a></li>
    <li><a href="/networks">네트워크</a></li>
    <li><a href="/storage/domains">스토리지</a></li>
        <li><a class="side" href="/storage/domains">도메인</a></li>
        <li><a class="side" href="/storage/disks">디스크</a></li>
    <li><a class="active" href="/admin/user">관리</a></li>
        <li><a class="side" href="/admin/user">사용자</a></li>
        <li><a class="side" href="/admin/instanceType">인스턴스 유형</a></li>
        <li><a class="side" href="/admin/macAddress">맥 주소 풀</a></li>
        <li><a class="side" id="act" href="/admin/setting">설정</a></li>
</ul>

<div style="margin-left:25%; padding:1px 16px; height:1000px; ">

    <div class="dashboard-container">

        <div class="widget">
            <h2>설정-관리</h2>

            <form name="settingForm" id="settingForm" method="post" action="/setting">
                <label for="id">ID</label>
                <input type="text" id="id" name="id" value="${setting.id}">
                <br>

                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" value="${setting.password}">
                <br>

                <label for="ip">IP</label>
                <input type="text" id="ip" name="ip" value="${setting.ip}">
            </form>
        </div>

    </div>


</div>

</body>
</html>


