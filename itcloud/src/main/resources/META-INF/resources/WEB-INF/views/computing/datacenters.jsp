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

    var dcId = "";

    function checkOnlyOne(element) {
        const checkboxes = document.getElementsByName("dcid");

        checkboxes.forEach((cb) => { cb.checked = false; })
        element.checked = true;

        dcId = element.value;
    }


	function openAdd() {
		window.open("datacenter-add", "mypopup", "width=400, height=550, top=150, left=200");
	}

	function openEdit() {
	    if(dcId == ""){
	        alert("데이터센터를 선택해주세요");
	        return;
        }
		window.open("datacenter-edit?id="+ dcId, "mypopup", "width=400, height=550, top=150, left=200");

	}

	function openDelete() {
        if(dcId == ""){
            alert("데이터센터를 선택해주세요");
            return;
        }
		window.open("datacenter-delete?id=" + dcId, "mypopup", "width=400, height=200, top=150, left=200");
	}

</script>

<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">DataCenter</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/datacenters" style="text-decoration-line: none">데이터 센터</a>
                            </p>
                        </div>
                    </div>

            <div>
                <div style="display: inline-block; margin: 0 5px;  float: center;">
                    <button type="button" class="btn btn-secondary" onclick="openAdd()">새로 만들기</button>
                    <button type="button" class="btn btn-secondary" onclick="openEdit()">편집</button>
                    <button type="button" class="btn btn-secondary" onclick="openDelete()">삭제</button>
                </div>
                    <br><br>

                <table id="tab" class="table table-bordered table-hover text-center">
                  <thead>
                    <tr class="table-secondary">
                        <td></td>
                        <td></td>
                        <td></td>
                        <td width="200px">이름</td>
                        <td>코멘트</td>
                        <td>스토리지 유형</td>
                        <td>상태</td>
                        <td>호환 버전</td>
                        <td>설명</td>
                    </tr>
                  </thead>

                    <c:if test="${empty datacenters}">
                    <tbody>
                        <tr>
                            <td>데이터센터가 없음 / 근데 이거 말이 안됨요</td>
                        </tr>
                    </c:if>
                    <c:forEach var="datacenters" items="${datacenters}" varStatus="status">
                        <tr>
                            <td><input type="checkbox" id="dcid" name="dcid" value="${datacenters.id}" onclick="checkOnlyOne(this)"/></td>
                            <td>${datacenters.status == "up" ? "▲" : "▽"}</td>
                            <td>&nbsp;&nbsp;&nbsp;</td>
                            <td>${datacenters.name}</td>
                            <td>${datacenters.comment}</td>
                            <td>${datacenters.storageType ? "로컬" : "공유됨"}</td>
                            <td>${datacenters.status}</td>
                            <td>${datacenters.version}</td>
                            <td>${datacenters.description}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </div>

                </div>
            </main>
        </div>


    </body>
</html>
