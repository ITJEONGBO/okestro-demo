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
                                <a href="/storage/storageDomain-datacenter?id=${id}" style="text-decoration-line: none">데이터 센터</a> |
                                <a href="/storage/storageDomain-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/storage/storageDomain-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="/storage/storageDomain-disk?id=${id}">디스크</a> |
                                <a href="/storage/storageDomain-snapshot?id=${id}" style="text-decoration-line: none">디스크 스냅샷</a> |
                                <a href="/storage/storageDomain-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/storage/storageDomain-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>별칭</td>
                        <td>부팅가능</td>
                        <td>공유가능</td>
                        <td>가상 크기</td>
                        <td>실제 크기</td>
                        <td>할당 정책</td>
                        <td>스토리지 도메인</td>
                        <td>생성 일자</td>
                        <td>최근 업데이트</td>
                        <td></td>
                        <td>연결대상</td>
                        <td>상태</td>
                        <td>유형</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty disks}">
                        <tr>
                            <td colspan="14" style="text-align: center">disk 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="disks" items="${disks}" varStatus="status">
                        <tr>
                            <td width="120px" style="word-break:break-all"><a href="/storage/disk?id=${disks.id}" style="text-decoration-line: none">${disks.alias}</a></td>
                            <td></td>
                            <td>${disks.shareable}</td>
                            <td>${disks.virtualSize / (1024*1024*1024) < 1 ? "< 1" : disks.virtualSize / (1024*1024*1024)} GB</td>
                            <td>${disks.actualSize / (1024*1024*1024) < 1 ? "< 1" : disks.actualSize / (1024*1024*1024)} GB</td>
                            <td></td>
                            <td>${disks.storageDomainName}</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>${disks.connection}</td>
                            <td>${disks.status}</td>
                            <td>${disks.storageType}</td>
                            <td>${disks.description}</td>
                        </tr>
                    </c:forEach>
                </table>
                <br><br><br><br>

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
