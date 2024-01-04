<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">DataCenter</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/datacenters" style="text-decoration-line: none">데이터 센터</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td width="200px">이름</td>
                        <td>코멘트</td>
                        <td>스토리지 유형</td>
                        <td>상태</td>
                        <td>호환 버전</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty datacenters}">
                        <tr>
                            <td>데이터센터가 없음 / 근데 이거 말이 안됨요</td>
                        </tr>
                    </c:if>
                    <c:forEach var="datacenters" items="${datacenters}" varStatus="status">
                        <tr>
                            <td>${datacenters.status == "up" ? "🔼" : "🔽"}</td>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td><a href="/computing/datacenter-storage?id=${datacenters.id}">${datacenters.name}</a> </td>
                            <td>${datacenters.comment}</td>
                            <td>${datacenters.storageType ? "로컬" : "공유됨"}</td>
                            <td>${datacenters.status}</td>
                            <td>${datacenters.version}</td>
                            <td>${datacenters.description}</td>
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
