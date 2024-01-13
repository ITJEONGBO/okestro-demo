<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">DataCenter</h1>
                    컴퓨팅 > <a href="/computing/datacenters" style="text-decoration-line: none">데이터 센터</a> > ${event[0].datacenterName} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                 <a href="/computing/datacenter-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                 <a href="/computing/datacenter-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                 <a href="/computing/datacenter-cluster?id=${id}" style="text-decoration-line: none">클러스터</a> |
                                 <a href="/computing/datacenter-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                 <a href="/computing/datacenter-event?id=${id}">이벤트</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td>시간</td>
                        <td>메시지</td>
                        <td>상관 관계 ID</td>
                        <td>소스</td>
                        <td>사용자 지정 이벤트 ID</td>
                    </tr>

                    <c:if test="${empty event}">
                        <tr>
                            <td colspan="6" style="text-align: center">표시할 항목이 없습니다</td>
                        </tr>
                    </c:if>
                    <c:forEach var="event" items="${event}" varStatus="status">
                        <tr>
                            <td>${event.severity}</td>
                            <td>${event.time}</td>
                            <td>${event.message}</td>
                            <td>${event.relationId}</td>
                            <td>${event.source}</td>
                            <td></td>
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
