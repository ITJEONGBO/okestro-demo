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

    var nId = "";

    function checkOnlyOne(element) {
        const checkboxes = document.getElementsByName("nid");
        checkboxes.forEach((cb) => { cb.checked = false; })
        element.checked = true;

        nId = element.value;
    }


	function openAdd() {
		window.open("network-add", "mypopup", "width=550, height=700, top=150, left=50");
	}

	function openEdit() {
	    if(nId == ""){
            alert("네트워크를 선택해주세요");
            return;
        }
		window.open("network-edit?id=" +nId, "mypopup", "width=550, height=700, top=150, left=50");
	}

    function openDelete() {
        if(nId == ""){
            alert("네트워크를 선택해주세요");
            return;
        }
        window.open("network-delete?id=" +nId, "mypopup", "width=450, height=200, top=150, left=50");
    }

</script>

<%@ include file="../base/header.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Network</h1>
                    <div class="card mb-4">
                        <div class="card-body">
                            <p class="mb-0">
                                네트워크 > <a href="/network/networks" style="text-decoration-line: none">네트워크</a>
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
                        <td>이름</td>
                        <td>코멘트</td>
                        <td>데이터 센터</td>
                        <td>설명</td>
                        <td>역할</td>
                        <td>VLAN 태그</td>
                        <td>레이블</td>
                        <td>공급자</td>
                        <td>MTU</td>
                        <td>포트 분리</td>
                    </tr>

                    <c:if test="${empty networks}">
                        <tr>
                            <td colspan="10" style="text-align: center">표시할 항목이 없습니다</td>
                        </tr>
                    </c:if>
                    <c:forEach var="networks" items="${networks}" varStatus="status">
                        <tr>
                            <td><input type="checkbox" id="nid" name="nid" value="${networks.id}" onclick="checkOnlyOne(this)"/></td>
                            <td><a href="/network/network?id=${networks.id} "style="text-decoration-line: none">${networks.name}</a></td>
                            <td>${networks.comment}</td>
                            <td><a href="/computing/datacenter-storage?id=${networks.datacenterId} "style="text-decoration-line: none">${networks.datacenterName}</a></td>
                            <td>${networks.description}</td>
                            <td>
                                ${networks.networkUsageVo.vm =="true" ? "vm":""}
                                ${networks.networkUsageVo.management =="true" ? "management":""}
                                ${networks.networkUsageVo.display =="true" ? "display":""}
                                ${networks.networkUsageVo.migration =="true" ? "migration":""}
                                ${networks.networkUsageVo.gluster =="true" ? "gluster":""}
                                ${networks.networkUsageVo.defaultRoute =="true" ? "defaultRoute":""}
                            </td>
                            <td>${networks.vlan == null ? "-" : networks.vlan}</td>
                            <td>${networks.label == null ? "-" : networks.label}</td>
                            <td><a href="#" style="text-decoration-line: none">${networks.providerName}</a></td>
                            <td>${networks.mtu == 0 ? "기본값(1500)" : networks.mtu}</td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </table>
                <br><br><br><br>
                </div>
            </main>
        </div>

    </body>
</html>
