<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

<style>
    table, tr, td{
        border:none;
    }
</style>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    컴퓨팅 > <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a> > ${name} <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/cluster?id=${id}">일반</a> |
                                <a href="/computing/cluster-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                <a href="/computing/cluster-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/computing/cluster-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/computing/cluster-affGroup?id=${id}" style="text-decoration-line: none">선호도 그룹</a> |
                                <a href="/computing/cluster-affLabel?id=${id}" style="text-decoration-line: none">선호도 레이블</a> |
                                <a href="/computing/cluster-permission?id=${id}" style="text-decoration-line: none">권한</a> |
                                <a href="/computing/cluster-event?id=${id}" style="text-decoration-line: none">이벤트</a>
                            </p>
                        </div>
                    </div>

                    <div>
                        <table>
                            <tr>
                                <td>이름</td>
                                <td>&emsp; ${cluster.name}</td>
                            </tr>
                            <tr>
                                <td>설명</td>
                                <td>&emsp; ${cluster.description}</td>
                            </tr>
                            <tr>
                                <td>데이터센터</td>
                                <td>&emsp; ${cluster.datacenterName}</td>
                            </tr>

                            <tr>
                                <td>호환버전</td>
                                <td>&emsp; ${cluster.version}</td>
                            </tr>
                            <tr>
                                <td>클러스터 노드 유형</td>
                                <td>&emsp; ${cluster.virt ? "Virt" : ""} ${cluster.gluster ? "Gluster" : ""} </td>
                            </tr>
                            <tr>
                                <td>클러스터 ID</td>
                                <td>&emsp; ${cluster.id}</td>
                            </tr>
                            <tr>
                                <td></td>
                            </tr>
                            <tr>
                                <td>클러스터 CPU 유형</td>
                                <td>&emsp; ${cluster.cpuType}</td>
                            </tr>
                            <tr>
                                <td>스레드를 CPU로 사용</td>
                                <td>&emsp; ${cluster.threadsAsCore ? "예" : "아니요"}</td>
                            </tr>
                            <tr>
                                <td>최대 메모리 오버 커밋</td>
                                <td>&emsp; ${cluster.memoryOverCommit}%</td>
                            </tr>
                            <tr>
                                <td>복구정책</td>
                                <td>&emsp; ${cluster.restoration}</td>
                            </tr>
                            <tr>
                                <td>칩셋/펌웨어 유형</td>
                                <td>&emsp; ${cluster.chipsetFirmwareType}</td>
                            </tr>
                            <tr>
                                <td>가상머신 수</td>
                                <td>&emsp; ${cluster.vmCnt}</td>
                            </tr>
                        </table>

                        <br><br><br><br>
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
