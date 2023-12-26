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

<h2 align="center">호스트</h2>
<div class="cluster-container">
    <table align="center">
        <tr>
            <td></td>
            <td></td>
            <td>이름</td>
            <td>코멘트</td>
            <td>호스트이름/IP</td>
            <td>클러스터</td>
            <td>데이터 센터</td>
            <td>상태</td>
            <td>가상머신</td>
            <td>SPM</td>
        </tr>

        <c:if test="${empty hostVOList}">
            <tr>
                <td colspan="10">host 없음</td>
            </tr>
        </c:if>
        <c:forEach var="hostVOList" items="${hostVOList}" varStatus="status">
            <tr>
                <td>${hostVOList.status}</td>
                <td></td>
                <td> <a href="/computing/host?id=${hostVOList.id}">${hostVOList.name}</a> </td>
                <td>${hostVOList.comment}</td>
                <td>${hostVOList.address}</td>
                <td><a href="/computing/cluster?id=${hostVOList.clusterId}">${hostVOList.clusterName}</a></td>
                <td><a href="/computing/datacenter-storage?id=${hostVOList.datacenterId}">${hostVOList.datacenterName}</a></td>
                <td>${hostVOList.status}</td>
                <td>${hostVOList.vmCnt}</td>
                <td>${hostVOList.spm}</td>
            </tr>
        </c:forEach>
    </table>




</div>

</body>
</html>