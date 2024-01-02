<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Dashboard</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active"></li>
                    </ol>

                    <div class="row">
                        <div class="col-xl-3 col-md-6">
                            <div class="card bg-primary text-white mb-4">
                                <div class="card-body">
                                [${dashboard.datacenterCnt}]&nbsp;데이터센터 <br>
                                UP &nbsp;${dashboard.datacenterActive} / DOWN &nbsp; ${dashboard.datacenterInactive}
                                </div>
                                <div class="card-footer d-flex align-items-center justify-content-between">
                                    <a class="small text-white stretched-link" href="/computing/datacenters">더보기</a>
                                    <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6">
                            <div class="card bg-warning text-white mb-4">
                                <div class="card-body">
                                [${dashboard.clusterCnt}]&nbsp;클러스터<br>
                                ${dashboard.clusterCnt}
                                </div>
                                <div class="card-footer d-flex align-items-center justify-content-between">
                                    <a class="small text-white stretched-link" href="/computing/clusters">더보기</a>
                                    <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6">
                            <div class="card bg-success text-white mb-4">
                                <div class="card-body">
                                [${dashboard.hostCnt}]&nbsp;호스트<br>
                                UP &nbsp;${dashboard.hostActive} / DOWN &nbsp; ${dashboard.hostInactive}
                                </div>
                                <div class="card-footer d-flex align-items-center justify-content-between">
                                    <a class="small text-white stretched-link" href="/computing/hosts">더보기</a>
                                    <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6">
                            <div class="card bg-danger text-white mb-4">
                                <div class="card-body">
                                [${dashboard.vmCnt}]&nbsp;가상머신<br>
                                UP &nbsp;${dashboard.vmActive} / DOWN &nbsp; ${dashboard.vmInactive}
                                </div>
                                <div class="card-footer d-flex align-items-center justify-content-between">
                                    <a class="small text-white stretched-link" href="/computing/vms">더보기</a>
                                    <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-md-6">
                            <div class="card bg-info text-white mb-4">
                                <div class="card-body">
                                [${dashboard.storageDomainCnt}]&nbsp;데이터 스토리지 도메인<br>
                                UP &nbsp;${dashboard.storageDomainActive} / DOWN &nbsp; ${dashboard.storageDomainInactive}
                                </div>
                                <div class="card-footer d-flex align-items-center justify-content-between">
                                    <a class="small text-white stretched-link" href="/storages">더보기</a>
                                    <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-xl-3 col-md-6">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-area me-1"></i>
                                    전체사용량 - CPU
                                </div>
                                <div class="card-body">
                                  총 cpu : ${dashboard.cpuTotal}<br>
                                  할당 cpu : ${dashboard.cpuAssigned}

                                </div>
                            </div>
                        </div>

                        <div class="col-xl-3 col-md-6">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-area me-1"></i>
                                    전체사용량 - Memory
                                </div>
                                <div class="card-body">
                                  총 메모리: ${dashboard.memoryTotal/(1024*1024*1024)} GB<br>
                                  사용된 메모리 : ${dashboard.memoryUsed/(1024*1024*1024)} GB<br>
                                  사용가능 메모리 : ${dashboard.memoryFree/(1024*1024*1024)} GB
                                </div>
                            </div>
                        </div>

                        <div class="col-xl-3 col-md-6">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <i class="fas fa-chart-area me-1"></i>
                                    전체사용량 - Storage
                                </div>
                                <div class="card-body">
                                  총 스토리지: ${dashboard.storageTotal/(1024*1024*1024)} GB<br>
                                  사용된 스토리지 : ${dashboard.storageUsed/(1024*1024*1024)} GB<br>
                                  사용가능 스토리지 : ${dashboard.storageFree/(1024*1024*1024)} GB
                                </div>
                            </div>
                        </div>
                    </div>


                </div>
            </main>
        </div>
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
