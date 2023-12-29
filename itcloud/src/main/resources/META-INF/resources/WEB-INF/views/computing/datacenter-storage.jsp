<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">데이터센터</h1>
                    <a href="/computing/datacenters" style="text-decoration-line: none">데이터센터</a> - 스토리지 <br><br>

                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                <a href="/computing/datacenter-storage?id=${id}">스토리지</a> |
                                <a href="/computing/datacenter-network?id=${id}" style="text-decoration-line: none">논리 네트워크</a> |
                                <a href="/computing/datacenter-cluster?id=${id}"  style="text-decoration-line: none">클러스터</a> |
                                <a href="#permission"  style="text-decoration-line: none">권한</a>
                            </p>
                        </div>
                    </div>

                <table>
                    <tr>
                        <td>도메인 이름</td>
                        <td>도메인 유형</td>
                        <td>상태</td>
                        <td>여유공간(GB)</td>
                        <td>사용된 공간</td>
                        <td>전체공간(GB)</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty storage}">
                        <tr>
                            <td>스토리지 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="storage" items="${storage}" varStatus="status">
                        <tr>
                            <td>${storage.name}</td>
                            <td>${storage.domainType}</td>
                            <td>${storage.status}</td>
                            <td>${storage.availableSize /(1024*1024*1024)}GB</td>
                            <td>${storage.usedSize /(1024*1024*1024)}GB</td>
                            <td>${storage.diskSize /(1024*1024*1024)}GB</td>
                            <td>${storage.description}</td>
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
