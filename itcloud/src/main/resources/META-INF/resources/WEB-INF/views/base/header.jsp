<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
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


        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <div class="sb-nav-link-icon">&nbsp;&nbsp;<img src="/svg/logo.png" alt="회사로고" width="40" height="auto" /> </div>
            <a class="navbar-brand ps-3" href="/dashboard">IT Cloud</a>
        </nav>

        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <!-- 왼쪽 메뉴 -->
                    <div class="sb-sidenav-menu">

                    <div class="nav" >
                        <a class="nav-link" href="/dashboard">
                            <div class="sb-nav-link-icon"><i class="fa-solid fa-window-restore"></i></div>
                            대시보드
                        </a>
                        <a class="nav-link" href="/setting">
                            <div class="sb-nav-link-icon"><i class="fa-solid fa-gear"></i></div>
                            설정
                        </a>

                    <div class="sb-sidenav-menu-heading">itCloud</div>

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
                              <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts5" aria-expanded="false" aria-controls="collapseLayouts5">
                                <img src="/svg/s.png" alt="스토리지" width="30" height="auto" />
                              </a>
                            </li>

                          </ul>

                       </nav>


                       <div>
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
                                                                                                    <li>
                                                                                                        <a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none; color: grey;">
                                                                                                            <img src="/svg/vm.png" alt="vm" width="20" height="auto" />&nbsp;${vm.name}
                                                                                                        </a>
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

                    <!-- 템플릿 포함 데이터센터 그거 -->
                         <div class="collapse" id="collapseLayouts2" data-bs-parent="#sidenavAccordion">

                         <c:if test="${empty m.datacenter}">
                             비었음
                         </c:if>
                         <c:forEach var="dc" items="${m.datacenter}" varStatus="status">
                             <a class="nav-link" href="/computing/datacenter-storage?id=${dc.id}">
                                 <img src="/svg/dcc.png" alt="vm" width="25" height="auto" />&nbsp; ${dc.name}
                             </a>
                         </c:forEach>
                         <ul id="sub">
                             <li>
                                 <c:if test="${empty m.cluster}">
                                     비었음
                                 </c:if>
                                 <c:forEach var="c" items="${m.cluster}" varStatus="status">
                                     <a class="nav-link" href="/computing/cluster?id=${c.id}">
                                         <img src="/svg/dc.png" alt="cluster" width="25" height="auto" />&nbsp;${c.name}
                                     </a>
                                 </c:forEach>

                                 <ul id="sub">
                                     <li>
                                         <c:if test="${empty m.host}">
                                             비었음
                                         </c:if>
                                         <c:forEach var="h" items="${m.host}" varStatus="status">
                                             <a class="nav-link" href="/computing/host?id=${h.id}">
                                                 <img src="/svg/h.png" alt="host" width="25" height="auto" />&nbsp;${h.name}
                                             </a>
                                         </c:forEach>
                                         <ul id="sub">
                                             <li>
                                                 <c:if test="${empty m.vm}">
                                                     비었음
                                                 </c:if>
                                                 <c:forEach var="vm" items="${m.vm}" varStatus="status">
                                                     <a class="nav-link" href="/computing/vm?id=${vm.id}">
                                                         <img src="/svg/vm.png" alt="host" width="25" height="auto" />&nbsp;${vm.name}
                                                     </a>
                                                 </c:forEach>
                                             </li>
                                         </ul>
                                     </li>
                                 </ul>
                             </li>
                         </ul>


                         <!--  과거버전
                            <a class="nav-link" href="/computing/datacenters">
                                 <img src="/svg/dcc.png" alt="vm" width="25" height="auto" />&nbsp;데이터센터
                            </a>
                            <ul id="sub">
                                 <li>
                                     <a class="nav-link" href="/computing/clusters">
                                         <img src="/svg/dc.png" alt="vm" width="25" height="auto" />&nbsp;클러스터
                                     </a>
                                     <ul id="sub">
                                         <li>
                                             <a class="nav-link" href="/computing/hosts">
                                                 <img src="/svg/h.png" alt="vm" width="25" height="auto" />&nbsp;호스트
                                             </a>
                                             <ul id="sub">
                                                 <li>
                                                     <a class="nav-link" href="/computing/vms">
                                                         <img src="/svg/vm.png" alt="vm" width="25" height="auto" />&nbsp;가상머신
                                                     </a>
                                                     <a class="nav-link" href="/computing/templates">
                                                         <img src="/svg/vm.png" alt="tm" width="25" height="auto" />&nbsp;템플릿
                                                     </a>
                                                 </li>
                                             </ul>
                                         </li>
                                     </ul>
                                 </li>
                            </ul>
                          -->

                         </div>

                         <div class="collapse" id="collapseLayouts3" data-bs-parent="#sidenavAccordion">
                         <ul>
                             <li>
                                  <a class="nav-link" href="/network/vnicProfiles">vNIC 프로파일</a>
                                  <c:if test="${empty m.vnic}">
                                      비었음
                                  </c:if>
                                  <c:forEach var="v" items="${m.vnic}" varStatus="status">
                                      <a class="nav-link" href="/network/vnicProfile-vm?id=${v.id}">&nbsp;${v.name}</a>
                                  </c:forEach>
                             </li>
                              <li>
                                   <a class="nav-link" href="/network/networks">네트워크</a>
                                   <c:if test="${empty m.network}">
                                       비었음
                                   </c:if>
                                   <c:forEach var="n" items="${m.network}" varStatus="status">
                                       <a class="nav-link" href="/network/network?id=${n.id}">&nbsp;${n.name}</a>
                                   </c:forEach>
                              </li>
                         </ul>


                         </div>

                         <div class="collapse" id="collapseLayouts5" data-bs-parent="#sidenavAccordion">
                            <a class="nav-link" href="/storage/storageDomains">도메인</a>
                            <a class="nav-link" href="/storage/volumes">볼륨</a>
                            <a class="nav-link" href="/storage/disks">디스크</a>
                         </div>

                       </div>

                    </div>
                    </div>
                </nav>
            </div>