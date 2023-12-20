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
            /*text-align: center;*/
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

<h2 align="center">클러스터 - 가상머신</h2>
<div class="cluster-container">
    <table align="center">
        <tr>
            <td><a href="/computing/cluster?id=${id}">일반</a></td>
            <td><a href="/computing/cluster-network?id=${id}">논리 네트워크</a></td>
            <td><a href="/computing/cluster-host?id=${id}">호스트</a></td>
            <td><a href="/computing/cluster-vm?id=${id}">가상머신</a></td>
            <td><a href="/computing/cluster-aff?id=${id}">선호도 그룹</a></td>
            <td>선호도 레이블</td>
            <td><a href="/computing/cluster-cpu?id=${id}">CPU 프로파일</a></td>
            <td>권한</td>
        </tr>
        <tr><td colspan="9"></td></tr>

        <c:if test="${empty vms}">
            <tr>
                <td>가상머신이 없음</td>
            </tr>
        </c:if>
        <c:forEach var="vms" items="${vms.vmVOList}" varStatus="status">
        <tr>
            <td colspan="9">
                이름: &nbsp;${vms.vmName} <br>
                상태: &nbsp;${vms.status} <br>
                start타임: &nbsp;${vms.startTime} <br>
                ip 주소(v4): &nbsp;${vms.ipv4} <br>
                ip 주소(v6): &nbsp;${vms.ipv6} <br>
            </td>
        </tr>
        </c:forEach>
    </table>


</div>

</body>
</html>


