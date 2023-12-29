<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a> - 일반 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/cluster?id=${id}">일반</a> |
                                <a href="/computing/cluster-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                <a href="/computing/cluster-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/computing/cluster-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/cluster-aff?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/cluster-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/cluster-cpu?id=${id}" style="text-decoration-line: none">CPU 프로파일</a> |
                                <a href="#" style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                    <div>
                        이름: &nbsp;${cluster.name} <br>
                        설명: &nbsp;${cluster.description} <br>
                        데이터센터: &nbsp;${cluster.datacenterId} <br>
                        호환버전: &nbsp;${cluster.version} <br>
                        클러스터 ID: &nbsp;${cluster.id} <br>
                        클러스터 CPU 유형: &nbsp;${cluster.cpuType} <br>
                        스레드를 CPU로 사용: &nbsp;${cluster.threadsAsCore ? "예":"아니요"} <br>
                        최대 메모리 오버 커밋: &nbsp;${cluster.memoryOverCommit} <br>
                        복구정책: &nbsp;(해야함) <br>
                        칩셋/펌웨어 유형: &nbsp;${cluster.chipsetFirmwareType} <br>
                        가상머신 수: &nbsp;${cluster.vmCnt} <br>
                    </div>
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
