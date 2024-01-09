<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">VM</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/vms">가상머신</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>이름</td>
                        <td>호스트</td>
                        <td>IP주소</td>
                        <td>FQDN</td>
                        <td>클러스터</td>
                        <td>데이터 센터</td>
                        <td>메모리</td>
                        <td>CPU</td>
                        <td>네트워크</td>
                        <td>상태</td>
                        <td>업타임</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty vms}">
                        <tr>
                            <td colspan="14">host 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="vms" items="${vms}" varStatus="status">
                        <tr>
                            <td>${vms.status == "up" ? "▲" : "▽"}</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td><a href="/computing/vm?id=${vms.id}">${vms.name}</a> </td>
                            <td><a href="/computing/host?id=${vms.hostId}">${vms.hostName}</a></td>
                            <td>${vms.ipv4}<br> ${vms.ipv6}</td>
                            <td>${vms.fqdn}</td>
                            <td><a href="/computing/cluster?id=${vms.clusterId}">${vms.clusterName}</a></td>
                            <td><a href="/computing/datacenter-storage?id=${vms.datacenterId}">${vms.datacenterName}</a></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>${vms.status == "up" ? "실행 중" : vms.status}</td>
                            <td>${vms.upTime}</td>
                            <td>${vms.description}</td>
                        </tr>
                    </c:forEach>
                </table>

                </div>
            </main>
        </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="js/scripts.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
    <script src="assets/demo/chart-area-demo.js"></script>
    <script src="assets/demo/chart-bar-demo.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js" crossorigin="anonymous"></script>
    <script src="js/datatables-simple-demo.js"></script>
    </body>
</html>
