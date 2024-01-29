<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<%@ include file="../base/header.jsp" %>

<script type="text/javascript">
	function openAdd() {
		window.open("cluster-add", "mypopup", "width=550, height=550, top=150, left=200");
	}

	function openEdit() {
		window.open("cluster-edit", "mypopup", "width=500, height=350, top=150, left=200");
	}

	function test() {
		alert("test");
	}
</script>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Cluster</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/clusters" style="text-decoration-line: none">클러스터</a>
                            </p>
                        </div>
                    </div>


                    <div style="display: inline-block; margin: 0 5px;  float: center;">
                        <button type="button" class="btn btn-secondary" onclick="openAdd()">새로 만들기</button>
                        <button type="button" class="btn btn-secondary" onclick="openEdit()">편집</button>
                        <button type="button" class="btn btn-secondary" onclick="test()">삭제</button>
                    </div>
                        <br><br>

                <table>
                    <tr>
                        <td>상태</td>
                        <td>이름</td>
                        <td>코멘트</td>
                        <td>호환 버전</td>
                        <td>설명</td>
                        <td>클러스터 CPU 유형</td>
                        <td>호스트 수</td>
                        <td>가상머신 수</td>
                        <td>업그레이드 상태</td>
                    </tr>

                    <c:if test="${empty clusters}">
                        <tr>
                            <td>클러스터가 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="clusters" items="${clusters}" varStatus="status">
                        <tr>
                            <td></td>
                            <td><a href="/computing/cluster?id=${clusters.id}">${clusters.name}</a> </td>
                            <td>${clusters.comment}</td>
                            <td>${clusters.version}</td>
                            <td>${clusters.description}</td>
                            <td>${clusters.cpuType}</td>
                            <td>${clusters.hostCnt}</td>
                            <td>${clusters.vmCnt}</td>
                            <td></td>
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
