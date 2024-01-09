<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Host</h1>
                    컴퓨팅 > <a href="/computing/hosts" style="text-decoration-line: none">호스트</a> <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/host?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/host-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/host-nic?id=${id}">네트워크 인터페이스</a> |
                                <a href="/computing/host-device?id=${id}" style="text-decoration-line: none">호스트 장치</a> |
                                <a href="/computing/host-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/host-aff?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/host-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>상태</td>
                            <td>이름</td>
                            <td>MAC</td>
                            <td>Rx 속도(Mbps)</td>
                            <td>총 Rx 속도</td>
                            <td>Tx 속도</td>
                            <td>총 Tx 속도</td>
                            <td>속도</td>
                            <td>중단 속도</td>
                        </tr>

                        <c:if test="${empty nic}">
                            <tr>
                                <td>nic 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="nic" items="${nic}" varStatus="status">
                            <tr>
                                <td>${nic.status == "up" ? "⬆️" : "️️️⬇️"}</td>
                                <td>${nic.name}</td>
                                <td>${nic.macAddress}</td>
                                <td>${nic.rxSpeed < 1 ? "< 1" : nic.rxSpeed}</td>
                                <td>${nic.rxTotalSpeed}</td>
                                <td>${nic.txSpeed< 1 ? "< 1" : nic.txSpeed}</td>
                                <td>${nic.txTotalSpeed}</td>
                                <td>${nic.speed}</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="9"></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td></td>
                                <td>관리되지 않음</td>
                                <td>VLAN</td>
                                <td>네트워크 이름</td>
                                <td>IPv4 주소</td>
                                <td>IPv6 주소</td>
                            </tr>
                            <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td>${nic.networkName}</td>
                                <td>${nic.ipv4}</td>
                                <td>${nic.ipv6}</td>
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
