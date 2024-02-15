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

        		if($("#hostIp").val() == ''){
        			alert("호스트이름/IP를 입력해주세요");
        			$("#hostIp").focus();
        			return;
        		}

        		if($("#password").val() == ''){
        			alert("암호를 입력해주세요");
        			$("#password").focus();
        			return;
        		}

        		$("#add").submit();
        	});

        	$("#test").click(function(){
                alert("test");
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
        #hostT{
            width: 140px;
        }

    </style>

</head>
<body>
    <div style="padding: 1rem;">
        <div>
            <h2>새 호스트</h2>

            <form id="add" autocomplete="off" method="get" action="host-add2">

                <hr>
                <h3>일반</h3>
                <table>
                    <tr>
                        <td id="hostT">호스트 클러스터</td>
                        <td>
                            <select id="datacenterId" name="datacenterId">
                                <c:forEach var="dc" items="${dc}" varStatus="status">
                                    <option value="${dc.id}">${dc.name}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td id="hostT">이름</td>
                        <td><input type="text" id="name" name="name" size="20"></td>
                    </tr>

                    <tr>
                        <td id="hostT">코멘트</td>
                        <td><input type="text" id="comment" name="comment"></td>
                    </tr>
                    <tr>
                        <td id="hostT">호스트이름/IP</td>
                        <td><input type="text" id="hostIp" name="hostIp"></td>
                    </tr>
                    <tr>
                        <td id="hostT">SSH 포트</td>
                        <td><input type="text" id="sshPort" name="sshPort" value="22"></td>
                    </tr>

                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input type="checkbox" id="active" name="active" checked/> 설치 후 호스트를 활성화</td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="restart" name="restart" checked/> 설치 후 호스트를 다시 시작</td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td><strong>인증</strong></td>
                    </tr>
                    <tr>
                        <td>사용자 이름</td>
                        <td><input type="text" id="user" name="userName" value="root" disabled></td>
                    </tr>
                    <tr>
                        <td>암호</td>
                        <td><input type="password" id="password" name="password" ></td>
                    </tr>

                    <!--
                        <tr>
                            <td> <input type='radio' id="sshPublicKey" name='sshPublicKey'/> SSH 공개 키 </td>
                        </tr>

                        <tr>
                            <td><br></td>
                        </tr>

                        <tr>
                            <td> 고급 매개 변수 </td>
                        </tr>
                    -->
                </table>
                <hr>

                <!--
                <h3>전원 관리</h3>
                <table>
                    <tr>
                        <td><input type="checkbox" id="powerActive" name="powerActive" checked/>&emsp; 전원 관리 활성화</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="kdump" name="kdump" checked disabled/>&emsp; kdump 통합</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" id="powerPolicy" name="powerPolicy" checked disabled/>&emsp; 전원 관리 정책 제어를 비활성화</td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td><strong>순서대로 정렬된 에이전트</strong></td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>

                    <tr>
                        <td><strong>펜스 에이전트 추가(편집)</strong></td>
                    </tr>

                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td>주소</td>
                        <td><input type="text" id="fenceAddress" name="fenceAddress" size="20"></td>
                    </tr>
                    <tr>
                        <td>사용자 이름</td>
                        <td><input type="text" id="fenceUserName" name="fenceUserName" size="20"></td>
                    </tr>
                    <tr>
                        <td>암호</td>
                        <td><input type="text" id="fencePassword" name="fencePassword" size="20"></td>
                    </tr>
                    <tr>
                        <td>유형</td>
                        <td>
                            <select id="fenceType" name="fenceType">
                                <option value="">amt_ws</option>
                                <option value="">apc</option>
                                <option value="">apc_snmp</option>
                                <option value="">bladecenter</option>
                                <option value="">cisco_ucs</option>
                                <option value="">drac5</option>
                                <option value="">drac7</option>
                                <option value="">eps</option>
                                <option value="">hpblade</option>
                                <option value="">ilo</option>
                                <option value="">ilo2</option>
                                <option value="">ilo3</option>
                                <option value="">ilo4</option>
                                <option value="">ilo_ssh</option>
                                <option value="">ipmilan</option>
                                <option value="">redfish</option>
                                <option value="">rsa</option>
                                <option value="">rsb</option>
                                <option value="">wti</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>SSH 포트</td>
                        <td><input type="text" id="fenceSsh" name="fenceSsh" size="20"></td>
                    </tr>
                    <tr>
                        <td>옵션</td>
                        <td><input type="text" id="fenceOption" name="fenceOption" size="20"></td>
                    </tr>
                    <tr>
                        <td>콤마로 구분된 "key=value" 목록을 사용하십시오</td>
                    </tr>
                    <tr>
                        <td><br></td>
                    <tr>
                        <td><input type="button" id="test" value="test"></td>
                    </tr>
                </table>
                <hr>
                -->


                <h3>SPM</h3>
                <table>
                    <tr>
                        <td> <input type="radio" id="spm" name="spm" value="-1"/> 없음 </td>
                        <td> <input type="radio" id="spm" name="spm" value="2"/> 낮음 </td>
                        <td> <input type="radio" id="spm" name="spm" value="5" checked/> 표준 </td>
                        <td> <input type="radio" id="spm" name="spm" value="7"/> 높음 </td>
                    </tr>
                </table>
                <hr>

                <h3>호스트 엔진</h3>
                <table>
                    <tr>
                        <td>호스트 엔진 배치 작업 선택</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="true">배포</option>
                                <option value="false">없음</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <hr>

                <h3>선호도</h3>
                <table>
                    <tr>
                        <td>선호도 그룹을 선택하십시오</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="false">없음</option>
                                <option value="true">배포</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td>선택된 선호도 그룹</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                    <tr>
                        <td>선호도 레이블 선택</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="false">없음</option>
                                <option value="true">배포</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><br></td>
                    </tr>
                   <tr>
                       <td>선택한 선호도 레이블</td>
                       <td> </td>
                   </tr>
                </table>
                <hr>

                <input type="button" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
