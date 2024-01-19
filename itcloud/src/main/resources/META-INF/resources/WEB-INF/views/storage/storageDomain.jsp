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
                                <a href="/storage/storageDomain?id=${id}">일반</a> |
                                <a href="/storage/storageDomain-datacenter?id=${id}" style="text-decoration-line: none">데이터 센터</a> |
                                <a href="/storage/storageDomain-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/storage/storageDomain-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="/storage/storageDomain-disk?id=${id}" style="text-decoration-line: none">디스크</a> |
                                <a href="/storage/storageDomain-snapshot?id=${id}" style="text-decoration-line: none">디스크 스냅샷</a> |
                                <a href="/storage/storageDomain-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/storage/storageDomain-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <div>
                        ID: ${domain.id}<br>
                        크기: ${domain.diskSize / (1024*1024*1024)}GB<br>
                        사용 가능: ${domain.availableSize / (1024*1024*1024)}GB<br>
                        사용됨: ${domain.usedSize / (1024*1024*1024)}GB<br>
                        할당됨: ${domain.commitedSize / (1024*1024*1024)}GB<br>
                        오버 할당 비율: <br>
                        이미지: <br>
                        경로: ${domain.storageAddress} ${domain.storagePath}<br>
                        NFS 버전: ${domain.nfsVersion}<br>
                        디스크 공간 부족 경고 표시: ${domain.warning}% <br>
                        심각히 부족한 디스크 공간의 동작 차단: ${domain.blockSize}GB<br>
                    </div>

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
