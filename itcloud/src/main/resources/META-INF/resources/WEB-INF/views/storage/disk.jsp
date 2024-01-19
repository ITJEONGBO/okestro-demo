<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Disk</h1>
                    스토리지 > <a href="/storage/disks" style="text-decoration-line: none">디스크</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/storage/disk?id=${id}">일반</a> |
                                <a href="/storage/disk-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/storage/disk-storage?id=${id}" style="text-decoration-line: none">스토리지</a> |
                                <a href="/storage/disk-permission?id=${id}" style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                    <div>
                        별칭: ${disk.alias}<br>
                        설명: ${disk.description}<br>
                        ID: ${disk.id}<br>
                        디스크 프로파일: ${disk.diskProfileName}<br>
                        삭제 후 초기화: ${disk.format}<br>
                        가상 크기: ${disk.virtualSize / (1024*1024*1024)}GB<br>
                        실제 크기: ${disk.actualSize / (1024*1024*1024)}GB<br>
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
