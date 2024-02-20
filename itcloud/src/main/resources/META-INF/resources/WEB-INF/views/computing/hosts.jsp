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

    var hid = "";

    function checkOnlyOne(element) {
        const checkboxes = document.getElementsByName("hid");

        checkboxes.forEach((cb) => { cb.checked = false; })
        element.checked = true;

        hid = element.value;
    }


	function openAdd() {
		window.open("host-add", "mypopup", "width=550, height=700, top=150, left=200");
	}

	function openEdit() {
	    if(hid == ""){
	        alert("호스트를 선택해주세요");
	        return;
        }
		window.open("host-edit?id="+ hid, "mypopup", "width=550, height=700, top=150, left=200");

	}

	function openDelete() {
        if(hid == ""){
            alert("호스트를 선택해주세요");
            return;
        }
		window.open("host-delete?id=" + hid, "mypopup", "width=400, height=200, top=150, left=200");
	}

	function openDeactivate() {
	    if(hid == ""){
            alert("호스트를 선택해주세요");
            return;
        }
	    window.open("host-deactive?id=" + hid, "mypopup", "width=400, height=200, top=150, left=200");
    }

    function openActivate() {
	    if(hid == ""){
            alert("호스트를 선택해주세요");
            return;
        }
	    window.open("host-active?id=" + hid, "mypopup", "width=400, height=200, top=150, left=200");
    }

    function openRefresh() {
	    if(hid == ""){
            alert("호스트를 선택해주세요");
            return;
        }
	    window.open("host-refresh?id=" + hid, "mypopup", "width=400, height=200, top=150, left=200");
    }

    function openRestart() {
        if(hid == ""){
            alert("호스트를 선택해주세요");
            return;
        }
        window.open("host-restart?id=" + hid, "mypopup", "width=400, height=200, top=150, left=200");
    }

</script>
<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Host</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/hosts" style="text-decoration-line: none">호스트</a>
                            </p>
                        </div>
                    </div>

                <div style="display: inline-block; margin: 0 5px;  float: center;">
                    <button type="button" class="btn btn-secondary" onclick="openAdd()">새로 만들기</button>
                    <button type="button" class="btn btn-secondary" onclick="openEdit()">편집</button>
                    <button type="button" class="btn btn-secondary" onclick="openDelete()">삭제</button>
                    &nbsp;&nbsp;&nbsp;
                    <button type="button" class="btn btn-secondary" onclick="openDeactivate()">유지보수</button>
                    <button type="button" class="btn btn-secondary" onclick="openActivate()">활성</button>
                    <button type="button" class="btn btn-secondary" onclick="openRefresh()">새로고침</button>
                    &nbsp;&nbsp;&nbsp;
                    <label>ssh 관리</label>
                    <button type="button" class="btn btn-secondary" onclick="openRestart()">재시작</button>
                    <button type="button" class="btn btn-secondary" onclick="openStart()">시작</button>
                    <button type="button" class="btn btn-secondary" onclick="openStop()">중지</button>
                    <button type="button" class="btn btn-secondary" onclick="">SPM으로 선택</button>
                    <button type="button" class="btn btn-secondary" onclick="">로컬 스토리지 설정</button>

                </div>
                <br><br>

                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>이름</td>
                        <td>코멘트</td>
                        <td>호스트이름/IP</td>
                        <td>클러스터</td>
                        <td>데이터센터</td>
                        <td>상태</td>
                        <td>가상머신</td>
                        <td>메모리</td>
                        <td>CPU</td>
                        <td>네트워크</td>
                    </tr>

                    <c:if test="${empty hosts}">
                        <tr>
                            <td colspan="12">host 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="hosts" items="${hosts}" varStatus="status">
                        <tr>
                            <td><input type="checkbox" id="hid" name="hid" value="${hosts.id}" onclick="checkOnlyOne(this)"/></td>
                            <td>${hosts.status == "up" ? "▲" : "▽"}</td>
                            <td>${hosts.hostedEngine == "true" ? "★" : (hosts.hostedEngine == null ? "" : "☆") }</td>
                            <td id="hostname"> <a href="/computing/host?id=${hosts.id}">${hosts.name}</a> </td>
                            <td>${hosts.comment}</td>
                            <td>${hosts.address}</td>
                            <td><a href="/computing/cluster?id=${hosts.clusterId}">${hosts.clusterName}</a></td>
                            <td><a href="/computing/datacenters">${hosts.datacenterName}</a></td>
                            <td>${hosts.status}</td>
                            <td>${hosts.vmCnt}</td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </table>

                </div>
            </main>
        </div>

    </body>
</html>
