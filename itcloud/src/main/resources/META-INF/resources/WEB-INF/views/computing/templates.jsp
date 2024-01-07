<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Template</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/templates" style="text-decoration-line: none">템플릿</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>이름</td>
                        <td>버전</td>
                        <td>생성 일자</td>
                        <td>상태</td>
                        <td>클러스터</td>
                        <td>데이터센터</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty templates}">
                        <tr>
                            <td colspan="9">templates 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="templates" items="${templates}" varStatus="status">
                        <tr>
                            <td><a href="/computing/template?id=${templates.id}">${templates.name}</a></td>
                            <td>${templates.version}</td>
                            <td>${templates.createDate}</td>
                            <td>${templates.status}</td>
                            <td></td>
                            <td></td>
                            <td>${templates.description}</td>
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
