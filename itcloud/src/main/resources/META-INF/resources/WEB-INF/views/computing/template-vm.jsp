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
                                <a href="/computing/template-vm?id=${id}">가상머신</a> |
                                <a href="/computing/template-nic?id=${id}" style="text-decoration-line: none">네트워크 인터페이스</a> |
                                <a href="/computing/template-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/computing/template-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                <a href="/computing/template-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/template-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table>
                        <tr>
                            <td></td>
                            <td>이름</td>
                            <td></td>
                            <td>호스트</td>
                            <td>IP 주소</td>
                            <td>fqdn</td>
                            <td>상태</td>
                            <td>업타임</td>
                            <td>콘솔 사용자</td>
                            <td>로그인된 사용자</td>
                        </tr>

                        <c:if test="${empty vm}">
                            <tr>
                                <td colspan="10">vm 없음</td>
                            </tr>
                        </c:if>
                        <c:forEach var="vm" items="${vm}" varStatus="status">
                            <tr>
                                <td>${vm.status == "up" ? "▲" : "▽"}</td>
                                <td><a href="/computing/vm?id=${vm.id}" style="text-decoration-line: none">${vm.name}</a></td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>${vm.hostName}</td>
                                <td>${vm.ipv4}<br> ${vm.ipv6}</td>
                                <td>${vm.fqdn}</td>
                                <td>${vm.status}</td>
                                <td>${vm.upTime != 0 ? vm.upTime:""}</td>
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
