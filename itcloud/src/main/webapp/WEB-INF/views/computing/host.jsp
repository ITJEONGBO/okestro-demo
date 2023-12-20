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
            <td><a href="/computing/host?id=${id}">일반</a></td>
            <td><a href="/computing/host-vm?id=${id}">가상머신</a></td>
            <td><a href="/computing/host-nic?id=${id}">네트워크 인터페이스</a></td>
            <td><a href="/computing/host-device?id=${id}">호스트 장치</a></td>
            <td>권한</td>
            <td><a href="/computing/host-aff?id=${id}">선호도 레이블</a></td>
            <td>이벤트</td>
        </tr>
        <tr><td colspan="9"></td></tr>
        <tr>
            <td colspan="9">
                호스트이름/IP:&nbsp;${host.name} <br>
                SPM 우선순위: ${host.spmPriority} <br>
                활성 가상머신: ${host.vmUpCnt} <br>
                논리 CPU 코어 수: ${host.cpuCoreCnt} <br>
<%--                부팅 시간: ${host.} <br>--%>
<%--                Hosted Engine HA: ${host.} <br>--%>
                iSCSI 개시자 이름: ${host.iscsi} <br>
                Kdump Intergration Status: ${host.kdumpStatus} <br>
                물리적 메모리: ${host.memory / (1024*1024)}MB <br>
                사용된 메모리: ${host.usedMemory/ (1024*1024)}MB <br>
                사용가능 메모리: ${host.freeMemory/ (1024*1024)}MB <br><br>

                Swap 크기: ${host.swapMemory / (1024*1024)}MB <br>
                Swap 사용된 크기: ${host.swapUsedMemory / (1024*1024)}MB <br>
                Swap 사용가능 크기: ${host.swapFreeMemory / (1024*1024)}MB <br>

<%--                공유 메모리: ${host.} <br>--%>
                장치 통과: ${host.devicePassthrough} <br>
                새로운 가상머신의 스케줄링을 위한 최대 여유 메모리: ${host.newVmMemory} <br>
<%--                메모리 페이지 공유: ${host.} <br>--%>
<%--                자동으로 페이지를 크게: ${host.} <br>--%>
                Huge Pages(size:free/total): ${host.hugePagesType}, ${host.hugePagesType2} <br>
                SELinux 모드: ${host.seLinux} <br>
<%--                클러스터 호환 버전: ${host.} <br>--%>
            </td>
        </tr>
        <tr>
            <td colspan="9">
                <h3>하드웨어</h3>
                <hr>
                생산자: ${host.hostHwVO.manufacturer} <br>
                제품군: ${host.hostHwVO.family} <br>
                제품 이름: ${host.hostHwVO.productName} <br>
                버전: ${host.hostHwVO.hwVersion} <br>
                cpu 모델: ${host.hostHwVO.cpuName} <br>
                cpu 유형: ${host.hostHwVO.cpuType} <br>
                uuid: ${host.hostHwVO.uuid} <br>
                일련번호: ${host.hostHwVO.serialNum} <br>
                코어당 cpu 스레드: ${host.hostHwVO.coreThread} <br>
                소켓당 cpu 코어: ${host.hostHwVO.socketCore} <br>
                cpu 소켓: ${host.hostHwVO.cpuSocket} <br>
            </td>
        </tr>
        <tr>
            <td colspan="9">
                <h3>소프트웨어</h3>
                <hr>
                os 버전: ${host.hostSwVO.osVersion} <br>
                os 정보:  <br>
                커널 버전 db: <br>
                kvm 버전 db:  <br>
                LIBVIRT 버전: ${host.hostSwVO.libvirtVersion} <br>
                VDSM 버전 db: ${host.hostSwVO.vdsmVersion} <br>
                SPICE 버전:  <br>
                GlusterFS 버전:  <br>
                CEPH 버전:  <br>
                Open vSwitch 버전:  <br>
                Nmstate 버전:  <br>
            </td>
        </tr>
    </table>


</div>

</body>
</html>


