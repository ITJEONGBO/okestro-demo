<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    컴퓨팅 > <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a> > <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/cluster?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/cluster-network?id=${id}">논리 네트워크</a> |
                                <a href="/computing/cluster-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/computing/cluster-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/cluster-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/cluster-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/cluster-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/cluster-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td></td>
                            <td width="120px">이름</td>
                            <td>상태</td>
                            <td>역할</td>
                            <td>설명</td>
                        </tr>

                        <c:if test="${empty network}">
                            <tr>
                                <td>네트워크가 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="network" items="${network}" varStatus="status">
                            <tr>
                                <td>${network.status == "operational" ? "🔼" : "🔽"}</td>
                                <td><a href="/network/network?id=${network.id}" style="text-decoration-line: none">${network.name}</a></td>
                                <td>${network.status == "operational" ? "가동 중" : "비 가동?"}</td>
                                <td>
                                    ${network.networkUsageVo.vm =="true" ? "vm":""}
                                    ${network.networkUsageVo.management =="true" ? "management":""}
                                    ${network.networkUsageVo.display =="true" ? "display":""}
                                    ${network.networkUsageVo.migration =="true" ? "migration":""}
                                    ${network.networkUsageVo.gluster =="true" ? "gluster":""}
                                    ${network.networkUsageVo.defaultRoute =="true" ? "defaultRoute":""}
                                </td>
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
