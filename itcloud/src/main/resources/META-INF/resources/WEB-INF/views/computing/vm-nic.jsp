<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Vm</h1>
                    컴퓨팅 > <a href="/computing/vms" style="text-decoration-line: none">가상머신</a>  <br><br>

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
                                <a href="/computing/vm-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/vm-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <c:if test="${empty nic}">
                        <div> 표시항목 없음 </div>
                        <br><br><br><br><br>
                    </c:if>
                    <c:forEach var="nic" items="${nic}" varStatus="status">
                     <table>
                        <tr>
                            <td>상태</td>
                            <td>이름</td>
                            <td>네트워크 이름</td>
                            <td>IPv4</td>
                            <td>IPv6</td>
                            <td>MAC</td>
                        </tr>
                        <tr>
                            <td>${nic.linkStatus == "true" ? "⬆️" : "️️️⬇️"}</td>
                            <td>${nic.name}</td>
                            <td>${nic.networkName}</td>
                            <td>${nic.ipv4}</td>
                            <td>${nic.ipv6}</td>
                            <td>${nic.macAddress}</td>
                        </tr>
                        <tr>
                            <td colspan="6"></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h3>일반</h3>
                                연결됨: ${nic.plugged == "true" ? "연결됨" : "x"} <br>
                                네트워크 이름: ${nic.networkName} <br>
                                프로파일 이름: ${nic.vnicProfileVo.name} <br>
                                링크 상태: ${nic.linkStatus == "true" ? "up" : "down"} <br>
                                유형: ${nic.type} <br>
                                속도(Mbps): ${nic.speed2} <br>
                                포트 미러링: ${nic.vnicProfileVo.portMirroring == "true" ? "활성화" : "비활성화"} <br>
                                게스트 인터페이스 이름: ${nic.guestInterface} <br>
                            </td>
                            <td colspan="2">
                                <h3>통계</h3>
                                Rx 속도(Mbps): ${nic.rxSpeed < 1 ? "< 1" : nic.rxSpeed} <br>
                                Tx 속도(Mbps): ${nic.txSpeed < 1 ? "< 1" : nic.txSpeed} <br>
                                총 Rx: ${nic.rxTotalSpeed} <br>
                                총 Tx: ${nic.txTotalSpeed} <br>
                                중단 (Pkts): ${nic.stop} <br>
                                <br><br><br>
                            </td>
                            <td colspan="2">
                                <h3>네트워크 필터 매개변수</h3>
                                <br><br><br><br><br><br><br>
                            </td>
                        </tr>
                    </table>
                    </c:forEach>

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
