<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <title>IT Cloud</title>
    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
    <link href="/css/styles.css" rel="stylesheet" />

    <style>
        table{
                border: #535c55 1px solid;
                border-collapse: collapse;
        }
        tr,td{
            border: #535c55 1px solid;
            padding: 10px;
        }

    </style>
</head>

<body class="sb-nav-fixed">

    <!-- 회사 로고와 회사명 -->
    <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
        <div class="sb-nav-link-icon">&nbsp;&nbsp;<a class="navbar-brand ps-3" href="/dashboard"><img src="/svg/logo.png" alt="회사로고" width="40" height="auto" /> </div>IT Cloud</a>
    </nav>

    <div id="layoutSidenav">
        <div id="layoutSidenav_nav">
            <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                <!-- 왼쪽 메뉴 시작 -->
                <div class="sb-sidenav-menu"> <br>

                    <!-- ovirt 아이콘 표시 -->
                    <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
                        <ul class="navbar-nav" style="margin-bottom: 1px">
                            <li class="nav-item">
                                <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
                                    <img src="/svg/dc.png" alt="컴퓨팅" width="30" height="auto" />
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link collapsed" href="#"  data-bs-toggle="collapse" data-bs-target="#collapseLayouts2" aria-expanded="false" aria-controls="collapseLayouts2">
                                    <img src="/svg/tm.png" alt="템플릿" width="30" height="auto" />
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts3" aria-expanded="false" aria-controls="collapseLayouts3">
                                    <img src="/svg/n.png" alt="네트워크" width="30" height="auto" />
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts4" aria-expanded="false" aria-controls="collapseLayouts5">
                                    <img src="/svg/s.png" alt="스토리지" width="30" height="auto" />
                                </a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts5" aria-expanded="false" aria-controls="collapseLayouts5">
                                    <img src="/svg/set.png" alt="설정" width="30" height="auto" />
                                </a>
                            </li>
                        </ul>
                    </nav>
                    <!-- ovirt 아이콘 표시 끝 -->

                <!-- 버튼 클릭시 나오는 리스트 출력 -->
                <div>

                    <!-- 컴퓨팅 -->
                    <div class="collapse" id="collapseLayouts"  data-bs-parent="#sidenavAccordion">
                        <c:choose>
                            <c:when test="${not empty m.datacenter}">

                                <ul id="desc">
                                    <c:forEach var="dc" items="${m.datacenter}">
                                        <li id="desc">
                                            <a href="/computing/datacenter-storage?id=${dc.id}" style="text-decoration-line: none" >
                                                <img src="/svg/dcc.png" alt="vm" width="20" height="auto" />
                                            </a>
                                            <a data-bs-toggle="collapse" href="#collapseDatacenter-${dc.id}" style="text-decoration-line: none; color: grey;">
                                                ${dc.name}
                                            </a>

                                            <div class="collapse" id="collapseDatacenter-${dc.id}">
                                                <ul id="desc">
                                                    <c:forEach var="c" items="${m.cluster}">

                                                        <c:if test="${dc.id eq c.datacenterId}">
                                                            <li>
                                                                <a href="/computing/cluster?id=${c.id}" style="text-decoration-line: none">
                                                                    <img src="/svg/dc.png" alt="cluster" width="20" height="auto" />
                                                                </a>
                                                                <a data-bs-toggle="collapse" href="#collapseCluster-${c.id}" style="text-decoration-line: none; color: grey;">
                                                                     ${c.name}
                                                                </a>

                                                                <div class="collapse" id="collapseCluster-${c.id}">
                                                                    <ul id="desc">
                                                                        <c:forEach var="h" items="${m.host}">

                                                                            <c:if test="${c.id eq h.clusterId}">
                                                                                <li>
                                                                                    <a href="/computing/host?id=${h.id}" style="text-decoration-line: none">
                                                                                        <img src="/svg/h.png" alt="vm" width="20" height="auto" />
                                                                                    </a>
                                                                                    <a data-bs-toggle="collapse" href="#collapseHost-${h.id}" style="text-decoration-line: none; color: grey;">
                                                                                        ${h.name}
                                                                                    </a>

                                                                                    <div class="collapse" id="collapseHost-${h.id}">
                                                                                        <ul id="desc">
                                                                                            <c:forEach var="vm" items="${m.vm}">

                                                                                                <c:if test="${h.id eq vm.hostId}">
                                                                                                    <c:if test="${vm.status eq 'up'}">
                                                                                                        <li>
                                                                                                            <a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none; color: grey;">
                                                                                                                <img src="/svg/vm.png" alt="vm" width="20" height="auto" />&nbsp;${vm.name}
                                                                                                            </a>
                                                                                                        </li>
                                                                                                    </c:if>

                                                                                                    <c:if test="${vm.status eq 'down'}">
                                                                                                        <li>
                                                                                                            <a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none; color: grey;">
                                                                                                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${vm.name}
                                                                                                            </a>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:if>
                                                                                            </c:forEach>
                                                                                        </ul>
                                                                                    </div>

                                                                                </li>
                                                                            </c:if>
                                                                        </c:forEach>

                                                                    </ul>
                                                                </div>
                                                            </li>
                                                        </c:if>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>

                            <c:otherwise>
                                비었음
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <!-- 첫번째 끝 -->


                    <!-- 컴퓨팅 (템플릿 포함) -->
                    <div class="collapse" id="collapseLayouts2" data-bs-parent="#sidenavAccordion">
                        <c:choose>
                            <c:when test="${not empty m.datacenter}">

                                <ul id="desc">
                                    <c:forEach var="dc" items="${m.datacenter}">
                                        <li id="desc">
                                            <a href="/computing/datacenter-storage?id=${dc.id}" style="text-decoration-line: none" >
                                                <img src="/svg/dcc.png" alt="vm" width="20" height="auto" />
                                            </a>
                                            <a data-bs-toggle="collapse" href="#collapseDatacenter-${dc.id}" style="text-decoration-line: none; color: grey;">
                                                ${dc.name}
                                            </a>

                                            <div class="collapse" id="collapseDatacenter-${dc.id}">
                                                <ul id="desc">
                                                    <c:forEach var="c" items="${m.cluster}">

                                                        <c:if test="${dc.id eq c.datacenterId}">
                                                            <li>
                                                                <a href="/computing/cluster?id=${c.id}" style="text-decoration-line: none">
                                                                    <img src="/svg/dc.png" alt="cluster" width="20" height="auto" />
                                                                </a>
                                                                <a data-bs-toggle="collapse" href="#collapseCluster-${c.id}" style="text-decoration-line: none; color: grey;">
                                                                     ${c.name}
                                                                </a>

                                                                <div class="collapse" id="collapseCluster-${c.id}">
                                                                    <ul id="desc">
                                                                        <c:forEach var="h" items="${m.host}">

                                                                            <c:if test="${c.id eq h.clusterId}">
                                                                                <li>
                                                                                    <a href="/computing/host?id=${h.id}" style="text-decoration-line: none">
                                                                                        <img src="/svg/h.png" alt="vm" width="20" height="auto" />
                                                                                    </a>
                                                                                    <a data-bs-toggle="collapse" href="#collapseHost-${h.id}" style="text-decoration-line: none; color: grey;">
                                                                                        ${h.name}
                                                                                    </a>

                                                                                    <div class="collapse" id="collapseHost-${h.id}">
                                                                                        <ul id="desc">
                                                                                            <c:forEach var="vm" items="${m.vm}">

                                                                                                <c:if test="${h.id eq vm.hostId}">
                                                                                                    <c:if test="${vm.status eq 'up'}">
                                                                                                        <li>
                                                                                                            <a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none; color: grey;">
                                                                                                                <img src="/svg/vm.png" alt="vm" width="20" height="auto" />&nbsp;${vm.name}
                                                                                                            </a>
                                                                                                        </li>
                                                                                                    </c:if>

                                                                                                    <c:if test="${vm.status eq 'down'}">
                                                                                                        <li>
                                                                                                            <a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none; color: grey;">
                                                                                                               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${vm.name}
                                                                                                            </a>
                                                                                                        </li>
                                                                                                    </c:if>
                                                                                                </c:if>
                                                                                            </c:forEach>
                                                                                        </ul>
                                                                                    </div>

                                                                                </li>
                                                                            </c:if>
                                                                        </c:forEach>

                                                                    </ul>
                                                                </div>
                                                            </li>
                                                        </c:if>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>

                            <c:otherwise>
                                비었음
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- 네트워크 -->
                    <div class="collapse" id="collapseLayouts3" data-bs-parent="#sidenavAccordion">

                    	<ul id="desc">
                    	    <!-- vnic -->
                            <a href="/network/vnicProfiles" style="text-decoration-line: none" >
                                <img src="/svg/vnic.png" alt="vnic" width="20" height="auto" />
                            </a>
                            <a data-bs-toggle="collapse" href="#collapseVnic" style="text-decoration-line: none; color: grey;">
                                vNic 프로파일
                            </a>
                    	    <c:forEach var="v" items="${m.vnic}" varStatus="status">
                    	        <li id="desc">
                                    <div class="collapse" id="collapseVnic">
                                        <ul id="desc">
                                            <li id="desc">
                                                <c:if test="${empty m.vnic}">
                                                    비었음
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                                <c:if test="${not empty m.vnic}">
                                                    <a href="/network/vnicProfile-vm?id=${v.id}" style="text-decoration-line: none; color: grey;">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;- ${v.name}
                                                    </a>
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                </c:forEach>

                            <!-- network -->
                            <a href="/network/networks" style="text-decoration-line: none" >
                                <img src="/svg/n.png" alt="network" width="20" height="auto" />
                            </a>
                            <a data-bs-toggle="collapse" href="#collapseNetwork" style="text-decoration-line: none; color: grey;">
                                Network
                            </a>
                            <c:forEach var="n" items="${m.network}" varStatus="status">
                                <li id="desc">
                                    <div class="collapse" id="collapseNetwork">
                                        <ul id="desc">
                                            <li id="desc">
                                                <c:if test="${empty m.network}">
                                                    비었음
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                                <c:if test="${not empty m.network}">
                                                    <a href="/network/network?id=${n.id}" style="text-decoration-line: none; color: grey;">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;- ${n.name}
                                                    </a>
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>

                    <!-- 스토리지 -->
                    <div class="collapse" id="collapseLayouts4" data-bs-parent="#sidenavAccordion">

                        <ul id="desc">
                            <!-- domain -->
                            <a href="/storage/storageDomains" style="text-decoration-line: none" >
                                <img src="/svg/s.png" alt="domain" width="20" height="auto" />
                            </a>
                            <a data-bs-toggle="collapse" href="#collapseDomain" style="text-decoration-line: none; color: grey;">
                                도메인
                            </a>
                            <c:forEach var="d" items="${m.domain}" varStatus="status">
                                <li id="desc">
                                    <div class="collapse" id="collapseDomain">
                                        <ul id="desc">
                                            <li id="desc">
                                                <c:if test="${empty m.domain}">
                                                    비었음
                                                    <br>
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                                <c:if test="${not empty m.domain}">
                                                    <a href="/storage/storageDomain?id=${d.id}" style="text-decoration-line: none; color: grey;">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;- ${d.name}
                                                    </a>
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                </c:forEach>

                            <!-- 볼륨 -->
                            <a href="/storage/volumes" style="text-decoration-line: none" >
                                <img src="/svg/s.png" alt="volume" width="20" height="auto" />
                            </a>
                            <a data-bs-toggle="collapse" href="#collapseVolume" style="text-decoration-line: none; color: grey;">
                                볼륨
                            </a>
                            <br>
                            <c:forEach var="v" items="${m.volume}" varStatus="status">
                                <li id="desc">
                                    <div class="collapse" id="collapseVolume">
                                        <ul id="desc">
                                            <li id="desc">
                                                <c:if test="${empty m.volume}">
                                                    비었음
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                                <c:if test="${not empty m.volume}">
                                                    <a href="/storage/volume?id=${v.id}" style="text-decoration-line: none; color: grey;">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;- ${v.name}
                                                    </a>
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                            </c:forEach>

                            <!-- 디스크 -->
                            <a href="/storage/disks" style="text-decoration-line: none" >
                                <img src="/svg/s.png" alt="disk" width="20" height="auto" />
                            </a>
                            <a data-bs-toggle="collapse" href="#collapseDisk" style="text-decoration-line: none; color: grey;">
                                디스크
                            </a>
                            <c:forEach var="d" items="${m.disk}" varStatus="status">
                                <li id="desc">
                                    <div class="collapse" id="collapseDisk">
                                        <ul id="desc">
                                            <li id="desc">
                                                <c:if test="${empty m.disk}">
                                                    비었음
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                                                <c:if test="${not empty m.disk}">
                                                    <a href="/storage/disk?id=${d.id}" style="text-decoration-line: none; color: grey;">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;- ${d.name}
                                                    </a>
                                                    </li>
                                                    </ul>
                                                    </li>
                                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>

        <div class="collapse" id="collapseLayouts5" data-bs-parent="#sidenavAccordion">
            <a class="nav-link" href="/setting/user">사용자</a>
        </div>

        </div>


        </nav>
    </div>