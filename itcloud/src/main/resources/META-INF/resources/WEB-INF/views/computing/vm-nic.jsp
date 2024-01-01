<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">가상머신</h1>
                    <a href="/computing/vms" style="text-decoration-line: none">가상머신</a> - 네트워크 인터페이스 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/vm?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/vm-nic?id=${id}">네트워크 인터페이스</a> |
                                <a href="/computing/vm-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/vm-snapshot?id=${id}" style="text-decoration-line: none">스냅샷</a> |
                                <a href="/computing/vm-application?id=${id}" style="text-decoration-line: none">애플리케이션</a> |
                                <a href="/computing/vm-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/vm-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/vm-guest?id=${id}" style="text-decoration-line: none">게스트 정보</a> |
                                <a href="#" style="text-decoration-line: none">권한</a> |
                                <a href="#" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>id</td>
                            <td>이름</td>
                            <td>MAC</td>
                            <td>속도</td>
                        </tr>

                        <c:if test="${empty nic}">
                            <tr>
                                <td>nic 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="nic" items="${nic}" varStatus="status">
                            <tr>
                                <td colspan="9">
                                    id: ${nic.id} <br>
                                    네트워크 이름: ${nic.name} <br>
                                    IPv4: ${nic.ipv4} <br>
                                    IPv6: ${nic.ipv6} <br>
                                    MAC: ${nic.macAddress} <br>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <h3>일반</h3>
                                    연결됨: ${nic.} <br>
                                    네트워크 이름: ${nic.} <br>
                                    프로파일 이름: ${nic.} <br>
                                    Qos 이름: ${nic.} <br>
                                    링크 상태: ${nic.} <br>
                                    유형: ${nic.} <br>
                                    속도(Mbps): ${nic.} <br>
                                    포트 미러링: ${nic.} <br>
                                    게스트 인터페이스 이름: ${nic.} <br>
                                </td>
                                <td>
                                    <h3>통계</h3>
                                    Rx 속도(Mbps): ${nic.} <br>
                                    Tx 속도(Mbps): ${nic.} <br>
                                    총 Rx: ${nic.} <br>
                                    총 Tx: ${nic.} <br>
                                    중단 (Pkts): ${nic.} <br>
                                </td>
                                <td>
                                    <h3>네트워크 필터 매개변수</h3>
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
