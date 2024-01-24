<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    컴퓨팅 > <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/cluster?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/cluster-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                <a href="/computing/cluster-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/computing/cluster-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/cluster-affGroup?id=${id}">선호도 그룹</a> |
                                <a href="/computing/cluster-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/cluster-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/cluster-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>상태</td>
                            <td>이름</td>
                            <td>설명</td>
                            <td>우선순위</td>
                            <td>가상머신 측 극성</td>
                            <td>가상머신 강제적용</td>
                            <td>호스트 측 극성</td>
                            <td>호스트 강제적용</td>
                            <td>가상머신 멤버</td>
                            <td>가상머신 레이블</td>
                            <td>호스트 멤버</td>
                            <td>호스트 레이블</td>
                        </tr>

                        <c:if test="${empty aff}">
                            <tr>
                                <td colspan="12" style="text-align: center">표시할 항목이 없습니다</td>
                            </tr>
                        </c:if>
                        <c:forEach var="aff" items="${aff}" varStatus="status">
                        <tr>
                            <td>${aff.status == "true" ? "" : "!"}</td>
                            <td>${aff.name}</td>
                            <td>${aff.description}</td>
                            <td>${aff.priority}</td>
                            <td>${aff.vmEnabled && aff.positive ? (aff.vmPositive == "true" ? "양극":"음극") : ""}</td>
                            <td>${aff.vmEnforcing ? "하드" : "소프트"}</td>
                            <td>${aff.hostEnabled && aff.positive ? (aff.hostPositive == "true" ? "양극":"음극") : ""}</td>
                            <td>${aff.hostEnforcing ? "하드" : "소프트"}</td>
                            <td>
                                <c:forEach var="vmList" items="${aff.vmList}" varStatus="status">
                                    ${vmList}
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="vmLabels" items="${aff.vmLabels}" varStatus="status">
                                    ${vmLabels}
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="hostList" items="${aff.hostList}" varStatus="status">
                                    ${hostList}
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="hostLabels" items="${aff.hostLabels}" varStatus="status">
                                    ${hostLabels}
                                </c:forEach>
                            </td>
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