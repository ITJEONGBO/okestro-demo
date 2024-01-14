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
                                <a href="/network/network-cluster?id=${id}" style="text-decoration-line: none">클러스터</a> |
                                <a href="/network/network-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/network/network-vm?id=${id}">가상머신</a> |
                                <a href="/network/network-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="/network/network-permission?id=${id}" style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td></td>
                            <td>이름</td>
                            <td>클러스터</td>
                            <td>IP 주소</td>
                            <td>FQDN</td>
                            <td>vNIC 상태</td>
                            <td>vNIC</td>
                            <td>vNIC Rx (Mbps)</td>
                            <td>vNIC Tx (Mbps)</td>
                            <td>총 Rx (바이트)</td>
                            <td>총 Tx (바이트)</td>
                            <td>설명</td>
                        </tr>

                        <c:if test="${empty vm}">
                            <tr>
                                <td colspan="12" style="text-align: center">표시할 항목이 없습니다</td>
                            </tr>
                        </c:if>
                        <c:forEach var="vm" items="${vm}" varStatus="status">
                            <tr>
                                <td>${vm.status == "up" ? "▲" : "▽"}</td>
                                <td><a href="/computing/vm?id=${vm.vmId}" style="text-decoration-line: none">${vm.vmName}</a></td>
                                <td>${vm.clusterName}</td>
                                <td>${vm.ipv4}<br>${vm.ipv6}</td>
                                <td>${vm.fqdn}</td>
                                <td>${vm.vnicStatus == "true" ? "▲" : vm.vnicStatus}</td>
                                <td>${vm.vnicName}</td>
                                <td>${vm.vnicRx < 1 ? "< 1" : vm.vnicRx}</td>
                                <td>${vm.vnicTx < 1 ? "< 1" : vm.vnicTx}</td>
                                <td>${vm.rxTotalSpeed}</td>
                                <td>${vm.txTotalSpeed}</td>
                                <td>${vm.description}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    <br><br><br><br><br>
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
