<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">VNIC 프로파일</h1>
                    네트워크 > <a href="/network/vnicProfiles" style="text-decoration-line: none">VNIC 프로파일</a>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/network/vnicProfile-vm?id=${id}">가상머신</a> |
                                <a href="/network/vnicProfile-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="#" style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                    <table width="700px">
                        <tr>
                            <td>이름</td>
                        </tr>

                        <c:if test="${empty vm}">
                            <tr>
                                <td>vm 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="vm" items="${vm}" varStatus="status">
                            <tr>
                                <td><a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none">${vm.name}</a></td>
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
