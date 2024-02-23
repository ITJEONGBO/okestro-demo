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
    var vid = "";

    function checkOnlyOne(element) {
        const checkboxes = document.getElementsByName("vid");

        checkboxes.forEach((cb) => { cb.checked = false; })
        element.checked = true;

        vid = element.value;
    }


	function openAdd() {
		window.open("vm-add", "mypopup", "width=550, height=700, top=150, left=200");
	}

	function openEdit() {
	    if(vid == ""){
	        alert("가상머신을 선택해주세요");
	        return;
        }
		window.open("vm-edit?id="+ vid, "mypopup", "width=550, height=700, top=150, left=200");

	}

	function openDelete() {
        if(vid == ""){
            alert("가상머신을 선택해주세요");
            return;
        }
		window.open("vm-delete?id=" + vid, "mypopup", "width=400, height=200, top=150, left=200");
	}
</script>

<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">VM</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                컴퓨팅 > <a href="/computing/vms">가상머신</a>
                            </p>
                        </div>
                    </div>
                    <div style="display: inline-block; margin: 0 5px;  float: center;">
                        <button type="button" class="btn btn-secondary" onclick="openAdd()">새로 만들기</button>
                        <button type="button" class="btn btn-secondary" onclick="openEdit()">편집</button>
                        <button type="button" class="btn btn-secondary" onclick="openDelete()">삭제</button>
                    </div>
                    <br><br>

                <table>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>이름</td>
                        <td>호스트</td>
                        <td>IP주소</td>
                        <td>FQDN</td>
                        <td>클러스터</td>
                        <td>데이터 센터</td>
                        <td>메모리</td>
                        <td>CPU</td>
                        <td>네트워크</td>
                        <td>상태</td>
                        <td>업타임</td>
                        <td>설명</td>
                    </tr>

                    <c:if test="${empty vms}">
                        <tr>
                            <td colspan="14">host 없음</td>
                        </tr>
                    </c:if>
                    <c:forEach var="vms" items="${vms}" varStatus="status">
                        <tr>
                            <td><input type="checkbox" id="vid" name="vid" value="${vms.id}" onclick="checkOnlyOne(this)"/></td>
                            <td>${vms.status == "up" ? "▲" : "▽"}</td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td><a href="/computing/vm?id=${vms.id}">${vms.name}</a> </td>
                            <td><a href="/computing/host?id=${vms.hostId}">${vms.hostName}</a></td>
                            <td>${vms.ipv4}<br> ${vms.ipv6}</td>
                            <td>${vms.fqdn}</td>
                            <td><a href="/computing/cluster?id=${vms.clusterId}">${vms.clusterName}</a></td>
                            <td><a href="/computing/datacenter-storage?id=${vms.datacenterId}">${vms.datacenterName}</a></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>${vms.status == "up" ? "실행 중" : vms.status}</td>
                            <td>${vms.upTime}</td>
                            <td>${vms.description}</td>
                        </tr>
                    </c:forEach>
                </table>

                </div>
            </main>
        </div>

    </body>
</html>
