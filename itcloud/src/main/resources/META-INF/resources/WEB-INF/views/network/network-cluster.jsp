<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Network</h1>
                    네트워크 > <a href="/network/networks" style="text-decoration-line: none">네트워크</a><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/network/network?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/network/network-vnicProfile?id=${id}" style="text-decoration-line: none">vNIC 프로파일</a> |
                                <a href="/network/network-cluster?id=${id}">클러스터</a> |
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
                            <td>호환 버전</td>
                            <td>연결된 네트워크</td>
                            <td>네트워크 상태</td>
                            <td>필수 네트워크</td>
                            <td>네트워크 역할</td>
                            <td>설명</td>
                        </tr>

                        <c:if test="${empty cluster}">
                            <tr>
                                <td colspan="7">표시할 항목이 없습니다.</td>
                            </tr>
                        </c:if>
                        <c:forEach var="cluster" items="${cluster}" varStatus="status">
                            <tr>
                                <td><a href="/computing/cluster?id=${cluster.id}" style="text-decoration-line: none">${cluster.name}</a></td>
                                <td>${cluster.version}</td>
                                <td></td>
                                <td>${cluster.status == "operational" ? "▲" : "▽"}</td>
                                <td></td>
                                <td>
                                    ${cluster.networkUsageVo.vm == "true" ? "vm" : ""}
                                    ${cluster.networkUsageVo.management == "true" ? "management" : ""}
                                    ${cluster.networkUsageVo.display == "true" ? "display" : ""}
                                    ${cluster.networkUsageVo.migration == "true" ? "migration" : ""}
                                    ${cluster.networkUsageVo.gluster == "true" ? "gluster" : ""}
                                    ${cluster.networkUsageVo.defaultRoute == "true" ? "defaultRoute" : ""}
                                </td>
                                <td>${cluster.description}</td>
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
