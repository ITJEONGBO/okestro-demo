<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Vm</h1>
                    컴퓨팅 > <a href="/computing/vms" style="text-decoration-line: none">가상머신</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/vm?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/vm-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/vm-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/vm-snapshot?id=${id}">스냅샷</a> |
                                <a href="/computing/vm-application?id=${id}" style="text-decoration-line: none">애플리케이션</a> |
                                <a href="/computing/vm-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/vm-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/vm-guest?id=${id}" style="text-decoration-line: none">게스트 정보</a> |
                                <a href="/computing/vm-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/vm-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                <c:if test="${empty snapshot}">
                        <div>snapshot 없음</div>
                </c:if>
                <c:forEach var="snapshot" items="${snapshot}" varStatus="status">
                    <table>
                        <tr>
                            <td>${snapshot.description}</td>
                        </tr>
                        <tr>
                            <td>
                                <h5>일반</h5>
                                날짜: ${snapshot.date}<br>
                                상태: ${snapshot.status}<br>
                                메모리: ${snapshot.persistMemory}<br>
                                설명: ${snapshot.description}<br>
                                설정된 메모리: MB<br>
                                할당된 실제 메모리: MB<br>
                                CPU 코어 수: <br>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <h5>디스크</h5>
                                <table>
                                    <tr>
                                        <td>상태</td>
                                        <td>별칭</td>
                                        <td>가상크기</td>
                                        <td>실제크기</td>
                                        <td>할당정책</td>
                                        <td>인터페이스</td>
                                        <td>생성 일자</td>
                                        <td>디스크 스냅샷 ID</td>
                                        <td>유형</td>
                                        <td>설명</td>
                                    </tr>
                                    <tr>
                                        <td>ㅇㄹㅇ</td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h5>네트워크 인터페이스</h5>
                                이름: <br>
                                네트워크 이름: <br>
                                프로파일 이름: <br>
                                유형: <br>
                                MAC: <br>
                                Rx 속도(Mbps): <br>
                                Tx 속도(Mbps): <br>
                                중단(Pkts):
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h5>설치된 애플리케이션</h5>

                            </td>
                        </tr>
                    </table>
                </c:forEach>
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
