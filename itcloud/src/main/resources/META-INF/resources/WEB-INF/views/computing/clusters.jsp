<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script src="js/scripts.js"></script>

<script type="text/javascript">

    var cId = "";

    function checkOnlyOne(element) {
        const checkboxes = document.getElementsByName("cid");
        checkboxes.forEach((cb) => { cb.checked = false; })
        element.checked = true;

        cId = element.value;
    }


	function openAdd() {
		window.open("cluster/add", "mypopup", "width=550, height=700, top=150, left=50");
	}

	function openEdit() {
	    if(cId == ""){
            alert("클러스터를 선택해주세요");
            return;
        }
		window.open("cluster/edit?id=" +cId, "mypopup", "width=550, height=700, top=150, left=50");
	}

    function openDelete() {
        if(cId == ""){
            alert("클러스터를 선택해주세요");
            return;
        }
        window.open("cluster/delete?id=" + cId, "mypopup", "width=450, height=200, top=150, left=50");
    }

</script>

<%@ include file="../base/header.jsp" %>
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
                        <button type="button" class="btn btn-secondary" onclick="openDelete()">삭제</button>
                    </div>
                        <br><br>

                <table class="table table-bordered table-hover text-center">
                    <thead>
                        <tr class="table-secondary">
                            <td></td>
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
                    </thead>

                    <tbody>
                    <c:if test="${empty clusters}">
                        <tr>
                            <td>클러스터가 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="clusters" items="${clusters}" varStatus="status">
                        <tr>
                            <td><input type="checkbox" id="cid" name="cid" value="${clusters.id}" onclick="checkOnlyOne(this)"/></td>
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
                    </tbody>
                </table>

                </div>
            </main>
        </div>

    </body>
</html>
