<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Network</h1>
                    네트워크 > <a href="/network/networks" style="text-decoration-line: none">네트워크 > ${name} </a><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/network/network?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/network/network-vnicProfile?id=${id}">vNIC 프로파일</a> |
                                <a href="/network/network-cluster?id=${id}" style="text-decoration-line: none">클러스터</a> |
                                <a href="/network/network-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/network/network-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/network/network-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="/network/network-permission?id=${id}" style="text-decoration-line: none">권한</a>
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

                        <c:if test="${empty vnic}">
                            <tr>
                                <td colspan="9" style="text-align: center">표시할 항목이 없습니다</td>
                            </tr>
                        </c:if>
                        <c:forEach var="vnic" items="${vnic}" varStatus="status">
                            <tr>
                                <td>${vnic.name}</td>
                                <td>${vnic.networkName}</td>
                                <td><a href="/computing/datacenter-storage?id=${vnic.datacenterId}" style="text-decoration-line: none">${vnic.datacenterName}</a></td>
                                <td>${vnic.version}</td>
                                <td>${vnic.networkFilterName}</td>
                                <td>${vnic.portMirroring == "true" ? "활성화됨" : ""}</td>
                                <td>${vnic.passThrough == "disabled" ? "아니요" : "네"}</td>
                                <td></td>
                                <td>${vnic.description}</td>
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
