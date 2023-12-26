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
    <li><a class="side" href="/computing/clusters">Cluster</a></li>
    <li><a class="side" href="/computing/hosts">Host</a></li>
    <li><a class="side" href="/computing/vms">Vm</a></li>
    <li><a class="side" href="/storage">Storage</a></li>
</ul>

<h2 align="center">클러스터 - 선호도 그룹</h2>
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

        <c:if test="${empty aff}">
            <tr>
                <td>선호도 그룹이 없음</td>
            </tr>
        </c:if>
        <c:forEach var="aff" items="${aff}" varStatus="status">
        <tr>
            <td colspan="9">
                상태: &nbsp;${aff.status} <br>
                이름: &nbsp;${aff.name} <br>
                설명:  ${aff.description} <br>
                우선순위: ${aff.priority} <br>
                가상머신 측 극성: ${aff.vmPositive} <br>   <%--양극, 극성, 비활성화--%>
                가상머신 강제적용: ${aff.vmEnforcing ? "하드" : "소프트"} <br>
                호스트 측 극성: ${aff.hostPositive} <br>
                호스트 강제적용: ${aff.hostEnforcing ? "하드" : "소프트"} <br>
                가상머신 멤버: ${aff.vmList}<br>
                가상머신 레이블: ${aff.vmLabels}<br>
                호스트 멤버: ${aff.hostList}<br>
                호스트 레이블: ${aff.hostLabels}<br>
            </td>
        </tr>
        </c:forEach>
    </table>


</div>

</body>
</html>


