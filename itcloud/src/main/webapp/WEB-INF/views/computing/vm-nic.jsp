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
        <li><a class="side" href="/computing/clusters">Clsuter</a></li>
        <li><a class="side" href="/computing/hosts">Host</a></li>
        <li><a class="side" href="/computing/vms">vm</a></li>
        <li><a class="side" href="/storage">Storage</a></li>
    </ul>
</div>

<h2 align="center">호스트</h2>
<div class="cluster-container">
    <table align="center">
        <tr>
            <td><a href="/computing/vm?id=${id}">일반</a></td>
            <td><a href="/computing/vm-nic?id=${id}">네트워크 인터페이스</a></td>
            <td><a href="/computing/vm-disk?id=${id}">디스크</a></td>
            <td><a href="/computing/vm-snapshot?id=${id}">스냅샷</a></td>
            <td><a href="/computing/vm-application?id=${id}">애플리케이션</a></td>
            <td><a href="/computing/vm-affgroup?id=${id}">선호도 그룹</a></td>
            <td><a href="/computing/vm-afflabel?id=${id}">선호도 레이블</a></td>
            <td><a href="/computing/vm-guest?id=${id}">게스트 정보</a></td>
            <td>권한</td>
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
                    네트워크 이름: &nbsp;${nic.name} <br>
                    IPv4:&nbsp;${nic.ipv4} <br>
                    IPv6:&nbsp;${nic.ipv6} <br>
                    MAC:&nbsp;${nic.macAddress} <br>
                </td>
            </tr>
            <tr>
                <td>
                    <h3>일반</h3>
                    연결됨: ${nic.} <br>
                    네트워크 이름: ${nic.} <br>
                    프로파일 이름: ${nic.} <br>
                    Qos 이름: ${nic.} <br>
                    링크 상태: ${nic.} <br>
                    유형: ${nic.} <br>
                    속도(Mbps): ${nic.} <br>
                    포트 미러링: ${nic.} <br>
                    게스트 인터페이스 이름: ${nic.} <br>
                </td>
                <td>
                    <h3>통계</h3>
                    Rx 속도(Mbps): ${nic.} <br>
                    Tx 속도(Mbps): ${nic.} <br>
                    총 Rx: ${nic.} <br>
                    총 Tx: ${nic.} <br>
                    중단 (Pkts): ${nic.} <br>
                </td>
                <td>
                    <h3>네트워크 필터 매개변수</h3>
                </td>
            </tr>
        </c:forEach>
    </table>


</div>

</body>
</html>


