<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a> - 가상머신 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/cluster?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/cluster-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                <a href="/computing/cluster-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/computing/cluster-vm?id=${id}">가상머신</a> |
                                <a href="/computing/cluster-aff?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/cluster-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/cluster-cpu?id=${id}" style="text-decoration-line: none">CPU 프로파일</a> |
                                <a href="#" style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>이름</td>
                            <td>상태</td>
                            <td>start타임</td>
                            <td>ip 주소(v4)</td>
                            <td>ip 주소(v6)</td>
                        </tr>

                        <c:if test="${empty vms}">
                                <tr>
                                    <td>가상머신이 없음</td>
                                </tr>
                        </c:if>
                        <c:forEach var="vms" items="${vms}" varStatus="status">
                            <tr>
                                <td>${vms.name}</td>
                                <td>${vms.status}</td>
                                <td>${vms.startTime}</td>
                                <td>${vms.ipv4}</td>
                                <td>${vms.ipv6}</td>
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
