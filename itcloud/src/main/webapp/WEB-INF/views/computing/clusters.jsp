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
            background-color: #535c55;
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
            text-align: left;
            size: 10px;
        }


        li a:hover:not(.active) {
            background-color: #555;
            color: #ffffff;
        }

        .cluster-container {
            display: flex;
            justify-content: space-around;
            margin: 15px;
        }

        .widget h2 {
            color: #333;
        }

        .widget p {
            color: #555;
        }

        table{
            border: #535c55 1px solid;
            border-collapse: collapse;
        }

        tr,td{
            border: #535c55 1px solid;
            text-align: center;
            padding: 10px;
        }

    </style>

</head>
<body>

<header>
    <h1>&nbsp;&nbsp; It Cloud</h1>
</header>

<ul>
    <li><a class="side" href="/computing/datacenters">DataCenter</a></li>
    <li><a class="side" href="/computing/clusters">Clsuter</a></li>
    <li><a class="side" href="/computing/hosts">Host</a></li>
    <li><a class="side" href="/computing/vms">vm</a></li>
    <li><a class="side" href="/storage">Storage</a></li>
</ul>

<h2 align="center">클러스터</h2>
<div class="cluster-container">
    <table align="center">
        <tr>
            <td>상태</td>
            <td>이름</td>
            <td>코멘트</td>
            <td>호환 버전</td>
            <td>설명</td>
            <td>클러스터 CPU 유형</td>
            <td>호스트 수</td>
            <td>가상머신 수</td>
            <td>업그레이드 상태</td>
        </tr>

    <c:if test="${empty clusters}">
        <tr>
            <td>클러스터가 없음</td>
        </tr>
    </c:if>
    <c:forEach var="clusters" items="${clusters}" varStatus="status">
        <tr>
            <td>${clusters.status}</td>
            <td> <a href="/computing/cluster?id=${clusters.id}">${clusters.name}</a> </td>
            <td>${clusters.comment}</td>
            <td>${clusters.version}</td>
            <td>${clusters.description}</td>
            <td>${clusters.cpuType}</td>
            <td>${clusters.hostCnt}</td>
            <td>${clusters.vmCnt}</td>
            <td>업그레이드 상태</td>
        </tr>
    </c:forEach>
    </table>




</div>

</body>
</html>


