<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Network</h1>
                    네트워크 > <a href="/network/networks" style="text-decoration-line: none">네트워크 > ${name} </a><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/network/network?id=${id}">일반</a> |
                                <a href="/network/network-vnicProfile?id=${id}" style="text-decoration-line: none">vNIC 프로파일</a> |
                                <a href="/network/network-cluster?id=${id}" style="text-decoration-line: none">클러스터</a> |
                                <a href="/network/network-host?id=${id}" style="text-decoration-line: none">호스트</a> |
                                <a href="/network/network-vm?id=${id}" style="text-decoration-line: none">가상머신</a> |
                                <a href="/network/network-template?id=${id}" style="text-decoration-line: none">템플릿</a> |
                                <a href="/network/network-permission?id=${id}" style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                    <div>
                                이름: ${network.name} <br>
                                Id: ${network.id} <br>
                                설명: ${network.description} <br>
                                VDSM 이름: ${network.vdsmName} <br><br>

                                가상머신 네트워크:  <br>
                                VLAN 태그: ${network.vlan == null ? "없음" : network.vlan} <br>
                                MTU: ${network.mtu == 0 ? "기본값(1500)" : network.mtu} <br>
                    </div>
                    <br><br><br><br><br>

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
