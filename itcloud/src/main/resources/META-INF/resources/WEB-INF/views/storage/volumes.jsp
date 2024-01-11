<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Storage</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                스토리지 > <a href="/storage/volumes" style="text-decoration-line: none">볼륨</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td>이름</td>
                        <td>클러스터</td>
                        <td>볼륨</td>
                        <td>브릭</td>
                        <td>정보</td>
                        <td>사용된 공간</td>
                        <td>작업</td>
                        <td>스냅샷 수</td>
                    </tr>

                    <c:if test="${empty volumes}">
                        <tr>
                            <td colspan="9">표시할 항목이 없습니다.</td>
                        </tr>
                    </c:if>
                    <c:forEach var="volumes" items="${volumes}" varStatus="status">
                        <tr>
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
