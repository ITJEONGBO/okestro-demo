<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">VNIC 프로파일</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                네트워크 > <a href="/network/vnicProfiles" style="text-decoration-line: none">VNIC 프로파일</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>이름</td>
                        <td>네트워크</td>
                        <td>데이터 센터</td>
                        <td>호환 버전</td>
                        <td>네트워크 필터</td>
                        <td>포트 미러링</td>
                        <td>통과</td>
                        <td>페일오버 vNIC 프로파일</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty vnics}">
                        <tr>
                            <td colspan="9">표시할 항목이 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="vnics" items="${vnics}" varStatus="status">
                        <tr>
                            <td><a href="/network/vnicProfile-vm?id=${vnics.id} "style="text-decoration-line: none">${vnics.name}</a></td>
                            <td>${vnics.networkName}</td>
                            <td><a href="/computing/datacenter-storage?id=${vnics.datacenterId} "style="text-decoration-line: none">${vnics.datacenterName}</a></td>
                            <td>${vnics.version}</td>
                            <td>${vnics.networkFilterName}</td>
                            <td>${vnics.portMirroring}</td>
                            <td>${vnics.passThrough == "true" ? "예" : "아니요"}</td>
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
