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
                                스토리지 > <a href="/storage/storageDomains" style="text-decoration-line: none">스토리지 도메인</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>상태</td>
                        <td></td>
                        <td>도메인 이름</td>
                        <td>코멘트</td>
                        <td>도메인 유형</td>
                        <td>스토리지 유형</td>
                        <td>포맷</td>
                        <td>데이터 센터간 상태</td>
                        <td>전체 공간(GB)</td>
                        <td>여유 공간(GB)</td>
                        <td>확보된 여유 공간</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty domains}">
                        <tr>
                            <td colspan="12" rowspan="3">스토리지 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="domains" items="${domains}" varStatus="status">
                        <tr>
                            <td>${domains.status == "ok" ? "▲" : "▽"}</td>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td><a href="/storage/storageDomain?id=${domains.id}" style="text-decoration-line: none">${domains.name}</a></td>
                            <td>${domains.comment}</td>
                            <td>${domains.domainType}</td>
                            <td>${domains.storageType}</td>
                            <td>${domains.domainFormat}</td>
                            <td>${domains.status}</td>
                            <td>${domains.diskSize/(1024*1024*1024)}</td>
                            <td>${domains.availableSize /(1024*1024*1024)}GB</td>
                            <td>${domains.availableSize /(1024*1024*1024)}GB</td>
                            <td>${domains.description}</td>
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
