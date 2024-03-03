<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript">

        $(document).ready(function(){

        	$("#ok").click(function(){

        		if($("#name").val() == ''){
        			alert("이름를 입력해주세요");
        			$("#name").focus();
        			return;
        		}

        		$("#add").submit();
        	});

        	$("#cancel").click(function(){
        	    window.close();
        	});

        });

    </script>

    <style>
        table, tr, td{
            border:none;
        }
    </style>

</head>
<body>
    <div style="padding: 1rem;">
        <div>
            <h2>새 논리적 네트워크</h2>
            <form id="add" autocomplete="off" method="post" action="network-add2">
                <hr><br>


                <h3>일반</h3>
                <table>
                    <tr>
                        <td>데이터 센터</td>
                        <td>&emsp;
                            <select id="datacenterId" name="datacenterId" onchange="updateDc()">
                                <option value="1ddd2876-a517-11ee-9388-00163e24ca5c">dc</option>
                            <!--
                                <c:forEach var="dc" items="${dc}" varStatus="status">
                                    <option value="${dc.id}">${dc.name}</option>
                                </c:forEach>
                            -->
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>이름</td>
                        <td>&emsp; <input type="text" id="name" name="name" size="20"></td>
                    </tr>
                    <tr>
                        <td>설명</td>
                        <td>&emsp; <input type="text" id="description" name="description"></td>
                    </tr>
                    <tr>
                        <td>코멘트</td>
                        <td>&emsp; <input type="text" id="comment" name="comment"></td>
                    </tr>


                    <tr>
                        <td><strong>네트워크 매개변수</strong></td>
                    </tr>

                    <tr>
                        <td>네트워크 레이블</td>
                        <td>&emsp; <input type="text" id="label" name="label"></td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="vlan" name="vlan" />&emsp; VLAN 태깅 활성화</td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="usageVm" name="usageVm" checked/>&emsp; 가상머신 네트워크</td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="portIsolation" name="portIsolation" />&emsp; 포트 분리</td>
                    </tr>

                    <tr>
                        <td><strong>MTU</strong></td>
                        <td>
                            <input type='radio' id="mtu" name='mtu' value='0' checked/> 기본 값(1500)<br>
                            <input type='radio' id="mtu" name='mtu'/> 사용자 정의<br>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="" name="" />&emsp; DNS 설정</td>
                    </tr>

                    <tr>
                        <td>DNS 서버</td>
                        <td>
                            &emsp; <input type="text" id="" name="">
                             &emsp; <input type="button" id="+" value="plus">&emsp; <input type="button" id="-" value="minus">
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="" name="" />&emsp; 외부 업체에서 작성</td>
                    </tr>


                    <tr>
                        <td><strong>외부</strong></td>
                    </tr>

                    <tr>
                        <td>외부 공급자</td>
                        <td>&emsp;
                            <select id="" name="" >
                                <option value="" selected></option>
                                <option value=""></option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>네트워크 포트 보안</td>
                        <td>&emsp;
                            <select id="" name="" >
                                <option value=""></option>
                                <option value=""></option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="" name="" />&emsp; 물리적 네트워크에 연결</td>
                    </tr>
                </table>
                <hr>

                <div>
                <h3>클러스터</h3>
                <table>
                    <p><strong>클러스터에서 네트워크를 연결/분리</strong></p>
                    <table>
                        <tr>
                            <td>이름</td>
                            <td><input type="checkbox" id="" name="" />모두 연결</td>
                            <td><input type="checkbox" id="" name="" />모두 필요</td>
                        </tr>
                            <td></td>
                            <td><input type="checkbox" id="" name="" />연결</td>
                            <td><input type="checkbox" id="" name="" />필요</td>
                        <tr>

                        </tr>
                    </table>
                </table>

                <div>
                <h3>vNIC 프로파일</h3>
                <table>

                    <tr>
                        <td><strong>외부</strong></td>
                    </tr>

                    <tr>
                        <td><input type="text" id="" name=""></td>
                        <td><input type="checkbox" id="" name="" selected/>공개</td>
                        <td><strong>QoS</strong></td>
                        <td>
                            <select id="" name="" >
                                <option value="none" selected>[제한 없음]</option>
                                <option value=""></option>
                            </select>
                        </td>
                        <td>
                            &emsp; <input type="button" id="+" value="plus">&emsp; <input type="button" id="-" value="minus">
                        </td>
                    </tr>

                </table>


                </div><br>

                <input type="button" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
