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
        <tr>
            <td colspan="9">
                이름:&nbsp;${vm.name} <br>
                설명:&nbsp;${vm.description} <br>
                상태:&nbsp;${vm.status} <br>
                업타임:&nbsp;${vm.startTime} <br>
<%--                탬플릿:&nbsp;${vm.} <br>--%>
                운영 시스템:&nbsp;${vm.os} <br>
                칩셋/펌웨어 유형:&nbsp;${vm.chipsetFirmType} <br><br>

                우선순위: ${vm.priority} <br>
                최적화 옵션:&nbsp;${vm.optimizeOption} <br>

                설정된 메모리: ${vm.memory / (1024*1024)}MB <br>
                할당할 실제 메모리: ${vm.realMemory/ (1024*1024)}MB <br>
<%--                게스트 OS의 여유/캐시+버퍼된 메모리: ${vm.freeMemory/ (1024*1024)}MB <br><br>--%>
                CPU 코어 수: ${vm.cpuCore} <br>
                게스트 CPU 수: ${vm.guestCpuCnt} <br>

                게스트 CPU: ${vm.guestCpu} <br>
                고가용성:&nbsp;${vm.ha} <br>
                모니터 수:&nbsp;${vm.monitor} <br>
                USB:&nbsp;${vm.usb} <br><br>

<%--                작성자:&nbsp;${vm.} <br>--%>
<%--                소스:&nbsp;${vm.} <br>--%>
<%--                실행 호스트:&nbsp;${vm.} <br>--%>
<%--                사용자 정의 속성:&nbsp;${vm.} <br>--%>
<%--                클러스터 호환 버전: ${vm.} <br>--%>
                가상 머신의 ID:&nbsp;${vm.id} <br>
                FQDN:&nbsp;${vm.fqdn} <br>
                하드웨어 클럭의 시간 오프셋:&nbsp;${vm.hwTimeOffset} <br>

            </td>
        </tr>
    </table>


</div>

</body>
</html>


