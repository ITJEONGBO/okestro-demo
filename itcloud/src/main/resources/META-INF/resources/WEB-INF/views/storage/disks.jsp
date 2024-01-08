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
                                스토리지 > <a href="/storage/disks" style="text-decoration-line: none">디스크</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>별칭</td>
                        <td>ID</td>
                        <td>공유가능</td>
                        <td></td>
                        <td>연결대상</td>
                        <td>스토리지 도메인</td>
                        <td>가상크기</td>
                        <td>상태</td>
                        <td>유형</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty disks}">
                        <tr>
                            <td colspan="9">disk 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="disks" items="${disks}" varStatus="status">
                        <tr>
                            <td width="120px" style="word-break:break-all"><a href="/storage/disk?id=${disks.id}" style="text-decoration-line: none">${disks.alias}</a></td>
                            <td>${disks.id}</td>
                            <td>${disks.shareable}</td>
                            <td></td>
                            <td></td>
                            <td>${disks.storageDomainName}</td>
                            <td>${disks.virtualSize / (1024*1024*1024)} GB</td>
                            <td>${disks.status}</td>
                            <td>${disks.storageType}</td>
                            <td>${disks.description}</td>
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
