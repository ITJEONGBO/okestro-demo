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
    <li><a class="side" href="/computing/clusters">Cluster</a></li>
    <li><a class="side" href="/computing/hosts">Host</a></li>
    <li><a class="side" href="/computing/vms">Vm</a></li>
    <li><a class="side" href="/storage">Storage</a></li>
</ul>

<h2 align="center">가상머신</h2>
<div class="cluster-container">
    <table align="center">
        <tr>
            <td>상태</td>
            <td></td>
            <td>이름</td>
<%--            <td>호스트</td>--%>
            <td>IP주소</td>
            <td>FQDN</td>
            <td>클러스터</td>
<%--            <td>데이터 센터</td>--%>
            <td>상태</td>
            <td>업타임</td>
            <td>설명</td>
        </tr>

        <c:if test="${empty vmList}">
            <tr>
                <td colspan="10">host 없음</td>
            </tr>
        </c:if>
        <c:forEach var="vmList" items="${vmList}" varStatus="status">
            <tr>
                <td>${vmList.status}</td>
                <td></td>
                <td><a href="/computing/vm?id=${vmList.id}">${vmList.name}</a> </td>
<%--                <td><a href="/computing/host?id=${vmList.hostId}">${vmList.hostName}</a></td>--%>
                <td>${vmList.ipv4} / ${vmList.ipv6}</td>
                <td>${vmList.fqdn}</td>
                <td><a href="/computing/cluster?id=${vmList.clusterId}">${vmList.clusterName}</a></td>
<%--                <td><a href="/computing/datacenter-storage?id=${vmList.datacenterId}">${vmList.datacenterName}</a></td>--%>
                <td>${vmList.status}</td>
                <td>${vmList.startTime}</td>
                <td>${vmList.description}</td>
            </tr>
        </c:forEach>
    </table>




</div>

</body>
</html>