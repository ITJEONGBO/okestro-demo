<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>It Cloud</title>

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
    <p align="right">로그인한 사용자</p>
</header>

<ul>
    <li><a href="/dashboard">대시보드</a></li>
    <li><a class="active" href="/computing/vms">컴퓨팅</a></li>
        <li><a class="side" href="/computing/vms">가상머신</a></li>
        <li><a class="side" href="/computing/templates">템플릿</a></li>
        <li><a class="side" id="act" href="/computing/hosts">호스트</a></li>
        <li><a class="side" href="/computing/clusters">클러스터</a></li>
    <li><a href="/networks">네트워크</a></li>
    <li><a href="/storage/domains">스토리지</a></li>
        <li><a class="side" href="/storage/domains">도메인</a></li>
        <li><a class="side" href="/storage/disks">디스크</a></li>
    <li><a href="/admin/user">관리</a></li>
        <li><a class="side" href="/admin/user">사용자</a></li>
        <li><a class="side" href="/admin/instanceType">인스턴스 유형</a></li>
        <li><a class="side" href="/admin/macAddress">맥 주소 풀</a></li>
        <li><a class="side" href="/admin/setting">설정</a></li>
</ul>

<div style="margin-left:25%; padding:1px 16px; height:1000px; ">

    <div class="dashboard-container">

        <div class="widget">
            <h2>호스트</h2>

            <table width="1100px">
                <thead>
                    <th>상태</th>
                    <th>이름</th>
                    <th>설명</th>
                    <th>IP</th>
                    <th>클러스터</th>
                    <th>가상머신 수</th>
                    <th>CPU</th>
                    <th>메모리</th>
                    <th>네트워크</th>
                </thead>

                <c:if test="${empty hostVOList}">
                    <tbody>
                    <tr>
                        <td colspan="6" align="center">템플릿이 없음</td>
                    </tr>
                    </tbody>
                </c:if>
                <c:forEach var="hostList" items="${hostVOList}" varStatus="status">
                    <tbody align="center">
                    <tr>
                        <td>${hostList.status}</td>
                        <td>${hostList.name}</td>
                        <td>${hostList.description}</td>
                        <td>${hostList.address}</td>
                        <td>네트워크</td>
                    </tr>
                    </tbody>
                </c:forEach>
            </table>

        </div>
    </div>


</div>

</body>
</html>


