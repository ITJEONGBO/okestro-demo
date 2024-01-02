<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Host</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/hosts" style="text-decoration-line: none">호스트</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>이름</td>
                        <td>코멘트</td>
                        <td>호스트이름/IP</td>
                        <td>클러스터</td>
                        <td>데이터 센터</td>
                        <td>상태</td>
                        <td>가상머신</td>
                        <td>CPU</td>
                        <td>메모리</td>
                        <td>네트워크</td>
                    </tr>

                    <c:if test="${empty hosts}">
                        <tr>
                            <td colspan="12" rowspan="2">host 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="hosts" items="${hosts}" varStatus="status">
                        <tr>
                            <td>${hosts.status == "up" ? "🔼" : "🔽"}</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td> <a href="/computing/host?id=${hosts.id}">${hosts.name}</a> </td>
                            <td>${hosts.comment}</td>
                            <td>${hosts.address}</td>
                            <td><a href="/computing/cluster?id=${hosts.clusterId}">${hosts.clusterName}</a></td>
                            <td><a href="/computing/datacenter-storage?id=${hosts.datacenterId}">${hosts.datacenterName}</a></td>
                            <td>${hosts.status}</td>
                            <td>${hosts.vmCnt}</td>
                            <td></td>
                            <td></td>
                            <td></td>
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
