<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Template</h1>
                    컴퓨팅 > <a href="/computing/templates" style="text-decoration-line: none">템플릿</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/template?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/computing/template-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/template-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/template-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/template-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                <a href="/computing/template-permission?id=${id}">권한</a> |
                                <a href="/computing/template-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td>사용자</td>
                        <td>인증 공급자</td>
                        <td>네임스페이스</td>
                        <td>역할</td>
                        <td>생성일</td>
                        <td>Inherited From</td>
                    </tr>

                    <c:if test="${empty permission}">
                        <tr>
                            <td colspan="7" style="text-align: center">표시할 항목이 없습니다</td>
                        </tr>
                    </c:if>
                    <c:forEach var="permission" items="${permission}" varStatus="status">
                        <tr>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td>${permission.user}</td>
                            <td>${permission.provider}</td>
                            <td>${permission.nameSpace}</td>
                            <td>${permission.role}</td>
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