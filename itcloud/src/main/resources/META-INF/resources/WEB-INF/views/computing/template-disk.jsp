<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Template</h1>
                    컴퓨팅 > <a href="/computing/templates" style="text-decoration-line: none">템플릿</a> <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/template?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/template-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/template-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/template-disk?id=${id}">디스크</a> |
                                <a href="/computing/template-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                <a href="/computing/template-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/template-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td>별칭</td>
                            <td>R/O</td>
                            <td>가상크기</td>
                            <td>실제크기</td>
                            <td>상태</td>
                            <td>할당 정책</td>
                            <td>인터페이스</td>
                            <td>유형</td>
                            <td>생성일자</td>
                        </tr>

                        <c:if test="${empty disk}">
                            <tr>
                                <td colspan="9">disk 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="disk" items="${disk}" varStatus="status">
                            <tr>
                                <td>${disk.name}</td>
                                <td></td>
                                <td>${disk.virtualSize / (1024*1024*1024)} GB</td>
                                <td></td>
                                <td>${disk.status}</td>
                                <td></td>
                                <td>${disk.interfaceName}</td>
                                <td>${disk.type}</td>
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
