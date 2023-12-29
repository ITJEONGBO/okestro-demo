<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">호스트</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                여기는 호스트
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>이름</td>
                        <td>코멘트</td>
                        <td>호스트이름/IP</td>
                        <td>클러스터</td>
                        <td>데이터 센터</td>
                        <td>상태</td>
                        <td>가상머신</td>
                        <td>SPM</td>
                    </tr>

                    <c:if test="${empty hostVOList}">
                        <tr>
                            <td colspan="10">host 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="hostVOList" items="${hostVOList}" varStatus="status">
                        <tr>
                            <td>${hostVOList.status}</td>
                            <td></td>
                            <td> <a href="/computing/host?id=${hostVOList.id}">${hostVOList.name}</a> </td>
                            <td>${hostVOList.comment}</td>
                            <td>${hostVOList.address}</td>
                            <td><a href="/computing/cluster?id=${hostVOList.clusterId}">${hostVOList.clusterName}</a></td>
                            <td><a href="/computing/datacenter-storage?id=${hostVOList.datacenterId}">${hostVOList.datacenterName}</a></td>
                            <td>${hostVOList.status}</td>
                            <td>${hostVOList.vmCnt}</td>

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
