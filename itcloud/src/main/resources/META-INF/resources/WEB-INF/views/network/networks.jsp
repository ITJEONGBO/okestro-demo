<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Network</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                네트워크 > <a href="/network/networks" style="text-decoration-line: none">네트워크</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>이름</td>
                        <td>코멘트</td>
                        <td>데이터 센터</td>
                        <td>설명</td>
                        <td>역할</td>
                        <td>VLAN 태그</td>
                        <td>레이블</td>
                        <td>공급자</td>
                        <td>MTU</td>
                        <td>포트 분리</td>
                    </tr>

                    <c:if test="${empty networks}">
                        <tr>
                            <td colspan="9">표시할 항목이 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="networks" items="${networks}" varStatus="status">
                        <tr>
                            <td><a href="/network/network?id=${networks.id} "style="text-decoration-line: none">${networks.name}</a></td>
                            <td>${networks.comment}</td>
                            <td><a href="/computing/datacenter-storage?id=${networks.datacenterId} "style="text-decoration-line: none">${networks.datacenterName}</a></td>
                            <td>${networks.description}</td>
                            <td>
                                ${networks.networkUsageVo.vm =="true" ? "vm":""}
                                ${networks.networkUsageVo.management =="true" ? "management":""}
                                ${networks.networkUsageVo.display =="true" ? "display":""}
                                ${networks.networkUsageVo.migration =="true" ? "migration":""}
                                ${networks.networkUsageVo.gluster =="true" ? "gluster":""}
                                ${networks.networkUsageVo.defaultRoute =="true" ? "defaultRoute":""}
                            </td>
                            <td>${networks.vlan == null ? "-" : networks.vlan}</td>
                            <td></td>
                            <td></td>
                            <td>${networks.mtu == 0 ? "기본값(1500)" : networks.mtu}</td>
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
