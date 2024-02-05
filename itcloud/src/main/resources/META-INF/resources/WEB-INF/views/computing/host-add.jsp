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
                        <td id="hostT">&emsp;
                            <select id="datacenterId" name="datacenterId">
                                <c:forEach var="dc" items="${dc}" varStatus="status">
                                    <option value="${dc.id}">${dc.name}</option>
                                </c:forEach>
                            </select>
                            <br>
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
                        <td><input type="text" id="hostIp" name="description"></td>
                    </tr>
                    <tr>
                        <td id="hostT">SSH 포트</td>
                        <td><input type="text" id="sshPort" name="sshPort" value="22"></td>
                    </tr><br>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="active" name="active" checked/> 설치 후 호스트를 활성화</td>
                    </tr>

                    <tr>
                        <td colspan="2"><input type="checkbox" id="restart" name="restart" checked/> 설치 후 호스트를 다시 시작</td>
                    </tr><br>

                    <tr>
                        <td>인증</td>
                    </tr>
                    <tr>
                        <td>사용자 이름</td>
                        <td><input type="text" id="userName" name="userName" value="root" disabled></td>
                    </tr>

                    <tr>
                        <td> <input type='radio' id="password" name='password' checked/> 암호 </td>
                        <td><input type="password" id="password" name="password" value="root" disabled></td>
                    </tr>

                    <tr>
                        <td> <input type='radio' id="sshPublicKey" name='sshPublicKey'/> SSH 공개 키 </td>
                    </tr><br>
                </table>
                <br><hr>

                <h3>전원 관리</h3>
                <table>
                    <tr>
                        <td><input type="checkbox" id="powerActive" name="powerActive" checked/>&emsp; 전원 관리 활성화</td>
                    </tr><br>
                    <tr>
                        <td><input type="checkbox" id="kdump" name="kdump" checked disabled/>&emsp; powerActive</td>
                    </tr><br>
                    <tr>
                        <td><input type="checkbox" id="powerPolicy" name="powerPolicy" checked disabled/>&emsp; 전원 관리 정책 제어를 비활성화</td>
                    </tr><br>
                </table>
                <br><hr>

                <h3>호스트 엔진</h3>
                <table>
                    <tr>
                        <td>호스트 엔진 배치 작업 선택</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="false">없음</option>
                                <option value="true">배포</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <br><hr>

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
                    </tr<br>

                    <tr>
                        <td>선택된 선호도 그룹</td>
                        <td></td>
                    </tr><br>

                    <tr>
                        <td>선호도 레이블 선택</td>
                        <td>
                            <select id="hostEngine" name="hostEngine">
                                <option value="false">없음</option>
                                <option value="true">배포</option>
                            </select>
                        </td>
                    </tr><br>

                   <tr>
                       <td>선택한 선호도 레이블</td>
                       <td> </td>
                   </tr>
                </table>
                <br><hr>
                <br><br><br>

                <input type="submit" id="ok" value="OK">
                <input type="reset" id="cancel" value="취소">
            </form>
        </div>

    </div>

</body>
</html>
