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

<h2 align="center">클러스터</h2>
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
        <tr>
            <td colspan="9">
                이름: &nbsp;${cluster.name} <br>
                설명: &nbsp;${cluster.description} <br>
                데이터센터: &nbsp;${cluster.dataCenter} <br>
                호환버전: &nbsp;${cluster.version} <br>
                클러스터 ID: &nbsp;${cluster.id} <br>
                클러스터 CPU 유형: &nbsp;${cluster.cpuType} <br>
                스레드를 CPU로 사용: &nbsp;${cluster.threadsAsCPU ? "예":"아니요"} <br>
                최대 메모리 오버 커밋: &nbsp;${cluster.memoryOverCommit} <br>
                복구정책: &nbsp;(해야함) <br>
                칩셋/펌웨어 유형: &nbsp;${cluster.chipsetFirmwareType} <br>
                가상머신 수: &nbsp;${cluster.vmCnt} <br>
            </td>
        </tr>
    </table>


</div>

</body>
</html>


