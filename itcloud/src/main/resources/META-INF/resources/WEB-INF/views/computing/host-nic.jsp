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
<div>
    <ul>
        <li><a class="side" href="/computing/datacenters">DataCenter</a></li>
        <li><a class="side" href="/computing/clusters">Cluster</a></li>
        <li><a class="side" href="/computing/hosts">Host</a></li>
        <li><a class="side" href="/computing/vms">Vm</a></li>
        <li><a class="side" href="/storage">Storage</a></li>
    </ul>
</div>

<h2 align="center">호스트</h2>
<div class="cluster-container">
    <table align="center">
        <tr>
            <td><a href="/computing/host?id=${id}">일반</a></td>
            <td><a href="/computing/host-vm?id=${id}">가상머신</a></td>
            <td><a href="/computing/host-nic?id=${id}">네트워크 인터페이스</a></td>
            <td><a href="/computing/host-device?id=${id}">호스트 장치</a></td>
            <td>권한</td>
            <td><a href="/computing/host-aff?id=${id}">선호도 레이블</a></td>
            <td>이벤트</td>
        </tr>
        <tr><td colspan="9"></td></tr>

        <c:if test="${empty nic}">
            <tr>
                <td>nic 없음</td>
            </tr>
        </c:if>
        <c:forEach var="nic" items="${nic.hostNicVOList}" varStatus="status">
            <tr>
                <td colspan="9">
                    id: &nbsp;${nic.id} <br>
                    이름: &nbsp;${nic.name} <br>
                    MAC: &nbsp;${nic.macAddress} <br>
                    속도: &nbsp;${nic.speed} <br>
                </td>
            </tr>
        </c:forEach>
    </table>


</div>

</body>
</html>


