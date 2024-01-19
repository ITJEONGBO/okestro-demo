<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">StorageDomain</h1>
                    스토리지 > <a href="/storage/storageDomains" style="text-decoration-line: none">스토리지 도메인</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/storage/storageDomain?id=${id}" style="text-decoration-line: none">일반</a> |
                                <a href="/storage/storageDomain-datacenter?id=${id}">데이터 센터</a> |
                                <a href="/storage/storageDomain-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/storage/storageDomain-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="/storage/storageDomain-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/storage/storageDomain-snapshot?id=${id}" style="text-decoration-line: none">디스크 스냅샷</a> |
                                <a href="/storage/storageDomain-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/storage/storageDomain-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <table width="700px">
                        <tr>
                            <td></td>
                            <td>이름</td>
                            <td>데이터 센터 내의 도메인 상태</td>
                        </tr>

                        <c:if test="${empty datacenter}">
                            <tr>
                                <td colspan="3">표시할 항목이 없습니다.</td>
                            </tr>
                        </c:if>
                        <c:forEach var="datacenter" items="${datacenter}" varStatus="status">
                            <tr>
                                <td>${datacenter.status  == "up" ? "▲" : "▽"}</td>
                                <td><a href="/computing/datacenter-storage?id=${datacenter.id}" style="text-decoration-line: none">${datacenter.name}</a></td>
                                <td>${datacenter.status}</td>
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
