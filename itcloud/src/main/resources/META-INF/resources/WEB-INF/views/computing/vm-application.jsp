<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Vm</h1>
                    컴퓨팅 > <a href="/computing/vms" style="text-decoration-line: none">가상머신</a> > 애플리케이션 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/vm?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/vm-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/vm-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/vm-snapshot?id=${id}" style="text-decoration-line: none">스냅샷</a> |
                                <a href="/computing/vm-application?id=${id}">애플리케이션</a> |
                                <a href="/computing/vm-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/vm-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/vm-guest?id=${id}" style="text-decoration-line: none">게스트 정보</a> |
                                <a href="#" style="text-decoration-line: none">권한</a> |
                                <a href="#" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table width="700px">
                        <tr>
                            <td>설치된 애플리케이션</td>
                        </tr>

                        <c:if test="${empty app}">
                            <tr>
                                <td>disk 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="app" items="${app}" varStatus="status">
                            <tr>
                                <td>${app.name}</td>
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
