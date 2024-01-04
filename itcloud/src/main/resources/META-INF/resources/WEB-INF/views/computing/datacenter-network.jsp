<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">DataCenter</h1>
                    컴퓨팅 > <a href="/computing/datacenters" style="text-decoration-line: none">데이터 센터</a> > ${network[0].datacenterName}<br><br>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/datacenter-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                <a href="/computing/datacenter-network?id=${id}">논리 네트워크</a> |
                                <a href="/computing/datacenter-cluster?id=${id}" style="text-decoration-line: none">클러스터</a> |
                                <a href="/computing/datacenter-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/datacenter-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                <table width="700px">
                    <tr>
                        <td>이름</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty network}">
                        <tr>
                            <td>네트워크가 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="network" items="${network}" varStatus="status">
                        <tr>
                            <td><a href="/network/network?id=${network.id}" style="text-decoration-line: none">${network.name}</a></td>
                            <td>${network.description}</td>
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
