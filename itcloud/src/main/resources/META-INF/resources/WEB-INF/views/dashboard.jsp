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

        .dashboard-container {
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

    <div class="dashboard-container">
        <h2 align="center">클러스터</h2>
        <table align="center">
            <tr>
                <td>[${dashboard.datacenterCnt}]&nbsp;데이터센터</td>
                <td>[${dashboard.clusterCnt}]&nbsp;클러스터</td>
                <td>[${dashboard.hostCnt}]&nbsp;호스트</td>
                <td>[${dashboard.storageDomainCnt}]&nbsp;데이터 스토리지 도메인</td>
                <td>[${dashboard.vmCnt}]&nbsp;가상머신</td>
                <td>이벤트</td>
            </tr>
            <tr>
                <td>UP &nbsp;${dashboard.datacenterActive} / DOWN &nbsp; ${dashboard.datacenterInactive}</td>
                <td>${dashboard.clusterCnt}</td>
                <td>UP &nbsp;${dashboard.hostActive} / DOWN &nbsp; ${dashboard.hostInactive}</td>
                <td>UP &nbsp;${dashboard.storageDomainActive} / DOWN &nbsp; ${dashboard.storageDomainInactive}</td>
                <td>UP &nbsp;${dashboard.vmActive} / DOWN &nbsp; ${dashboard.vmInactive}</td>
                <td>이벤트준비중</td>
            </tr>
            <tr> <td style="padding: 30px" colspan="6"></td> </tr>

            <tr> <td colspan="6">전체 사용량</td> </tr>
            <tr>
                <td colspan="2">CPU</td>
                <td colspan="2">메모리</td>
                <td colspan="2">스토리지</td>
            </tr>
            <tr>
                <td colspan="2">
                    총 cpu : ${dashboard.cpuTotal}<br>
                    할당 cpu : ${dashboard.cpuAssigned}
                </td>
                <td colspan="2">
                    총 메모리: ${dashboard.memoryTotal/(1024*1024*1024)} GB<br>
                    사용된 메모리 : ${dashboard.memoryUsed/(1024*1024*1024)} GB<br>
                    사용가능 메모리 : ${dashboard.memoryFree/(1024*1024*1024)} GB
                </td>
                <td colspan="2">
                    총 스토리지: ${dashboard.storageTotal/(1024*1024*1024)} GB<br>
                    사용된 스토리지 : ${dashboard.storageUsed/(1024*1024*1024)} GB<br>
                    사용가능 스토리지 : ${dashboard.storageFree/(1024*1024*1024)} GB
                </td>
            </tr>

        </table>




    </div>

</body>
</html>


