<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Host</h1>
                    <a href="/computing/hosts" style="text-decoration-line: none">호스트</a> - 가상머신 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/host?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/host-vm?id=${id}">가상머신</a> |
                                <a href="/computing/host-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/host-device?id=${id}" style="text-decoration-line: none">호스트 장치</a> |
                                <a href="#" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/host-aff?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="#" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>이름</td>
                            <td>상태</td>
                            <td>클러스터</td>
                            <td>ip 주소(v4)</td>
                            <td>ip 주소(v6)</td>
                            <td>fqdn</td>
                            <td>startTime</td>
                        </tr>

                        <c:if test="${empty vm}">
                            <tr>
                                <td>vm 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="vm" items="${vm}" varStatus="status">
                            <tr>
                                <td>${vm.name}</td>
                                <td>${vm.status}</td>
                                <td>${vm.clusterName}</td>
                                <td>${vm.ipv4}</td>
                                <td>${vm.ipv6}</td>
                                <td>${vm.fqdn}</td>
                                <td>${vm.startTime}</td>
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
