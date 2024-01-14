<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    컴퓨팅 > <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a> > ${vms[0].clusterName} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/cluster?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/cluster-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                <a href="/computing/cluster-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/computing/cluster-vm?id=${id}">가상머신</a> |
                                <a href="/computing/cluster-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/cluster-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/cluster-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/cluster-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td>이름</td>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td>상태</td>
                            <td>업타임</td>
                            <td>CPU</td>
                            <td>메모리</td>
                            <td>네트워크</td>
                            <td>IP 주소</td>
                        </tr>

                        <c:if test="${empty vms}">
                            <tr>
                                <td colspan="9" style="text-align: center">표시할 항목이 없습니다</td>
                            </tr>
                        </c:if>
                        <c:forEach var="vms" items="${vms}" varStatus="status">
                            <tr>
                                <td>${vms.status == "up" ? "▲" : "▽"}</td>
                                <td><a href="/computing/vm?id=${vms.id}" style="text-decoration-line: none">${vms.name}</a></td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>${vms.status == "up" ? " 실행 중" : vms.status}</td>
                                <td>
                                    ${vms.upTime/60}
                                </td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td> ${vms.ipv4}<br> ${vms.ipv6}</td>

                            </tr>
                        </c:forEach>
                    </table>
                    <br><br><br><br>

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
