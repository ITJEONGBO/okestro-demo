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

        li a.active {
            background-color: #f1f1f1;
            color: #686565;
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

        .widget {
            background-color: #fff;
            padding: 15px;
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
    </style>

</head>
<body>

    <header>
        <h1>&nbsp;&nbsp; It Cloud</h1>
        <p align="right">${dashboardStatus.id}</p>
    </header>

    <%--<li><a class="active" href="/dashboard">대시보드</a></li>
            <li><a href="/computing/vms">컴퓨팅</a></li>
            <li><a class="side" href="/computing/vms">가상머신</a></li>
            <li><a class="side" href="/computing/templates">템플릿</a></li>
            <li><a class="side" href="/computing/hosts">호스트</a></li>
            <li><a class="side" href="/computing/clusters">클러스터</a></li>
            <li><a href="/networks">네트워크</a></li>
            <li><a href="/storage/domain">스토리지</a></li>
            <li><a class="side" href="/stoarge/domain">도메인</a></li>
            <li><a class="side" href="/stoarge/disk">디스크</a></li>
            <li><a href="/admin/user">관리</a></li>
            <li><a class="side" href="/admin/user">사용자</a></li>
            <li><a class="side" href="/admin/instanceType">인스턴스 유형</a></li>
            <li><a class="side" href="/admin/macAddress">맥 주소 풀</a></li>
            <li><a class="side" href="/admin/setting">설정</a></li>--%>

    <ul>
        <li><a class="side" href="#datacenter">DataCenter</a>
            <ul>
                <li><a class="side" href="#cluster">Clsuter</a>
                    <ul>
                        <li><a class="side" href="#host">Host</a>
                            <ul>
                                <li><a class="side" href="#vm">vm</a></li>
                            </ul>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
    </ul>

    <div style="margin-left:25%; padding:1px 16px; height:1000px; ">

        <div class="dashboard-container">
            <div class="widget">
                <h2>${dashboard.datacenterCnt} &nbsp;&nbsp;데이터 센터</h2>
                <p align="center">${dashboard.datacenterCnt}</p>
            </div>

            <div class="widget">
                <h2>${dashboard.clusterCnt} &nbsp;&nbsp;클러스터</h2>
                <p align="center">${dashboard.clusterCnt}</p>
            </div>

            <div class="widget">
                <h2>${dashboard.hostCnt}&nbsp;&nbsp;호스트</h2>
                <p align="center">${dashboard.hostCnt}</p>
            </div>

            <div class="widget">
                <h2>${dashboard.storageDomainCnt}&nbsp;&nbsp;데이터 스토리지 도메인</h2>
                <p>${dashboard.storageDomainCnt}</p>
            </div>

            <div class="widget">
                <h2>${dashboard.vmCnt}&nbsp;&nbsp;가상머신</h2>
                <p>${dashboard.vmCnt}</p>
            </div>



        </div>

        <div class="dashboard-container">
            <br>
            <div class="widget">
                <h3 align="center"> 전체 사용량</h3>
                <br>
            </div>

        </div>

        <div class="dashboard-container">
            <div class="widget">
                <h2>호스트 </h2>
                <p>  ${dashboardStatus.hostsUp}개, (${dashboardStatus.hostName})</p>

            </div>

        </div>


    </div>

</body>
</html>


